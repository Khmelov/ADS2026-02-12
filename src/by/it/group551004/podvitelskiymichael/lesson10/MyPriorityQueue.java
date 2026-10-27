package by.it.group551004.podvitelskiymichael.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
    }

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

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E p = heap[parent];
            if (compare(x, p) >= 0) {
                break;
            }
            heap[k] = p;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, E x, int n) {
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < n && compare(c, heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (compare(x, c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i], size);
        }
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            siftDown(i, moved, size);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    private boolean removeMatching(Collection<?> c, boolean removeIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }
        int end = size;
        boolean[] doomed = new boolean[end];
        boolean any = false;
        for (int i = 0; i < end; i++) {
            if (c.contains(heap[i]) == removeIfContained) {
                doomed[i] = true;
                any = true;
            }
        }
        if (!any) {
            return false;
        }
        int w = 0;
        for (int i = 0; i < end; i++) {
            if (!doomed[i]) {
                heap[w++] = heap[i];
            }
        }
        for (int i = w; i < end; i++) {
            heap[i] = null;
        }
        size = w;
        heapify();
        return true;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
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
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size >= heap.length) {
            grow();
        }
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        E last = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0, last, size);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
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
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeMatching(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeMatching(c, false);
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        removeAt(i);
        return true;
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
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}