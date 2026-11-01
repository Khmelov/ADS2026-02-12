package by.it.group551002.voroshko.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {
    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private int compare(E a, E b) {
        return ((Comparable<Object>)a).compareTo(b);
    }

    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, key);

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

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 3 / 2;
            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }

        sb.append("]");
        return sb.toString();
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
        int index = binarySearch(element);

        if (index >= 0) {
            return false;
        }

        int insertPos = -(index + 1);

        ensureCapacity();

        System.arraycopy(elements, insertPos, elements, insertPos + 1, size - insertPos);

        elements[insertPos] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index;
        E e = (E) element;
        index = binarySearch(e);

        if (index < 0) {
            return false;
        }

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;

        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        E e = (E) element;

        return binarySearch(e) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean modified = false;
        for (E element : collection) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (size == 0) {
            return false;
        }

        boolean modified = false;

        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            E e = (E) elements[readIndex];
            if (!collection.contains(e)) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (size == 0) {
            return false;
        }

        boolean modified = false;

        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            E e = (E) elements[readIndex];
            if (collection.contains(e)) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }
}
