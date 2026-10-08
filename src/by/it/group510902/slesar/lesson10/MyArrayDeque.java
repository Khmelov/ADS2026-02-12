package by.it.group510902.slesar.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    @SuppressWarnings("unchecked")
    private E[] elements = (E[]) new Object[16];
    private int head = 0;
    private int tail = 0;

    @SuppressWarnings("unchecked")
    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCap = n << 1;
        E[] a = (E[]) new Object[newCap];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public String toString() {
        if (head == tail) return "[]";
        StringBuilder sb = new StringBuilder("[");
        int mask = elements.length - 1;
        int i = head;
        while (i != tail) {
            sb.append(elements[i]);
            i = (i + 1) & mask;
            if (i != tail) sb.append(", ");
        }
        return sb.append("]").toString();
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
        if (element == null) throw new NullPointerException();
        head = (head - 1) & (elements.length - 1);
        elements[head] = element;
        if (head == tail) doubleCapacity();
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        elements[tail] = element;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) doubleCapacity();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        E result = elements[head];
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E getLast() {
        E result = elements[(tail - 1) & (elements.length - 1)];
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        int h = head;
        E result = elements[h];
        if (result == null) return null;
        elements[h] = null;
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    @Override
    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
        if (result == null) return null;
        elements[t] = null;
        tail = t;
        return result;
    }

    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return head == tail; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}