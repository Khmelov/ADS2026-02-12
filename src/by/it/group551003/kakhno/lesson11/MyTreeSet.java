package by.it.group551003.kakhno.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROWTH_FACTOR = 2;

    private E[] elements;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.elements = (E[]) new Comparable[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException("Null elements are not allowed");

        if (size >= capacity)
            resize();

        int pos = binarySearch(element);

        if (pos >= 0)
            return false;

        int insertIndex = -(pos + 1);

        for (int i = size; i > insertIndex; i--)
            elements[i] = elements[i - 1];

        elements[insertIndex] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null)
            return false;

        try {
            @SuppressWarnings("unchecked")
            E e = (E) element;

            int index = binarySearch(e);

            if (index < 0)
                return false;

            for (int i = index; i < size - 1; i++)
                elements[i] = elements[i + 1];
            elements[size - 1] = null;
            size--;

            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean contains(Object element) {
        if (element == null)
            return false;

        try {
            @SuppressWarnings("unchecked")
            E e = (E) element;

            return binarySearch(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++)
            array[i] = elements[i];
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            a = newArray;
        }

        for (int i = 0; i < size; i++)
            a[i] = (T) elements[i];

        if (a.length > size)
            a[size] = null;

        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        for (Object element : c)
            if (!contains(element))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        for (E element : c)
            if (add(element))
                modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (!c.contains(element)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        for (Object element : c)
            if (remove(element))
                modified = true;
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (i > 0)
                sb.append(", ");
            E element = elements[i];
            sb.append(element == this ? "(this Collection)" : element);
        }

        sb.append(']');
        return sb.toString();
    }

    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // Беззнаковый сдвиг для избежания переполнения
            E midVal = elements[mid];

            int cmp = midVal.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }

        return -(low + 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * GROWTH_FACTOR;
        E[] newElements = (E[]) new Comparable[newCapacity];

        for (int i = 0; i < size; i++)
            newElements[i] = elements[i];

        elements = newElements;
        capacity = newCapacity;
    }

    private class TreeSetIterator implements Iterator<E> {
        private int currentIndex;
        private boolean canRemove;

        public TreeSetIterator() {
            this.currentIndex = 0;
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            canRemove = true;
            return elements[currentIndex++];
        }

        @Override
        public void remove() {
            if (!canRemove)
                throw new IllegalStateException();

            MyTreeSet.this.remove(elements[currentIndex - 1]);
            currentIndex--;
            canRemove = false;
        }
    }
}
