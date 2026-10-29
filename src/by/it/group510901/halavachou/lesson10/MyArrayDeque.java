package by.it.group510901.halavachou.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16];
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (head == tail && elements[head] != null) {
            E[] newArr = (E[]) new Object[elements.length * 2];
            int r = elements.length - head;
            System.arraycopy(elements, head, newArr, 0, r);
            System.arraycopy(elements, 0, newArr, r, head);
            head = 0;
            tail = elements.length;
            elements = newArr;
        }
    }

    @Override
    public int size() {
        if (head == tail) return elements[head] == null ? 0 : elements.length;
        return (tail - head + elements.length) % elements.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
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
        if (isEmpty()) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) return null;
        E item = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return item;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E item = elements[tail];
        elements[tail] = null;
        return item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public String toString() {
        int s = size();
        if (s == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < s; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < s - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return getFirst(); }
    @Override public E removeLast() { return getLast(); }
    @Override public E peekFirst() { return isEmpty() ? null : getFirst(); }
    @Override public E peekLast() { return isEmpty() ? null : getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return a; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { elements = (E[]) new Object[16]; head = tail = 0; }
}