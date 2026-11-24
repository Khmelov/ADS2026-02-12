package by.it.group510901.gulchenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements = (E[]) new Object[8];
    private int head = 0;
    private int size = 0;

    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }

        E[] newElements = (E[]) new Object[elements.length * 2];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }

        elements = newElements;
        head = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += elements[(head + i) % elements.length];
        }

        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity();

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity();

        int index = (head + size) % elements.length;
        elements[index] = element;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        int index = (head + size - 1) % elements.length;
        return elements[index];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E removed = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;

        return removed;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        int index = (head + size - 1) % elements.length;
        E removed = elements[index];
        elements[index] = null;
        size--;

        return removed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public boolean offer(E element) {
        return offerLast(element);
    }

    @Override
    public E removeFirst() {
        E element = getFirst();
        pollFirst();
        return element;
    }

    @Override
    public E removeLast() {
        E element = getLast();
        pollLast();
        return element;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0
                ? null
                : elements[(head + size - 1) % elements.length];
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (size > 0) {
            pollFirst();
        }
        head = 0;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}