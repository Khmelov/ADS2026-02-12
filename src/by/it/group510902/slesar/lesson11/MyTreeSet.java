package by.it.group510902.slesar.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private E[] elements;
    private int size;

    public MyTreeSet() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
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
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int index = findInsertIndex(e);

        if (index < size && compare(elements[index], e) == 0) {
            return false;
        }

        ensureCapacity();

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = e;
        size++;

        return true;
    }

    @Override
    public boolean contains(Object o) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            int comparison = compare(elements[middle], (E) o);

            if (comparison == 0) {
                return true;
            }

            if (comparison < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = findInsertIndex((E) o);

        if (index >= size || compare(elements[index], (E) o) != 0) {
            return false;
        }

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        return true;
    }

    private int findInsertIndex(E e) {
        int left = 0;
        int right = size;

        while (left < right) {
            int middle = (left + right) / 2;

            if (compare(elements[middle], e) < 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    private int compare(E first, E second) {
        return ((Comparable<E>) first).compareTo(second);
    }

    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }

        E[] newElements = (E[]) new Object[elements.length * 2];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }

            result += elements[i];
        }

        result += "]";

        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;

        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (Object element : c) {
            if (remove(element)) {
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
    public java.util.Iterator<E> iterator() {
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