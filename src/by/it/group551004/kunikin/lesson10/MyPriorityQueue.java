package by.it.group551004.kunikin.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap = (E[]) new Object[11];
    private int size = 0;

    private void grow(int minCapacity) {
        int oldCapacity = heap.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = heap[parent];
            if (key.compareTo((E) e) >= 0)
                break;
            heap[k] = (E) e;
            k = parent;
        }
        heap[k] = (E) key;
    }

    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) heap[right]) > 0)
                c = heap[child = right];
            if (key.compareTo((E) c) <= 0)
                break;
            heap[k] = (E) c;
            k = child;
        }
        heap[k] = (E) key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
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
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public boolean contains(Object element) {
        if (element != null) {
            for (int i = 0; i < size; i++) {
                if (element.equals(heap[i])) return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        int i = size;
        if (i >= heap.length) grow(i + 1);
        siftUp(i, element);
        size = i + 1;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = heap[0];
        E x = heap[s];
        heap[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
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
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        if (newSize == size) return false;
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        if (newSize == size) return false;
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        heapify();
        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные методы (заглушки)              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}