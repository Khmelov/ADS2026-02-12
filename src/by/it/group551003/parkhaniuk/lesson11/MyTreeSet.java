package by.it.group551003.parkhaniuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size = 0;
    private int capacity = 10;

    public MyTreeSet() {
        elements = (E[]) new Object[capacity];
    }

    private int binarySearch(Object target) {
        int low = 0;
        int high = size - 1;

        Comparable<Object> key = (Comparable<Object>) target;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = key.compareTo(elements[mid]);

            if (cmp > 0) {
                low = mid + 1;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -low - 1;
    }

    private void ensureCapacity() {
        if (size >= capacity) {
            capacity = (capacity * 3) / 2 + 1;
            E[] newElements = (E[]) new Object[capacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
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
        if (o == null) {
            return false;
        }
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        }

        int index = binarySearch(e);

        if (index >= 0) {
            return false;
        }

        int insertionPoint = -(index + 1);

        ensureCapacity();

        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        int index = binarySearch(o);

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
    public String toString() {
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
        boolean changed = false;
        for (E item : c) {
            if (add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            if (remove(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                i--; // Корректируем индекс после удаления элемента
                changed = true;
            }
        }
        return changed;
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
    public <T> T[] toArray(T[] a) {
        return null;
    }
}

