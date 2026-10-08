package by.it.group551004.sharkevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 32;

    private E[] elements;
    private int size;
    private int head;
    private int tail;

    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    private int index(int i) {
        return (head + i) % elements.length;
    }

    public void ensureCapasity() {
        if  (size == elements.length) {
            int newCapacity = size * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[index(i)];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[index(i)]);
        }
        sb.append("]");
        return sb.toString();
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
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
    public Iterator<E> descendingIterator() {
        return null;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    public void addFirst(E element) {
        ensureCapasity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    public void addLast(E element) {
        ensureCapasity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
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
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = elements[head];
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    // Опциональные метод
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    public E peekLast() {
        return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }


}
