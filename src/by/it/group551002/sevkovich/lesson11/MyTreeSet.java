package by.it.group551002.sevkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private Object[] array;
    private int count;

    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        array = new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compareObject(Object a, E b) {
        if (a == null || b == null) {
            throw new NullPointerException("null is not supported");
        }
        return ((Comparable) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private int find(Object o) {
        if (o == null) {
            throw new NullPointerException("null is not supported");
        }

        int low = 0;
        int high = count - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) array[mid];
            int cmp = compareObject(o, midVal);

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }

        return -(low + 1);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= array.length) {
            return;
        }

        int newCapacity = array.length * 2;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }

        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < count; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object o) {
        return find(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("null is not supported");
        }

        int idx = find(e);
        if (idx >= 0) {
            return false;
        }

        int insertIndex = -idx - 1;
        ensureCapacity(count + 1);

        for (int i = count; i > insertIndex; i--) {
            array[i] = array[i - 1];
        }

        array[insertIndex] = e;
        count++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = find(o);
        if (idx < 0) {
            return false;
        }

        for (int i = idx; i < count - 1; i++) {
            array[i] = array[i + 1];
        }

        array[--count] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < count; i++) {
            array[i] = null;
        }
        count = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            boolean modified = !isEmpty();
            clear();
            return modified;
        }

        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            return false;
        }

        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (index >= count) {
                    throw new NoSuchElementException();
                }
                lastReturned = index;
                return (E) array[index++];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }

                @SuppressWarnings("unchecked")
                E e = (E) array[lastReturned];

                MyTreeSet.this.remove(e);
                index = lastReturned;
                lastReturned = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[count];
        for (int i = 0; i < count; i++) {
            result[i] = array[i];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < count) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), count);
        }

        for (int i = 0; i < count; i++) {
            a[i] = (T) array[i];
        }

        if (a.length > count) {
            a[count] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;
        return containsAll(c);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < count; i++) {
            Object e = array[i];
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }
}