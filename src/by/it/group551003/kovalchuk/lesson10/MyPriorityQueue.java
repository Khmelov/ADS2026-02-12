package by.it.group551003.kovalchuk.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private void siftUp(int k) {
        E key = elements[k];
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E parentElem = elements[parent];
            if (compare(key, parentElem) >= 0) {
                break;
            }
            elements[k] = parentElem;
            k = parent;
        }
        elements[k] = key;
    }

    private void siftDown(int k) {
        E key = elements[k];
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = elements[child];
            int right = child + 1;

            if (right < size && compare(c, elements[right]) > 0) {
                child = right;
                c = elements[child];
            }
            if (compare(key, c) <= 0) {
                break;
            }
            elements[k] = c;
            k = child;
        }
        elements[k] = key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
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
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == elements.length) {
            grow();
        }
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int lastIndex = size - 1;
        E result = elements[0];
        E lastElem = elements[lastIndex];
        elements[lastIndex] = null;
        size--;
        if (size > 0) {
            elements[0] = lastElem;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
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
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[i] = null;
                modified = true;
            } else {
                elements[newSize] = elements[i];
                if (newSize != i) {
                    elements[i] = null;
                }
                newSize++;
            }
        }
        if (modified) {
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[i] = null;
                modified = true;
            } else {
                elements[newSize] = elements[i];
                if (newSize != i) {
                    elements[i] = null;
                }
                newSize++;
            }
        }
        if (modified) {
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int i) {
        int lastIndex = size - 1;
        if (lastIndex == i) {
            elements[i] = null;
            size--;
        } else {
            E moved = elements[lastIndex];
            elements[lastIndex] = null;
            size--;
            elements[i] = moved;
            siftDown(i);
            if (elements[i] == moved) {
                siftUp(i);
            }
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
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






    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}