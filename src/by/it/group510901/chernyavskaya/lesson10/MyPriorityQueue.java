package by.it.group510901.chernyavskaya.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

// сделать очередь с приоритетом на двоичной куче

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap = (E[]) new Comparable[16];
    private int size = 0;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
            heap[i] = null;
        }
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        E key = heap[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E parentVal = heap[parent];
            if (key.compareTo(parentVal) >= 0) {
                break;
            }
            heap[index] = parentVal;
            index = parent;
        }
        heap[index] = key;
    }

    private void siftDown(int index) {
        E key = heap[index];
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && c.compareTo(heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            heap[index] = c;
            index = child;
        }
        heap[index] = key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        heap[size] = e;
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
        int lastIndex = size - 1;
        E last = heap[lastIndex];
        heap[lastIndex] = null;
        size--;
        if (size > 0) {
            heap[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                heap[i] = heap[size - 1];
                heap[size - 1] = null;
                size--;
                heapify();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) return false;
        int writeIndex = 0;
        int oldSize = size;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            }
        }
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }
        if (writeIndex != oldSize) {
            size = writeIndex;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        int writeIndex = 0;
        int oldSize = size;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            }
        }
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }
        if (writeIndex != oldSize) {
            size = writeIndex;
            heapify();
            return true;
        }
        return false;
    }

    @Override public E remove() { return poll(); }
    @Override public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            }
        }
        return modified;
    }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}