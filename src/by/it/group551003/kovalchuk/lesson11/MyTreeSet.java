package by.it.group551003.kovalchuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private int capacity;

    public MyTreeSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.elements = new Object[capacity];
        this.size = 0;
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
    public boolean add(Object o) {
        if (o == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, что элемент реализует Comparable
        if (!(o instanceof Comparable)) {
            throw new ClassCastException("Element must implement Comparable");
        }

        @SuppressWarnings("unchecked")
        Comparable<E> element = (Comparable<E>)o;

        if (contains(o)) {
            return false;
        }

        if (size >= capacity) {
            resize();
        }

        insertSorted((E)o);
        size++;

        return true;
    }

    @SuppressWarnings("unchecked")
    private void insertSorted(E element) {
        int pos = size;
        for (int i = 0; i < size; i++) {
            E current = (E)elements[i];
            if (((Comparable<E>)element).compareTo(current) < 0) {
                pos = i;
                break;
            }
        }

        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }

        elements[pos] = element;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        int index = findIndex(o);
        if (index == -1) {
            return false;
        }

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;

        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        return findIndex(o) != -1;
    }

    @SuppressWarnings("unchecked")
    private int findIndex(Object o) {
        Comparable<E> element = (Comparable<E>)o;

        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            E current = (E)elements[mid];

            int comparison = element.compareTo(current);
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
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
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

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
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

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
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        int i = 0;

        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }

        return modified;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Object[] newElements = new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
        capacity = newCapacity;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}