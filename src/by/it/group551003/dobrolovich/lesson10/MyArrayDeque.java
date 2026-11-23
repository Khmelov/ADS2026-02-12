package by.it.group551003.dobrolovich.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements = new Object[16];
    private int size = 0;

    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public int size() { return size; }

    public boolean add(E element) { addLast(element); return true; }
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        System.arraycopy(elements, 0, elements, 1, size);
        elements[0] = element;
        size++;
    }
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        elements[size++] = element;
    }

    public E element() { return getFirst(); }
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[0];
    }
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[size - 1];
    }

    public E poll() { return pollFirst(); }
    public E pollFirst() {
        if (size == 0) return null;
        E e = (E) elements[0];
        System.arraycopy(elements, 1, elements, 0, size - 1);
        elements[--size] = null;
        return e;
    }
    public E pollLast() {
        if (size == 0) return null;
        E e = (E) elements[--size];
        elements[size] = null;
        return e;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCap = elements.length * 2;
            if (newCap < minCapacity) newCap = minCapacity;
            elements = Arrays.copyOf(elements, newCap);
        }
    }

    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    public void clear() { throw new UnsupportedOperationException(); }
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    public boolean isEmpty() { return size == 0; }
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    public boolean offer(E e) { return offerLast(e); }
    public boolean offerFirst(E e) { addFirst(e); return true; }
    public boolean offerLast(E e) { addLast(e); return true; }
    public E peek() { return peekFirst(); }
    public E peekFirst() { return size == 0 ? null : (E) elements[0]; }
    public E peekLast() { return size == 0 ? null : (E) elements[size - 1]; }
    public E pop() { return removeFirst(); }
    public void push(E e) { addFirst(e); }
    public E remove() { return removeFirst(); }
    public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}