package by.it.group510901.gulchenko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements = (E[]) new Object[8];
    private int size = 0;

    private int compare(E first, E second) {
        return ((Comparable<? super E>) first).compareTo(second);
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

    private void siftUp(int index) {
        E element = elements[index];

        while (index > 0) {
            int parent = (index - 1) / 2;

            if (compare(element, elements[parent]) >= 0) {
                break;
            }

            elements[index] = elements[parent];
            index = parent;
        }

        elements[index] = element;
    }

    private void siftDown(int index) {
        E element = elements[index];

        while (index < size / 2) {
            int child = index * 2 + 1;
            int right = child + 1;

            if (right < size
                    && compare(elements[child], elements[right]) > 0) {
                child = right;
            }

            if (compare(element, elements[child]) <= 0) {
                break;
            }

            elements[index] = elements[child];
            index = child;
        }

        elements[index] = element;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
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
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        Comparable<? super E> comparable =
                (Comparable<? super E>) element;

        ensureCapacity();

        elements[size] = element;
        siftUp(size);
        size++;

        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E removed = elements[0];
        E last = elements[--size];
        elements[size] = null;

        if (size > 0) {
            elements[0] = last;
            siftDown(0);
        }

        return removed;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }

        boolean changed = false;

        for (E element : c) {
            add(element);
            changed = true;
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

        if (writeIndex == oldSize) {
            return false;
        }

        for (int i = writeIndex; i < oldSize; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        heapify();

        return true;
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

        if (writeIndex == oldSize) {
            return false;
        }

        for (int i = writeIndex; i < oldSize; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        heapify();

        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
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