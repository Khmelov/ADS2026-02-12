package by.it.group551004.velichko.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
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
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }
    @Override
    public boolean offerFirst(E e) {
        addFirst(e); return true;
    }
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        return poll();
    }
    @Override
    public E removeFirst() {
        return pollFirst();
    }
    @Override
    public E removeLast() {
        return pollLast();
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[head];
    }
    @Override
    public E peekFirst() {
        return peek();
    }
    @Override
    public E peekLast() {
        return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void push(E e) {
        addFirst(e);
    }
    @Override
    public E pop() {
        return pollFirst();
    }
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++)
            elements[i] = null;
        head = 0;
        tail = 0;
        size = 0;
    }
}