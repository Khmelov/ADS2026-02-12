package by.it.group551002.savitsky.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int size;
    private int head;

    public MyArrayDeque() {
        data = (E[]) new Object[1000];
        size = 0;
        head = 0;
    }

    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            result = result + data[(head + i) % data.length];
            if (i < size - 1) {
                result = result + ", ";
            }
        }

        return result + "]";
    }

    @Override
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        if (size == data.length) {
            grow();
        }

        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        if (size == data.length) {
            grow();
        }

        data[(head + size) % data.length] = e;
        size++;
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E result = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E result = data[(head + size - 1) % data.length];
        data[(head + size - 1) % data.length] = null;
        size--;
        return result;
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return data[(head + size - 1) % data.length];
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public int size() {
        return size;
    }

    private void grow() {
        E[] bigger = (E[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[(head + i) % data.length];
        }

        data = bigger;
        head = 0;
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
}
