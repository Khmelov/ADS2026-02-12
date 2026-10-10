package by.it.group510901.chibisov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] queue;
    private int head;
    private int tail;

    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        queue = new Object[initialCapacity];
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity >= queue.length) {
            int size = size();
            int oldCapacity = queue.length;
            int newCapacity = Math.max(oldCapacity + (oldCapacity >> 1) + 1, minCapacity);
            Object[] newArray = new Object[newCapacity];
            if (head <= tail) {
                System.arraycopy(queue, head, newArray, 0, size);
            } else {
                System.arraycopy(queue, head, newArray, 0, oldCapacity - head);
                System.arraycopy(queue, 0, newArray, oldCapacity - head, tail);
            }
            queue = newArray;
            head = 0;
            tail = size;
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) queue[index];
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int size = size();
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(queue[(head + i) % queue.length]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return (tail - head + queue.length) % queue.length;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size() + 1);
        head = (head - 1 + queue.length) % queue.length;
        queue[head] = e;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size() + 1);
        queue[tail] = e;
        tail = (tail + 1) % queue.length;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return elementAt(head);
    }

    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return elementAt((tail - 1 + queue.length) % queue.length);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) return null;
        E old = elementAt(head);
        queue[head] = null;
        head = (head + 1) % queue.length;
        return old;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) return null;
        tail = (tail - 1 + queue.length) % queue.length;
        E old = elementAt(tail);
        queue[tail] = null;
        return old;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public void clear() {
        if (head <= tail) {
            for (int i = head; i < tail; i++) {
                queue[i] = null;
            }
        } else {
            for (int i = head; i < queue.length; i++) {
                queue[i] = null;
            }
            for (int i = 0; i < tail; i++) {
                queue[i] = null;
            }
        }
        head = tail = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (head <= tail) {
            for (int i = head; i < tail; i++) {
                if (o.equals(queue[i])) return true;
            }
        } else {
            for (int i = head; i < queue.length; i++) {
                if (o.equals(queue[i])) return true;
            }
            for (int i = 0; i < tail; i++) {
                if (o.equals(queue[i])) return true;
            }
        }
        return false;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        int cursor = head;

        @Override
        public boolean hasNext() {
            return cursor != tail;
        }

        @Override
        public E next() {
            if (cursor == tail) {
                throw new NoSuchElementException();
            }
            E e = elementAt(cursor);
            cursor = (cursor + 1) % queue.length;
            return e;
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}
