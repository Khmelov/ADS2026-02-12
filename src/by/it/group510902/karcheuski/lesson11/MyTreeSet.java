package by.it.group510902.karcheuski.lesson11;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] elements;
    private int size;

    private Comparator<? super E> comparator;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }


    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((E) a, (E) b);
        }
        return ((Comparable<? super E>) a).compareTo((E) b);
    }


    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], o);

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


    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
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
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return binarySearch(o) >= 0;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("MyTreeSet does not permit null elements");
        }

        int index = binarySearch(e);

        if (index >= 0) {
            return false;
        }

        int insertIndex = -(index + 1);

        ensureCapacity(size + 1);

        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = e;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index;
        try {
            index = binarySearch(o);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }

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
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
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
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(elements[i]);
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

        StringBuilder sb = new StringBuilder();
        sb.append("[");
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
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }


    private interface Comparator<T> extends java.util.Comparator<T> {
    }
}
