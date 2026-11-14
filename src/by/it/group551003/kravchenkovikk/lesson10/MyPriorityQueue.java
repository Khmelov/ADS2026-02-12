package by.it.group551003.kravchenkovikk.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.capacity = 10;
        this.heap = (E[]) new Object[capacity];
        this.size = 0;
    }

    private int getParent(int i) {
        return (i - 1) / 2;
    }
    private int getLeftChild(int i) {
        return 2 * i + 1;
    }
    private int getRightChild(int i) {
        return 2 * i + 2;
    }

    private void grow() {
        capacity *= 2;
        E[] newHeap = (E[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    private void siftDown(int i) {
        while (getLeftChild(i) < size) {
            int right = getRightChild(i);
            int smallest = getLeftChild(i);
            if (right < size && ((Comparable<E>) heap[right]).compareTo(heap[smallest]) < 0) {
                smallest = right;
            }
            if (((Comparable<E>) heap[i]).compareTo(heap[smallest]) <= 0) {
                break;
            }
            E temp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = temp;
            i = smallest;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i == size - 1) {
                sb.append("]");
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override public boolean add(E e) {
        if (size == capacity) {
            grow();
        }
        int i = size;
        heap[size++] = e;
        while (i > 0 && ((Comparable<E>) heap[getParent(i)]).compareTo(heap[i]) > 0) {
            E temp = heap[i];
            heap[i] = heap[getParent(i)];
            heap[getParent(i)] = temp;
            i = getParent(i);
        }
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            return null;
        }
        E removed = heap[0];
        if (size == 1) {
            heap[0] = null;
            size--;
            return removed;
        } else {
            heap[0] = heap[--size];
            heap[size] = null;
            siftDown(0);
        }
        return removed;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E removed = heap[0];
        if (size == 1) {
            heap[0] = null;
            size--;
            return removed;
        } else {
            heap[0] = heap[--size];
            heap[size] = null;
            siftDown(0);
        }
        return removed;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E obj: c) {
            add(obj);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (size == 0) {
            return false;
        }
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (!c.contains(heap[read])) {
                heap[write++] = heap[read];
            }
        }
        if (write == size) {
            return false;
        }
        int oldSize = size;
        size = write;
        for (int i = size; i < oldSize; i++) {
            heap[i] = null;
        }
        for (int i = getParent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (size == 0) {
            return false;
        }
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (c.contains(heap[read])) {
                heap[write++] = heap[read];
            }
        }
        int oldSize = size;
        size = write;
        for (int i = size; i < oldSize; i++) {
            heap[i] = null;
        }
        for (int i = getParent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
        return true;
    }

    @Override public boolean remove(Object o) {return false;}
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return a; }
}
