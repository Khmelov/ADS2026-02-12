package by.it.group551004.pedko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private final int STD_SIZE = 8;
    private E[] my_deque = (E[]) new Object[STD_SIZE];
    private int size = 0, capacity = STD_SIZE;
    private int head = 0, tail = 0;

    private int inc(int index) {
        index = (index+1) % my_deque.length;
        return index;
    }

    private int dec(int index) {
        index = (index-1 + my_deque.length) % my_deque.length;
        return index;
    }

    void extend() {
        int newCapacity = my_deque.length * 2;
        E[] new_deque = (E[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            new_deque[i] = my_deque[(head + i) % my_deque.length];
        }

        my_deque = new_deque;
        capacity = newCapacity;
        head = 0;
        tail = size;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(my_deque[(head + i) % my_deque.length]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        if (size == capacity) {
            extend();
        }
        head = dec(head);
        my_deque[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == capacity) {
            extend();
        }
        my_deque[tail] = element;
        tail = inc(tail);
        size++;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return my_deque[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return my_deque[dec(tail)];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = my_deque[head];
        my_deque[head] = null;   // освобождаем
        size--;
        head = inc(head);
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = dec(tail);
        E value = my_deque[tail];
        my_deque[tail] = null;   // освобождаем
        size--;
        return value;
    }

    @Override
    public E poll() {
        return pollFirst();
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
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
}
