package by.it.group510902.sorokoletov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E extends Comparable<? super E>> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = midVal.compareTo(key);
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (o == null) return false;
        E key;
        try {
            key = (E) o;
        } catch (ClassCastException ex) {
            return false;
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp;
            try {
                cmp = midVal.compareTo(key);
            } catch (ClassCastException ex) {
                return false;
            }
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int pos = binarySearch(e);
        if (pos >= 0) return false;
        int insertPos = -(pos + 1);
        if (size == elements.length) grow();

        for (int i = size; i > insertPos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) return false;
        E key;
        try {
            key = (E) o;
        } catch (ClassCastException ex) {
            return false;
        }
        int pos;
        try {
            pos = binarySearch(key);
        } catch (ClassCastException ex) {
            return false;
        }
        if (pos < 0) return false;

        for (int i = pos; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E item : c) {
            if (add(item)) modified = true;
        }
        return modified;
    }

    private boolean collectionContains(Collection<?> c, Object value) {
        for (Object item : c) {
            if (item == null) {
                if (value == null) return true;
            } else if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E value = it.next();
            if (collectionContains(c, value)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E value = it.next();
            if (!collectionContains(c, value)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                MyTreeSet.this.remove(elements[lastRet]);
                cursor = lastRet;
                lastRet = -1;
            }
        };
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
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}