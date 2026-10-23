package by.it.group551004.pedko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private final int STD_SIZE = 8;
    private E[] heap = (E[]) new Object[STD_SIZE];
    private int size = 0;

    private void grow() {
        E[] newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((Comparable<E>)heap[index]).compareTo(heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && ((Comparable<E>)heap[left]).compareTo(heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && ((Comparable<E>)heap[right]).compareTo(heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
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
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
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

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
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
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0);
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
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                return true;
            }
        }
        return false;
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
        E[] tmp = (E[]) new Object[size];
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                modified = true;
            } else {
                tmp[newSize++] = heap[i];
            }
        }
        if (modified) {
            for (int i = 0; i < newSize; i++) {
                heap[i] = tmp[i];
            }
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        E[] tmp = (E[]) new Object[size];
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                tmp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = 0; i < newSize; i++) {
                heap[i] = tmp[i];
            }
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean remove(Object o) { return false; }
}