package by.it.group551004.podvitelskiymichael.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    private int search(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = ((Comparable<Object>) elements[mid]).compareTo(key);
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

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int index = search(element);
        if (index >= 0) {
            return false;
        }
        int position = -index - 1;
        if (size == elements.length) {
            grow();
        }
        for (int i = size; i > position; i--) {
            elements[i] = elements[i - 1];
        }
        elements[position] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = search(o);
        if (index < 0) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return search(o) >= 0;
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
        return removeMatching(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeMatching(c, false);
    }

    private boolean removeMatching(Collection<?> c, boolean removeIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (c.contains(elements[read]) != removeIfContained) {
                elements[write++] = elements[read];
            }
        }
        boolean modified = write != size;
        for (int i = write; i < size; i++) {
            elements[i] = null;
        }
        size = write;
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}