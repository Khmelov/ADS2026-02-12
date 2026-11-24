package by.it.group551003.kalvinkovskaya.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void resize() {
        if (size == elements.length) {
            int newCapacity = elements.length + ((elements.length < 64) ? (elements.length + 2) : (elements.length / 2));
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            if (compare(x, elements[parent]) >= 0) {
                break;
            }
            elements[k] = elements[parent];
            k = parent;
        }
        elements[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size / 2;
        while (k < half) {
            int child = (k * 2) + 1;
            E c = elements[child];
            int right = child + 1;
            if (right < size && compare(c, elements[right]) > 0) {
                c = elements[child = right];
            }
            if (compare(x, c) <= 0) {
                break;
            }
            elements[k] = c;
            k = child;
        }
        elements[k] = x;
    }

    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, elements[i]);
        }
    }

    private void removeAt(int i) {
        size--;
        if (i == size) {
            elements[i] = null;
        } else {
            E moved = elements[size];
            elements[size] = null;
            siftDown(i, moved);
            if (elements[i] == moved) {
                siftUp(i, moved);
            }
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
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String result = "[";
        result += elements[0];
        for (int i = 1; i < size; i++) {
            result += ", " + elements[i];
        }
        result += "]";
        return result;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("PriorityQueue does not permit null elements");
        }
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("PriorityQueue does not permit null elements");
        }
        resize();
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) {
            throw new RuntimeException("Queue is empty");
        }
        return x;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elements[0];
        E x = elements[size - 1];
        elements[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new RuntimeException("Queue is empty");
        }
        return x;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();
        boolean modified = false;
        int writeIdx = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[writeIdx] = elements[i];
                writeIdx++;
            } else {
                modified = true;
            }
        }

        for (int i = writeIdx; i < size; i++) {
            elements[i] = null;
        }

        if (modified) {
            size = writeIdx;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int writeIdx = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[writeIdx] = elements[i];
                writeIdx++;
            } else {
                modified = true;
            }
        }

        for (int i = writeIdx; i < size; i++) {
            elements[i] = null;
        }

        if (modified) {
            size = writeIdx;
            heapify();
        }
        return modified;
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
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Object[] res = new Object[size];
            for (int i = 0; i < size; i++) {
                res[i] = elements[i];
            }
            return (T[]) res;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new RuntimeException("No more elements");
                }
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}