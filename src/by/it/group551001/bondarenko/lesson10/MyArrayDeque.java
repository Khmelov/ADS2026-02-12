package by.it.group551001.bondarenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
    }

    @SuppressWarnings("unchecked")
    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p; // число элементов справа от head
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Deque is too big");
        }

        E[] a = (E[]) new Object[newCapacity];
        // Копируем элементы от head до конца массива
        for (int i = 0; i < r; i++) {
            a[i] = elements[p + i];
        }
        // Копируем элементы от начала массива до head
        for (int i = 0; i < p; i++) {
            a[r + i] = elements[i];
        }

        elements = a;
        head = 0;
        tail = n;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        head = (head - 1) & (elements.length - 1);
        elements[head] = element;
        if (head == tail) {
            doubleCapacity();
        }
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        elements[tail] = element;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) {
            doubleCapacity();
        }
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elements[(tail - 1) & (elements.length - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        int h = head;
        E result = elements[h];
        if (result == null) {
            return null;
        }
        elements[h] = null;
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    @Override
    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
        if (result == null) {
            return null;
        }
        elements[t] = null;
        tail = t;
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int currentSize = size();
        for (int i = 0; i < currentSize; i++) {
            int index = (head + i) & (elements.length - 1);
            sb.append(elements[index]);
            if (i < currentSize - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Заглушки для остатка интерфейса Deque<E>          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean isEmpty() { return head == tail; }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return isEmpty() ? null : elements[head]; }
    @Override public E peekLast() { return isEmpty() ? null : elements[(tail - 1) & (elements.length - 1)]; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { E x = pollFirst(); if (x == null) throw new NoSuchElementException(); return x; }
    @Override public E removeLast() { E x = pollLast(); if (x == null) throw new NoSuchElementException(); return x; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked")
    @Override public void clear() { head = 0; tail = 0; elements = (E[]) new Object[DEFAULT_CAPACITY]; }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}