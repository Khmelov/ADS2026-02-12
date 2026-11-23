package by.it.group510901.gulchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements = (E[]) new Object[16];
    private int size = 0;

    private int compare(E first, E second) {
        return ((Comparable<? super E>) first).compareTo(second);
    }

    private int findIndex(E element) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int comparison = compare(element, elements[middle]);

            if (comparison < 0) {
                right = middle - 1;
            } else if (comparison > 0) {
                left = middle + 1;
            } else {
                return middle;
            }
        }

        return -(left + 1);
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

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += elements[i];
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
        if (element == null) {
            throw new NullPointerException();
        }

        compare(element, element);

        int index = findIndex(element);

        if (index >= 0) {
            return false;
        }

        index = -index - 1;
        ensureCapacity();

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        E element = (E) o;
        Comparable<? super E> comparable =
                (Comparable<? super E>) element;

        int index = findIndex(element);

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
        if (o == null) {
            throw new NullPointerException();
        }

        E element = (E) o;
        Comparable<? super E> comparable =
                (Comparable<? super E>) element;

        return findIndex(element) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == this) {
            return true;
        }

        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == this) {
            return false;
        }

        boolean changed = false;

        for (E element : c) {
            if (add(element)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            boolean changed = size > 0;
            clear();
            return changed;
        }

        int oldSize = size;
        int writeIndex = 0;

        for (int i = 0; i < oldSize; i++) {
            if (!c.contains(elements[i])) {
                elements[writeIndex++] = elements[i];
            }
        }

        for (int i = writeIndex; i < oldSize; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        return size != oldSize;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            return false;
        }

        int oldSize = size;
        int writeIndex = 0;

        for (int i = 0; i < oldSize; i++) {
            if (c.contains(elements[i])) {
                elements[writeIndex++] = elements[i];
            }
        }

        for (int i = writeIndex; i < oldSize; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        return size != oldSize;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return elements[index++];
            }
        };
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