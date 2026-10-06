package by.it.group510902.shust.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private Object[] elements = new Object[16];
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(Object o) {
        int left = 0;
        int right = size - 1;
        Comparable<? super E> key = (Comparable<? super E>) o;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = key.compareTo((E) elements[mid]);
            if (cmp < 0) {
                right = mid - 1;
            } else if (cmp > 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -(left + 1);
    }

    @Override
    public boolean add(E e) {
        int index = binarySearch(e);
        if (index >= 0) return false;

        int insertionPoint = -(index + 1);
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) return false;

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                modified = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                modified = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        return modified;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}