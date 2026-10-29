package by.it.group510902.shust.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] queue = new Object[11];
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
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
    public boolean contains(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        int i = size;
        if (i >= queue.length) grow();
        size = i + 1;
        if (i == 0) {
            queue[0] = element;
        } else {
            siftUp(i, element);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) queue[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

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
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int w = 0;
        boolean modified = false;
        for (int r = 0; r < size; r++) {
            if (!c.contains(queue[r])) {
                queue[w++] = queue[r];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) queue[i] = null;
            size = w;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int w = 0;
        boolean modified = false;
        for (int r = 0; r < size; r++) {
            if (c.contains(queue[r])) {
                queue[w++] = queue[r];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) queue[i] = null;
            size = w;
            heapify();
        }
        return modified;
    }

    private void grow() {
        Object[] newQueue = new Object[queue.length * 2];
        System.arraycopy(queue, 0, newQueue, 0, queue.length);
        queue = newQueue;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0) break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0) {
                c = queue[child = right];
            }
            if (key.compareTo((E) c) <= 0) break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}