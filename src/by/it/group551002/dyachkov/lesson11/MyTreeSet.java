package by.it.group551002.dyachkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements;

    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void grow() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        this.elements = newElements;
    }

    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> comparableKey = (Comparable<? super E>) key;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<? super E> midVal = (Comparable<? super E>) elements[mid];
            int cmp = comparableKey.compareTo((E) midVal);

            if (cmp > 0) {
                low = mid + 1;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /// ////////////////////////////////////////////////////////////
    /// ///               Обязательные                       ///////
    /// ////////////////////////////////////////////////////////////

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
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        int index = binarySearch(e);

        if (index >= 0) {
            return false;
        }

        int insertionIndex = -(index + 1);

        if (size == elements.length) {
            grow();
        }

        for (int i = size; i > insertionIndex; i--) {
            elements[i] = elements[i - 1];
        }

        elements[insertionIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || size == 0) {
            return false;
        }

        int index = binarySearch(o);

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
        if (o == null || size == 0) {
            return false;
        }
        return binarySearch(o) >= 0;
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
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (!modified) {
            return false;
        }

        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (!modified) {
            return false;
        }

        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return true;
    }

    /// //////////////////////////////////////////////////////////////////////
    /// ///               Опциональные                                 ///////
    /// //////////////////////////////////////////////////////////////////////

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