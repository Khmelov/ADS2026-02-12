package by.it.group551001.Barusevich.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap = (E[]) new Object[11];
    private int size = 0;

    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;
            E[] newHeap = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) newHeap[i] = heap[i];
            heap = newHeap;
        }
    }

    private int compare(E e1, E e2) {
        return ((Comparable<? super E>) e1).compareTo(e2);
    }

    private void siftUp(int k) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            if (compare(heap[k], heap[parent]) >= 0) break;
            E tmp = heap[k]; heap[k] = heap[parent]; heap[parent] = tmp;
            k = parent;
        }
    }

    private void siftDown(int k) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            int right = child + 1;
            if (right < size && compare(heap[right], heap[child]) < 0)
                child = right;
            if (compare(heap[k], heap[child]) <= 0) break;
            E tmp = heap[k]; heap[k] = heap[child]; heap[child] = tmp;
            k = child;
        }
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E result = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        E[] newHeap = (E[]) new Object[heap.length];
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        heap = newHeap;
        size = newSize;
        heapify();
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        E[] newHeap = (E[]) new Object[heap.length];
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        heap = newHeap;
        size = newSize;
        heapify();
        return modified;
    }

    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { Object[] arr = new Object[size]; for(int i=0;i<size;i++) arr[i]=heap[i]; return arr; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}