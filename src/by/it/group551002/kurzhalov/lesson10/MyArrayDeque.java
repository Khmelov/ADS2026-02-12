package by.it.group551002.kurzhalov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int INITIAL_CAPACITY = 16;
    private E[] elements;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (head == tail && elements[head] != null) {
            int oldCapacity = elements.length;
            int newCapacity = oldCapacity * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            int rightSegment = oldCapacity - head;
            System.arraycopy(elements, head, newElements, 0, rightSegment);
            System.arraycopy(elements, 0, newElements, rightSegment, head);

            elements = newElements;
            head = 0;
            tail = oldCapacity;
        }
    }

    @Override
    public int size() {
        if (elements[head] == null && head == tail) {
            return 0;
        }
        if (tail > head) {
            return tail - head;
        }
        return elements.length - head + tail;
    }

    @Override
    public void addFirst(E element) {
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        ensureCapacity();
    }

    @Override
    public void addLast(E element) {
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        ensureCapacity();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E getFirst() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        int lastIdx = (tail - 1 + elements.length) % elements.length;
        return elements[lastIdx];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size() == 0) {
            return null;
        }
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    @Override
    public E pollLast() {
        if (size() == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E result = elements[tail];
        elements[tail] = null;
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int currentSize = size();
        for (int i = 0; i < currentSize; i++) {
            int idx = (head + i) % elements.length;
            sb.append(elements[idx]);
            if (i < currentSize - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // =========================================================================
    // Заглушки для остальных методов интерфейса Deque (не проверяются в Task A)
    // =========================================================================

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return getFirst(); }
    @Override public E removeLast() { return getLast(); }
    @Override public E peekFirst() { return size() == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size() == 0 ? null : getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size() == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { head = 0; tail = 0; }
}