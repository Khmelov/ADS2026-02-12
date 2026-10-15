package by.it.group510902.paleychik.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements;
    private int size;

    public MyPriorityQueue() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // =========================================================================
    // Обязательные методы из задания C
    // =========================================================================

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
            elements[i] = null;
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
            throw new NullPointerException("Element cannot be null");
        }
        ensureCapacity();
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return elements[0];
    }

    @Override
    public E peek() {
        return isEmpty() ? null : elements[0];
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = elements[0];
        E lastElement = elements[size - 1];
        elements[size - 1] = null;
        size--;

        if (size > 0) {
            elements[0] = lastElement;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return true;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return true;
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
        if (c == null) {
            throw new NullPointerException();
        }
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
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        boolean modified = false;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
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
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
            }
            size = writeIndex;
            heapify(); // Восстанавливаем свойства кучи
        }
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
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

    // =========================================================================
    // Вспомогательные приватные методы
    // =========================================================================

    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    // Просеивание элемента вверх
    private void siftUp(int k) {
        Comparable<? super E> key = (Comparable<? super E>) elements[k];
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = elements[parent];
            if (key.compareTo((E) e) >= 0) {
                break;
            }
            elements[k] = (E) e;
            k = parent;
        }
        elements[k] = (E) key;
    }

    // Просеивание элемента вниз
    private void siftDown(int k) {
        Comparable<? super E> key = (Comparable<? super E>) elements[k];
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = elements[child];
            int right = child + 1;

            if (right < size && ((Comparable<? super E>) c).compareTo(elements[right]) > 0) {
                child = right;
                c = elements[child];
            }

            if (key.compareTo((E) c) <= 0) {
                break;
            }
            elements[k] = (E) c;
            k = child;
        }
        elements[k] = (E) key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // =========================================================================
    // Нереализованные методы интерфейса Collection
    // =========================================================================

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}