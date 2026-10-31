package by.it.group551002.sevkovich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] queue;
    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.queue = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(queue, 0, newArray, 0, size);
        queue = newArray;
    }

    @SuppressWarnings("unchecked")
    private int compareTo(E x, E y) {
        return ((Comparable<? super E>) x).compareTo(y);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    public boolean add(E element) {
        return offer(element);
    }

    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        if (size >= queue.length) {
            grow();
        }
        siftUp(size, element);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = (E) queue[parent];
            if (compareTo(x, e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    public E peek() {
        return size == 0 ? null : (E) queue[0];
    }

    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) queue[0];
    }

    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = (E) queue[child];
            int right = child + 1;
            if (right < size && compareTo(c, (E) queue[right]) > 0) {
                c = (E) queue[child = right];
            }
            if (compareTo(x, c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    private void heapify() {
        for (int i = (size >> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    public boolean contains(Object element) {
        if (element == null) return false;
        for (int i = 0; i < size; i++) {
            if (element.equals(queue[i])) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains((E) e)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                queue[w++] = queue[i];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) {
                queue[i] = null;
            }
            size = w;
            heapify();
        }
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                queue[w++] = queue[i];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) {
                queue[i] = null;
            }
            size = w;
            heapify();
        }
        return modified;
    }

    // Optional
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
}