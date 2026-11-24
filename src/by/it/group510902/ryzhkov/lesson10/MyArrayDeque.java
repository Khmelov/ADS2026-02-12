package by.it.group510902.ryzhkov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements;
    private int size;
    private int head;
    private int tail;

    private void increaseCapacity() {
        Object[] newElements = new Object[size * 2];
        for (int i = 0; i < elements.length; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }

    public MyArrayDeque() {
        elements = new Object[10];
        size = 0;
        head = 0;
        tail = 0;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        int i = head;
        while (i != tail) {
            builder.append(elements[i]);

            if (i != tail - 1)
                builder.append(", ");

            i++;
            if (i == elements.length)
                i = 0;
        }
        builder.append("]");

        return builder.toString();
    }

    public int size() {
        return size;
    }

    public void addFirst(E element) {
        if (size == elements.length)
            increaseCapacity();
        head = (elements.length + head - 1) % elements.length;
        elements[head] = element;
        size++;
    }

    public void addLast(E element) {
        if (size == elements.length)
            increaseCapacity();

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E getFirst() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        return (E) elements[head];
    }

    public E getLast() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        return (E) elements[tail - 1];
    }

    public E element() {
        return getFirst();
    }

    public E pollFirst() {
        if (size == 0)
            return null;
        E result = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    public E pollLast() {
        if (size == 0)
            return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E result = (E) elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    public E poll() {
        return pollFirst();
    }

    // Заглушки
    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
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
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
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
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
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
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
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
}