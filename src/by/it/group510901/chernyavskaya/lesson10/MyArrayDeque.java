package by.it.group510901.chernyavskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// сделать двустороннюю очередь на кольцевом массиве

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements = (E[]) new Object[16];
    private int head = 0;
    private int tail = 0;

    @Override
    public String toString() {
        if (head == tail) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int current = head;
        while (current != tail) {
            sb.append(elements[current]);
            current = (current + 1) & (elements.length - 1);
            if (current != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Deque too big");
        }
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = (E[]) a;
        head = 0;
        tail = n;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) return;
        head = (head - 1) & (elements.length - 1);
        elements[head] = e;
        if (head == tail) {
            doubleCapacity();
        }
    }

    @Override
    public void addLast(E e) {
        if (e == null) return;
        elements[tail] = e;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) {
            doubleCapacity();
        }
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E getFirst() {
        if (head == tail) return null;
        return elements[head];
    }

    @Override
    public E getLast() {
        if (head == tail) return null;
        return elements[(tail - 1) & (elements.length - 1)];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (head == tail) return null;
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) & (elements.length - 1);
        return result;
    }

    @Override
    public E pollLast() {
        if (head == tail) return null;
        int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
        elements[t] = null;
        tail = t;
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E peek() {
        return getFirst();
    }

    @Override
    public E peekFirst() {
        return getFirst();
    }

    @Override
    public E peekLast() {
        return getLast();
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
    public void clear() {
        head = 0;
        tail = 0;
    }

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return pollFirst(); }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
}