package by.it.group551003.kravchenkovikk.lesson10;

import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E>{

    private E[] elements;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[16];
        this.head = 0;
        this.tail = 0;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int begin = head;
        int size = elements.length;
        int r = size - begin;
        int newCapacity = size << 1;
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, begin, newElements, 0, r);
        System.arraycopy(elements, 0, newElements, r, begin);
        elements = (E[]) newElements;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        if (head == tail) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int i = head;
        do {
            sb.append(elements[i]);
            i = (i + 1) & (elements.length - 1);
            if (i == tail) {
                break;
            }
            sb.append(", ");
        } while (true);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean add(Object element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(Object element) {
        head = (head - 1) & (elements.length - 1);
        elements[head] = (E) element;
        if (tail == head) {
            grow();
        }
    }

    @Override
    public void addLast(Object element) {
        elements[tail] = (E) element;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) {
            grow();
        }
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (elements[head] == null)
            throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        int last = (tail - 1) & (elements.length - 1);
        if (elements[last] == null) throw new
                NoSuchElementException();
        return elements[last];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == tail) {
            return null;
        }
        E removedElement = elements[head];
        elements[head] = null;
        head = (head + 1) & (elements.length - 1);
        return removedElement;
    }

    @Override
    public E pollLast() {
        if (head == tail) {
            return null;
        }
        tail = (tail - 1) & (elements.length - 1);
        E removedElement = elements[tail];
        elements[tail] = null;
        return removedElement;
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
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return head == tail; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
}
