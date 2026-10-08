package by.it.group551002.yorsh.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            @SuppressWarnings("unchecked")
            Comparable<E> current = (Comparable<E>) elements[i];
            if (current.compareTo(elements[parent]) < 0) {
                E temp = elements[i];
                elements[i] = elements[parent];
                elements[parent] = temp;
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;

            if (right < size) {
                @SuppressWarnings("unchecked")
                Comparable<E> rightElem = (Comparable<E>) elements[right];
                if (rightElem.compareTo(elements[left]) < 0) {
                    smallest = right;
                }
            }

            @SuppressWarnings("unchecked")
            Comparable<E> smallestElem = (Comparable<E>) elements[smallest];
            if (smallestElem.compareTo(elements[i]) < 0) {
                E temp = elements[i];
                elements[i] = elements[smallest];
                elements[smallest] = temp;
                i = smallest;
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
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
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        elements[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E res = poll();
        if (res == null) throw new java.util.NoSuchElementException();
        return res;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E root = elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return root;
    }

    @Override
    public E element() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return elements[0];
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (offer(item)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        @SuppressWarnings("unchecked")
        E[] newElements = (E[]) new Object[elements.length];

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }
        elements = newElements;
        size = newSize;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        @SuppressWarnings("unchecked")
        E[] newElements = (E[]) new Object[elements.length];

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }
        elements = newElements;
        size = newSize;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    // Заглушки для оставшихся методов Queue
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean remove(Object o) { return false; }
}