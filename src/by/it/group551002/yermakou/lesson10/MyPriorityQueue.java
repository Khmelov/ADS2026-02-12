package by.it.group551002.yermakou.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private E[] queue;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    public MyPriorityQueue() {
        this.queue = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    private void grow(int minCapacity) {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        queue = Arrays.copyOf(queue, newCapacity);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        int i = size;
        if (i >= queue.length) {
            grow(i + 1);
        }
        size = i + 1;
        if (i == 0) {
            queue[0] = e;
        } else {
            siftUp(i, e);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = queue[0];
        E x = queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    @Override
    public E peek() {
        return (size == 0) ? null : queue[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
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
        if (c == null) {
            throw new NullPointerException();
        }
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                queue[w++] = queue[i];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                queue[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                queue[w++] = queue[i];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                queue[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, queue[i]);
        }
    }

    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) / 2 ;
            E e = queue[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = (E) key;
    }

    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = queue[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo(queue[right]) > 0) {
                child = right;
                c = queue[child];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = (E) key;
    }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
