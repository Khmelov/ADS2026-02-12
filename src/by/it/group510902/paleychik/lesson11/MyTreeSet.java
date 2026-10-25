package by.it.group510902.paleychik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] elements;
    private int size;

    public MyTreeSet() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> key = (Comparable<? super E>) o;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // Найдено
            }
        }
        return -(low + 1); // Не найдено
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
    }

    @Override
    public boolean contains(Object o) {
        if (o == null || size == 0) {
            return false;
        }
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not supported");
        }
        int idx = binarySearch(e);
        if (idx >= 0) {
            return false;
        }

        int insertionPoint = -(idx + 1);
        ensureCapacity();

        for (int i = size; i > insertionPoint; i--) {
            elements[i] = elements[i - 1];
        }

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || size == 0) {
            return false;
        }
        int idx = binarySearch(o);
        if (idx < 0) {
            return false;
        }

        for (int i = idx; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // оставшиеся методы Set
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}