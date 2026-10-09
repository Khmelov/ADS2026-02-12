package by.it.group510901.chibisov.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] queue;
    private int size;
    private final Comparator<? super E> comparator;

    public MyPriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        queue = new Object[initialCapacity];
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E first, E second) {
        if (comparator != null) {
            return comparator.compare(first, second);
        }
        return ((Comparable<? super E>) first).compareTo(second);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > queue.length) {
            int oldCapacity = queue.length;
            int newCapacity = Math.max(oldCapacity + (oldCapacity >> 1) + 1, minCapacity);
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(queue, 0, newArray, 0, size);
            queue = newArray;
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) queue[index];
    }

    private void siftDown(int i) {
        E temp = elementAt(i);
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            int j = left;

            if (right < size && compare(elementAt(right), elementAt(left)) < 0) {
                j = right;
            }

            if (compare(temp, elementAt(j)) <= 0) break;

            queue[i] = queue[j];
            i = j;
        }
        queue[i] = temp;
    }

    private void siftUp(int i) {
        E temp = elementAt(i);
        while (i > 0) {
            int parent = (i - 1) / 2;

            if (compare(elementAt(parent), temp) > 0) {
                queue[i] = queue[parent];
            } else break;

            i = parent;
        }
        queue[i] = temp;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(queue[i]);
        }
        sb.append(']');
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
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        queue[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E poll() {
        if (isEmpty()) return null;
        E old = elementAt(0);
        queue[0] = queue[--size];
        queue[size] = null;
        siftDown(0);
        return old;
    }

    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public E peek() {
        return elementAt(0);
    }

    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException();
        return elementAt(0);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(queue[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        if (c.isEmpty()) return false;
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean keepIfContains) {
        if (c == null) throw new NullPointerException();
        int r = 0;
        for (;; r++) {
            if (r == size) return false;
            if (c.contains(queue[r]) != keepIfContains) break;
        }
        int w = r++;
        for (; r < size; r++) {
            if (c.contains(queue[r]) == keepIfContains) {
                queue[w++] = queue[r];
            }
        }
        for (int i = w; i < size; i++) {
            queue[i] = null;
        }
        size = w;
        heapify();
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        System.arraycopy(queue, 0, a, 0, size);
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            return elementAt(cursor++);
        }
    }

}
