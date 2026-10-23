package by.it.group551001.romanovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private E[] elements = (E[]) new Object[DEFAULT_CAPACITY];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
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
        int insertAt = -index - 1;
        if (size == elements.length) {
            grow();
        }
        for (int i = size; i > insertAt; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertAt] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = binarySearch(element);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean contains(Object element) {
        return binarySearch(element) >= 0;
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
        return removeIf(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeIf(c, false);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private int binarySearch(Object element) {
        Comparable<Object> key = (Comparable<Object>) element;
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
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

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    private boolean removeIf(Collection<?> c, boolean removeContained) {
        int kept = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i]) != removeContained) {
                elements[kept++] = elements[i];
            }
        }
        if (kept == size) {
            return false;
        }
        for (int i = kept; i < size; i++) {
            elements[i] = null;
        }
        size = kept;
        return true;
    }

    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return elements[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) elements[i];
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }
}
