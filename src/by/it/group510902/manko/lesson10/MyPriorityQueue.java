package by.it.group510902.manko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

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

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(elements[index], elements[parent]) >= 0) {
                break;
            }
            E tmp = elements[index];
            elements[index] = elements[parent];
            elements[parent] = tmp;
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && compare(elements[left], elements[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && compare(elements[right], elements[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            E tmp = elements[index];
            elements[index] = elements[smallest];
            elements[smallest] = tmp;
            index = smallest;
        }
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
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
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E value = poll();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
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
        ensureCapacity(size + 1);
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
        E result = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public E element() {
        E value = peek();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
            offer(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (size == 0 || c.isEmpty()) {
            return false;
        }
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }
        if (newSize == size) {
            return false;
        }
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (size == 0) {
            return false;
        }
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }
        if (newSize == size) {
            return false;
        }
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        heapify();
        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Queue           ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                size--;
                elements[i] = elements[size];
                elements[size] = null;
                if (i < size) {
                    siftDown(i);
                    siftUp(i);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[cursor++];
            }
        };
    }
}