package by.it.group510902.karcheuski.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }


    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
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
        if (size == elements.length) ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }


    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }


    @Override public boolean offer(E e)                          { return false; }
    @Override public boolean offerFirst(E e)                     { return false; }
    @Override public boolean offerLast(E e)                      { return false; }

    @Override public E remove()                                  { return null; }
    @Override public E removeFirst()                             { return null; }
    @Override public E removeLast()                              { return null; }

    @Override public E peek()                                    { return null; }
    @Override public E peekFirst()                               { return null; }
    @Override public E peekLast()                                { return null; }

    @Override public boolean removeFirstOccurrence(Object o)     { return false; }
    @Override public boolean removeLastOccurrence(Object o)      { return false; }

    @Override public boolean remove(Object o)                    { return false; }
    @Override public boolean contains(Object o)                  { return false; }
    @Override public boolean isEmpty()                           { return false; }
    @Override public void push(E e)                              { }
    @Override public E pop()                                     { return null; }

    @Override public Iterator<E> iterator()                      { return null; }
    @Override public Iterator<E> descendingIterator()            { return null; }

    @Override public Object[] toArray()                          { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a)                      { return a; }

    @Override public boolean containsAll(Collection<?> c)        { return false; }
    @Override public boolean addAll(Collection<? extends E> c)   { return false; }
    @Override public boolean removeAll(Collection<?> c)          { return false; }
    @Override public boolean retainAll(Collection<?> c)          { return false; }
    @Override public void clear()                                { }
}
