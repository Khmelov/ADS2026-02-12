package by.it.group510902.karcheuski.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }


    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
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
        return offer(element);
    }

    @Override
    public E remove() {
        E value = poll();
        if (value == null) throw new NoSuchElementException();
        return value;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(heap[i], element)) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (size == heap.length) ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E top = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return top;
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
        for (Object o : c) {
            if (!contains(o)) return false;
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
        return filter(e -> !c.contains(e));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(e -> c.contains(e));
    }

    private boolean filter(Predicate<E> predicate) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        boolean modified = newSize != size;

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;

        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @FunctionalInterface
    private interface Predicate<T> {
        boolean test(T t);
    }


    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) smallest = left;
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) smallest = right;

            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Comparable[newCapacity];
        for (int i = 0; i < size; i++) newHeap[i] = heap[i];
        heap = newHeap;
    }

    private boolean removeOne(Object o) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(heap[i], o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int index) {
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (index < size) {
            siftDown(index);
            siftUp(index);
        }
    }


    @Override public Iterator<E> iterator()                      { return null; }
    @Override public Object[] toArray()                          { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a)                      { return a; }
    @Override public boolean remove(Object o)                    {return removeOne(o);}
}
