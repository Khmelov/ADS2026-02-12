package by.it.group551002.brutski.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    int start_capacity = 16;
    private Object[] array = new Object[start_capacity];
    int head = 0, size = 0, tail = -1;
    double k = 1.5;

    private void reallocate(int min_size) {
        if (min_size > array.length) {
            int newCapacity = array.length == 0 ? 15 : (int) (array.length * k);
            if (newCapacity < min_size) {
                newCapacity = min_size;
            }
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = array[(head + i) % array.length];
            }
            array = newElements;
            head = 0;
            tail = size - 1;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                str.append(", ");
            }
            Object e = array[(head + i) % array.length];
            str.append(e == this ? "(this Collection)" : String.valueOf(e));
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        reallocate(size + 1);
        head = ((head - 1) % array.length + array.length) % array.length;
        array[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        reallocate(size + 1);
        tail = (tail + 1) % array.length;
        array[tail] = e;
        size++;
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        return (E) array[head];
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        return (E) array[tail];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0)
            return null;

        Object deleted = array[head];

        array[head] = null;
        size--;
        head = (head + 1) % array.length;

        return (E) deleted;
    }

    @Override
    public E pollLast() {
        if (size == 0)
            return null;

        Object deleted = array[tail];
        tail = ((tail - 1) % array.length + array.length) % array.length;
        size--;

        return (E) deleted;
    }


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////


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
