package by.it.group510902.manko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private E[] elements = (E[]) new Object[10];
    private int size = 0, capacity = 10;

    private void ensureCapacity(int newSize) {
        if (newSize <= capacity) {
            return;
        }
        while (newSize > capacity) {
            capacity = capacity * 7 / 4 + 1;
        }
        E[] newElements = (E[]) new Object[capacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // Binary search: returns index of element if found, otherwise
    // returns -(insertionPoint + 1) (same convention as Arrays.binarySearch).
    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("unchecked")
            int cmp = ((Comparable<? super E>) o).compareTo(elements[mid]);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(elements[i]);
        }
        result.append("]");
        return result.toString();
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
        int index = binarySearch(e);
        if (index >= 0) {
            return false; // already present
        }
        int insertionPoint = -(index + 1);
        ensureCapacity(size + 1);
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1,
                size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false;
        }
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
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
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (c.contains(elements[read])) {
                elements[write++] = elements[read];
            } else {
                modified = true;
            }
        }
        for (int i = write; i < size; i++) {
            elements[i] = null;
        }
        size = write;
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Set             ///////
    /////////////////////////////////////////////////////////////////////////

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
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(elements[lastRet]);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}