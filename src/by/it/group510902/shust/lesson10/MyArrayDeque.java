package by.it.group510902.shust.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements = new Object[16];
    private int head = 0;
    private int tail = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int i = head;
        boolean first = true;
        while (i != tail) {
            if (!first) sb.append(", ");
            sb.append(elements[i]);
            first = false;
            i = (i + 1) & (elements.length - 1);
        }
        sb.append("]");
        return sb.toString();
    }

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
        head = (head - 1) & (elements.length - 1);
        elements[head] = element;
        if (head == tail) doubleCapacity();
    }

    @Override
    public void addLast(E element) {
        elements[tail] = element;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) doubleCapacity();
    }

    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        if (head == tail) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (head == tail) throw new NoSuchElementException();
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (head == tail) return null;
        E result = (E) elements[head];
        elements[head] = null;
        head = (head + 1) & (elements.length - 1);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollLast() {
        if (head == tail) return null;
        tail = (tail - 1) & (elements.length - 1);
        E result = (E) elements[tail];
        elements[tail] = null;
        return result;
    }

    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E peek() { return null; }
    @Override public void push(E e) {}
    @Override public E pop() { return null; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() {}
}