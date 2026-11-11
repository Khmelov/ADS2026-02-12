package by.it.group510902.sorokoletov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements java.util.Queue<E> {


    private E[] queue = (E[]) new Object[11];
    private int size = 0;

    public MyPriorityQueue() {
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
        ensureCapacity();
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public E peek() {
        return size == 0 ? null : queue[0];
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int lastIdx = --size;
        E result = queue[0];
        E lastElem = queue[lastIdx];
        queue[lastIdx] = null;

        if (lastIdx != 0) {
            siftDown(0, lastElem);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
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
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                queue[newSize++] = queue[i];
            }
        }
        if (newSize == size) {
            return false;
        }
        for (int i = newSize; i < size; i++) {
            queue[i] = null;
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
            if (c.contains(queue[i])) {
                queue[newSize++] = queue[i];
            }
        }
        if (newSize == size) {
            return false;
        }
        for (int i = newSize; i < size; i++) {
            queue[i] = null;
        }
        size = newSize;
        heapify();
        return true;
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
        return sb.append("]").toString();
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, queue[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E parentElem = queue[parent];
            if (key.compareTo(parentElem) >= 0) {
                break;
            }
            queue[k] = parentElem;
            k = parent;
        }
        queue[k] = (E) key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E childElem = queue[child];
            int right = child + 1;

            if (right < size && ((Comparable<? super E>) childElem).compareTo(queue[right]) > 0) {
                child = right;
                childElem = queue[right];
            }
            if (key.compareTo(childElem) <= 0) {
                break;
            }
            queue[k] = childElem;
            k = child;
        }
        queue[k] = (E) key;
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


    private void ensureCapacity() {
        if (size == queue.length) {
            int newCap = queue.length * 2;
            E[] newQueue = (E[]) new Object[newCap];
            for (int i = 0; i < size; i++) {
                newQueue[i] = queue[i];
            }
            queue = newQueue;
        }
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

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
        return a;
    }
}