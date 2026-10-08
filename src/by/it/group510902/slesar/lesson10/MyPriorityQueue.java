package by.it.group510902.slesar.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] queue = (E[]) new Object[11];
    private int size = 0;

    private void grow(int minCapacity) {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        queue = Arrays.copyOf(queue, newCapacity);
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0) break;
            queue[k] = (E) e;
            k = parent;
        }
        queue[k] = x;
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
                child = right;
                c = queue[child];
            }
            if (key.compareTo((E) c) <= 0) break;
            queue[k] = (E) c;
            k = child;
        }
        queue[k] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, queue[i]);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
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
        for (int i = 0; i < size; i++) queue[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        int i = size;
        if (i >= queue.length) grow(i + 1);
        size = i + 1;
        if (i == 0) queue[0] = element;
        else siftUp(i, element);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = queue[0];
        E x = queue[s];
        queue[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x != null) return x;
        throw new NoSuchElementException();
    }

    @Override
    public E peek() {
        return (size == 0) ? null : queue[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x != null) return x;
        throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
            if (!c.contains(queue[i])) {
                queue[newSize++] = queue[i];
            }
        }
        if (newSize == size) return false;
        for (int i = newSize; i < size; i++) queue[i] = null;
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                queue[newSize++] = queue[i];
            }
        }
        if (newSize == size) return false;
        for (int i = newSize; i < size; i++) queue[i] = null;
        size = newSize;
        heapify();
        return true;
    }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}