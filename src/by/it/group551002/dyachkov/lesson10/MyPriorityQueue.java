package by.it.group551002.dyachkov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] heap;

    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    public MyPriorityQueue() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void grow() {
        Object[] newHeap = new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        this.heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index, E element) {
        Comparable<? super E> key = (Comparable<? super E>) element;
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = (E) heap[parentIndex];

            if (key.compareTo(parent) >= 0) {
                break;
            }

            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index, E element) {
        Comparable<? super E> key = (Comparable<? super E>) element;
        int half = size / 2;

        while (index < half) {
            int childIndex = (index * 2) + 1;
            Object child = heap[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && ((Comparable<? super E>) child).compareTo((E) heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = heap[childIndex];
            }

            if (key.compareTo((E) child) <= 0) {
                break;
            }

            heap[index] = child;
            index = childIndex;
        }
        heap[index] = key;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, (E) heap[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void removeAt(int index) {
        if (index == size - 1) {
            heap[--size] = null;
        } else {
            E moved = (E) heap[--size];
            heap[size] = null;

            siftDown(index, moved);
            if (heap[index] == moved) {
                siftUp(index, moved);
            }
        }
    }

    /////////////////////////////////////////////////////////////////
    //////               Обязательные                         ///////
    /////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
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
        if (element == null) throw new NullPointerException();
        if (size == heap.length) grow();

        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;

        E result = (E) heap[0];
        E lastElement = (E) heap[--size];
        heap[size] = null;

        if (size > 0) {
            siftDown(0, lastElement);
        }

        return result;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (size == 0) return null;
        return (E) heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return true;
        }
        return false;
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
            if (offer(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;

        heapify();

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;

        heapify();

        return true;
    }

    /// //////////////////////////////////////////////////////////////////////
    /// ///               Опциональные                                 ///////
    /// //////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }
}
