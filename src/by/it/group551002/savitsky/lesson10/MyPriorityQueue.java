package by.it.group551002.savitsky.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] data;
    private int size;

    MyPriorityQueue() {
        data = (E[]) new Object[1000];
        size = 0;
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
        if (e == null) {
            throw new NullPointerException();
        }

        if (size == data.length) {
            grow();
        }

        data[size] = e;
        siftUp(size);
        size++;

        return true;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = data[0];
        size--;
        data[0] = data[size];
        data[size] = null;
        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return poll();
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return data[0];
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }

        return data[0];
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            result = result + data[i];

            if (i < size - 1) {
                result = result + ", ";
            }
        }

        return result + "]";
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                removeAt(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
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

        boolean changed = false;

        for (E e : c) {
            offer(e);
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, true);
    }

    private void grow() {
        E[] bigger = (E[]) new Object[data.length * 2];

        if (size >= 0) System.arraycopy(data, 0, bigger, 0, size);

        data = bigger;
    }

    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;

            if (compare(data[i], data[parent]) >= 0) {
                break;
            }

            swap(i, parent);
            i = parent;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && compare(data[left], data[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && compare(data[right], data[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == i) {
                break;
            }

            swap(i, smallest);
            i = smallest;
        }
    }

    private void removeAt(int i) {
        size--;
        if (i == size) {
            data[size] = null;
            return;
        }

        E last = data[size];
        data[size] = null;
        data[i] = last;
        siftDown(i);
        if (data[i] == last) {
            siftUp(i);
        }
    }

    private boolean filter(Collection<?> c, boolean keepIfContain) {
        if (c == null) {
            throw new NullPointerException();
        }

        int newSize = 0;
        for (int i = 0; i < size; i++) {

            if (c.contains(data[i]) == keepIfContain) {
                data[newSize] = data[i];
                newSize++;
            }
        }

        boolean changed = newSize != size;

        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }
        size = newSize;

        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }

        return changed;
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
        return null;
    }
}