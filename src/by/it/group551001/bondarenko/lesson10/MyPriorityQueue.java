package by.it.group551001.bondarenko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] array;
    private int size = 0;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        array = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == array.length) {
            int newCapacity = array.length * 2;
            E[] newArray = (E[]) new Comparable[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
    }

    private void siftUp(int i) {
        E key = array[i];
        while (i > 0) {
            int parent = (i - 1) >>> 1;
            E e = array[parent];
            if (((Comparable<? super E>) key).compareTo(e) >= 0) {
                break;
            }
            array[i] = e;
            i = parent;
        }
        array[i] = key;
    }

    private void siftDown(int i) {
        E key = array[i];
        int half = size >>> 1;
        while (i < half) {
            int child = (i << 1) + 1;
            E c = array[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo(array[right]) > 0) {
                child = right;
                c = array[child];
            }
            if (((Comparable<? super E>) key).compareTo(c) <= 0) {
                break;
            }
            array[i] = c;
            i = child;
        }
        array[i] = key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
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
        array[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = array[0];
        size--;
        E last = array[size];
        array[size] = null;
        if (size > 0) {
            array[0] = last;
            siftDown(0);
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
        return (size == 0) ? null : array[0];
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
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                return true;
            }
        }
        return false;
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
            if (offer(e)) {
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
        int writeIndex = 0;
        boolean modified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(array[readIndex])) {
                array[writeIndex++] = array[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                array[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        boolean modified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(array[readIndex])) {
                array[writeIndex++] = array[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                array[i] = null;
            }
            size = writeIndex;
            heapify();
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
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Заглушки для остатка интерфейса Queue<E>          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}