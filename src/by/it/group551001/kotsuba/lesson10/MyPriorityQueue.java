package by.it.group551001.kotsuba.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] array;
    private int size;

    public MyPriorityQueue() {
        array = (E[]) new Comparable[11];
        size = 0;
    }

    private void resize() {
        E[] newArray = (E[]) new Comparable[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    private void siftUp(int k,E x){
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) / 2;
            E e = array[parent];
            if (key.compareTo(e) >= 0)
                break;
            array[k] = e;
            k = parent;
        }
        array[k] = (E) key;
    }

    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size / 2;
        while (k < half) {
            int child = (2 * k) + 1;
            E c = array[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo(array[right]) > 0) {
                child = right;
                c = array[child];
            }
            if (key.compareTo(c) <= 0)
                break;
            array[k] = c;
            k = child;
        }
        array[k] = (E) key;
    }

    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, array[i]);
        }
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
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
        if (size >= array.length) {
            resize();
        }
        if (size == 0) {
            array[0] = element;
            size = 1;
        } else {
            siftUp(size++, element);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = array[0];
        E x = array[s];
        array[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : array[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return array[0];
    }

    @Override
    public E remove() {
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) return true;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) return true;
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
        if (c == null) throw new NullPointerException();
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
        int i = 0;
        for (int j = 0; j < size; j++) {
            if (!c.contains(array[j])) {
                array[i++] = array[j];
            }
        }
        if (i != size) {
            for (int k = i; k < size; k++) {
                array[k] = null;
            }
            size = i;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int i = 0;
        for (int j = 0; j < size; j++) {
            if (c.contains(array[j])) {
                array[i++] = array[j];
            }
        }
        if (i != size) {
            for (int k = i; k < size; k++) {
                array[k] = null;
            }
            size = i;
            heapify();
            return true;
        }
        return false;
    }

    @Override public boolean remove(Object o) { return false; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> iterator() { return null; }
}

