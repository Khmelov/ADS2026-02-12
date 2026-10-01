package by.it.group551003.parkhaniuk.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] data;
    private int size = 0;

    public MyPriorityQueue() {
        data = (E[]) new Comparable[10];
    }

    private void ensureCapacity() {
        if (size == data.length) {
            E[] newArr = (E[]) new Comparable[data.length * 2];
            System.arraycopy(data, 0, newArr, 0, size);
            data = newArr;
        }
    }

    private void siftUp(int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (data[idx].compareTo(data[parent]) >= 0) break;
            E tmp = data[idx];
            data[idx] = data[parent];
            data[parent] = tmp;
            idx = parent;
        }
    }

    private void siftDown(int idx) {
        while (true) {
            int left = idx * 2 + 1;
            int right = idx * 2 + 2;
            int smallest = idx;

            if (left < size && data[left].compareTo(data[smallest]) < 0)
                smallest = left;
            if (right < size && data[right].compareTo(data[smallest]) < 0)
                smallest = right;

            if (smallest == idx) break;

            E tmp = data[idx];
            data[idx] = data[smallest];
            data[smallest] = tmp;

            idx = smallest;
        }
    }

    private void heapify() {
        for (int i = (size - 2) / 2; i >= 0; i--) {
            siftDown(i);
        }
    }

    private boolean bulkRemove(Collection<?> c, boolean removeMatching) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            boolean inC = c.contains(data[i]);
            if (inC != removeMatching) {
                data[newSize++] = data[i];
            }
        }
        boolean changed = newSize != size;
        for (int i = newSize; i < size; i++)
            data[i] = null;
        size = newSize;
        if (changed) heapify();
        return changed;
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            data[i] = null;
        } else {
            E moved = data[s];
            data[s] = null;
            data[i] = moved;
            siftDown(i);
            if (data[i] == moved) siftUp(i);
        }
    }

    // ============================
    // REQUIRED METHODS
    // ============================

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        sb.append(data[0]);
        for (int i = 1; i < size; i++) {
            sb.append(", ").append(data[i]);
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
        for (int i = 0; i < size; i++)
            data[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        data[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        E root = data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;

        if (size > 0) siftDown(0);

        return root;
    }

    @Override
    public E peek() {
        return size == 0 ? null : data[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(data[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c)
            if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            offer(x);
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
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(data[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
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
                if (cursor >= size) throw new NoSuchElementException();
                return data[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size, Object[].class);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(data, size, a.getClass());
        }
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }
}