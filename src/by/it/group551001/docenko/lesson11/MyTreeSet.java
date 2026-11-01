package by.it.group551001.docenko.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private E[] elements = (E[]) new Object[DEFAULT_CAPACITY];
    private int size = 0;
    private int modCount = 0;
    private final Comparator<? super E> comparator;

    public MyTreeSet() {
        this.comparator = null;
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    private int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((E) a, (E) b);
        }
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int binarySearch(Object o) {
        if (o == null && comparator == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], o);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    private void grow() {
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        modCount++;
    }

    private boolean filter(Collection<?> c, boolean removeMatching) {
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r]) != removeMatching) {
                elements[w++] = elements[r];
            }
        }
        if (w == size) {
            return false;
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        size = w;
        modCount++;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        return sb.append(']').toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        modCount++;
    }

    @Override
    public boolean add(E e) {
        int index = binarySearch(e);
        if (index >= 0) {
            return false;
        }
        int insertAt = -(index + 1);
        if (size == elements.length) {
            grow();
        }
        for (int i = size; i > insertAt; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertAt] = e;
        size++;
        modCount++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, false);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Collection<?> other = (Collection<?>) o;
        return other.size() == size && containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < size; i++) {
            h += elements[i].hashCode();
        }
        return h;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor++;
                return elements[lastRet];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            }
        };
    }
}
