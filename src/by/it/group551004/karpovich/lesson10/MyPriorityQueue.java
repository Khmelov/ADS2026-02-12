package by.it.group551004.karpovich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int i) {
        E item = heap[i];
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(item, heap[parent]) >= 0) {
                break;
            }
            heap[i] = heap[parent];
            i = parent;
        }
        heap[i] = item;
    }

    private void siftDown(int i) {
        E item = heap[i];
        while (true) {
            int left = 2 * i + 1;
            if (left >= size) {
                break;
            }
            int right = left + 1;
            int smallest = left;
            if (right < size && compare(heap[right], heap[left]) < 0) {
                smallest = right;
            }
            if (compare(item, heap[smallest]) <= 0) {
                break;
            }
            heap[i] = heap[smallest];
            i = smallest;
        }
        heap[i] = item;
    }

    private void removeAt(int i) {
        size--;
        if (i == size) {
            heap[size] = null;
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

    private boolean bulkRemove(Collection<?> c, boolean removeWhenContained) {
        boolean[] dead = new boolean[size];
        boolean any = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) == removeWhenContained) {
                dead[i] = true;
                any = true;
            }
        }
        if (!any) {
            return false;
        }
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (!dead[i]) {
                heap[w++] = heap[i];
            }
        }
        for (int i = w; i < size; i++) {
            heap[i] = null;
        }
        size = w;
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += heap[i];
            if (i < size - 1) {
                result += ", ";
            }
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
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null;
            siftDown(0);
        } else {
            heap[0] = null;
        }
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
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

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
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return heap[cursor++];
            }
        };
    }
}