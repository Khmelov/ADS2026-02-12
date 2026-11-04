package by.it.group510902.yaskel.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
    }

    // вспомогательные методы

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = heap[i];
        }
        heap = bigger;
    }

    private void siftUp(int k) {
        E x = heap[k];
        while (k > 0) {
            int parent = (k - 1) / 2;
            if (compare(x, heap[parent]) >= 0) {
                break;
            }
            heap[k] = heap[parent];
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k) {
        E x = heap[k];
        int half = size / 2;
        while (k < half) {
            int child = 2 * k + 1;
            int right = child + 1;
            if (right < size && compare(heap[child], heap[right]) > 0) {
                child = right;
            }
            if (compare(x, heap[child]) <= 0) {
                break;
            }
            heap[k] = heap[child];
            k = child;
        }
        heap[k] = x;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void removeAt(int i) {
        size--;
        if (i == size) {
            heap[i] = null;
            return;
        }
        E moved = heap[size];
        heap[size] = null;
        heap[i] = moved;
        siftDown(i);
        if (heap[i] == moved) {
            siftUp(i);
        }
    }

    // обязательные методы

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += heap[i];
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
            heap[i] = null;
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
            if (o.equals(heap[i])) {
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
        if (size == heap.length) {
            grow();
        }
        heap[size] = element;
        size++;
        siftUp(size - 1);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        removeAt(0);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
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
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        return filter(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        return filter(c, true);
    }

    private boolean filter(Collection<?> c, boolean keepIfContained) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) == keepIfContained) {
                heap[j++] = heap[i];
            }
        }
        boolean changed = j != size;
        for (int i = j; i < size; i++) {
            heap[i] = null;
        }
        size = j;
        if (changed) {
            heapify();
        }
        return changed;
    }

    // остальные методы интерфейса

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

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
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return heap[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}