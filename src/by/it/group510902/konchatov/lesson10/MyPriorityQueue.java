package by.it.group510902.konchatov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] heap;
    private int size;
    public MyPriorityQueue() {
        heap = (E[]) new Object[10];
        size = 0;
    }

    private void incr() {
        if (size == heap.length) {
            E[] q = (E[]) new Object[heap.length * 2];
            for (int i = 0; i < size; i++) q[i] = heap[i];
            heap = q;
        }
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(heap[i], heap[parent]) >= 0) break;
            swap(i, parent);
            i = parent;
        }
    }
    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int min= i;
            if (left < size && compare(heap[left], heap[min]) < 0) min = left;
            if (right < size && compare(heap[right], heap[min]) < 0) min = right;
            if ( min== i) break;
            swap(i, min);
            i = min;
        }
    }
    private void swap(int a, int b) {
        E tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }
    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) return i;
        }
        return -1;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
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
    public boolean add(E element) {
        incr();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E top = heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return top;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        return remove();
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return heap[0];
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
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean q = false;
        for (E e : c) { 
            add(e);
            q = true; 
        }
        return q;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        boolean q = newSize < size;
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        heapify();
        return q;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        boolean q = newSize < size;
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        heapify();
        return q;
    }
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }



    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}