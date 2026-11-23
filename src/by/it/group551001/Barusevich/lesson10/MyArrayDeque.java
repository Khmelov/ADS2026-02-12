package by.it.group551001.Barusevich.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements = (E[]) new Object[16];
    private int size = 0;

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        for (int i = size; i > 0; i--) {
            elements[i] = elements[i - 1];
        }
        elements[0] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        elements[size++] = e;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[size - 1];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = elements[0];
        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E e = elements[--size];
        elements[size] = null;
        return e;
    }

    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : elements[0]; }
    @Override public E peekLast() { return size == 0 ? null : elements[size - 1]; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean contains(Object o) { for (int i = 0; i < size; i++) if (o.equals(elements[i])) return true; return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Object[] toArray() { Object[] arr = new Object[size]; for(int i=0;i<size;i++) arr[i]=elements[i]; return arr; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { for (Object e : c) if (!contains(e)) return false; return true; }
    @Override public boolean addAll(Collection<? extends E> c) { for (E e : c) add(e); return true; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { for (int i = 0; i < size; i++) elements[i] = null; size = 0; }
    @Override public boolean remove(Object o) { return false; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
}