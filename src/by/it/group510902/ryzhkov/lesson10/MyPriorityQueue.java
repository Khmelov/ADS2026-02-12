package by.it.group510902.ryzhkov.lesson10;

import java.util.Collection;
import java.util.Objects;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = (E[]) new Object[10];
        size = 0;
    }

    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < size; i++) {
            builder.append(heap[i]);
            if (i < size - 1)
                builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    private void increaseCapacity() {
        E[] newHeap = (E[]) new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    private void siftUp(int index) {
        E key = heap[index];
        Comparable<E> comp = (Comparable<E>) key;
        while (index > 0) {
            int parent = (index - 1) / 2;
            E p = heap[parent];
            if (comp.compareTo(p) >= 0)
                break;
            heap[index] = p;
            index = parent;
        }
        heap[index] = key;
    }

    private void siftDown(int index) {
        E key = heap[index];
        Comparable<E> comp = (Comparable<E>) key;
        int half = size / 2;
        while (index < half) {
            int child = (index / 2) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<E>) c).compareTo(heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (comp.compareTo(c) <= 0)
                break;
            heap[index] = c;
            index = child;
        }
        heap[index] = key;
    }

    private void removeAt(int index) {
        size--;
        if (size == index) {
            heap[index] = null;
        } else {
            E moved = heap[size];
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    public boolean add(E element) {
        if (heap.length == size)
            increaseCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    public E remove() {
        E data = heap[0];
        removeAt(0);
        return data;
    }

    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i]))
                return true;
        }

        return false;
    }

    public boolean offer(E element) {
        return add(element);
    }

    public E poll() {
        return remove();
    }

    public E peek() {
        return heap[0];
    }

    public E element() {
        return peek();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e))
                return false;
        }

        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e))
                modified = true;
        }
        return modified;
    }

    public boolean removeAll(Collection<?> c) {
        int writeIndex = 0;
        boolean modified = false;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                heap[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        int writeIndex = 0;
        boolean modified = false;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                heap[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public java.util.Iterator<E> iterator() {
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

    @Override
    public boolean remove(Object o) {
        return false;
    }
}
