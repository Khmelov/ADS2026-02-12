package by.it.group510901.halavachou.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 11;
    private E[] queue;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.queue = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        this.queue = (E[]) new Object[initialCapacity];
        this.size = 0;
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
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size >= queue.length) {
            grow();
        }
        siftUp(size, element);
        size++;
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
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
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
        boolean modified = false;
        for (Object e : c) {
            while (contains(e)) {
                remove(e);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(queue[i])) {
                removeAt(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(queue[i] == this ? "(this Collection)" : queue[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCap = queue.length;
        int newCap = oldCap + (oldCap < 64 ? oldCap + 2 : oldCap / 2);
        E[] newQueue = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
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

    @SuppressWarnings("unchecked")
    private int compare(E k1, E k2) {
        return ((Comparable<? super E>) k1).compareTo(k2);
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            E e = queue[parent];
            if (compare(x, e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size / 2;
        while (k < half) {
            int child = 2 * k + 1;
            E c = queue[child];
            int right = child + 1;
            if (right < size && compare(c, queue[right]) > 0) {
                child = right;
                c = queue[child];
            }
            if (compare(x, c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            queue[i] = null;
        } else {
            E moved = queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}