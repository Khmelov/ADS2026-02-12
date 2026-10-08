package by.it.group551002.sevkovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int capacity;
    private int size;

    private int head = 0;
    private int tail = 0;

    private static final int DEFAULT_CAPACITY = 16;

    // Constructors
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int defaultCapacity) {
        if (defaultCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        elements = (E[]) new Object[defaultCapacity];
        size = 0;
        capacity = defaultCapacity;
    }

    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    // Helpers
    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = capacity * 2;
        E[] newList = (E[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newList[i] = elements[(head + i) % capacity];
        }

        elements = newList;
        head = 0;
        tail = size > 0 ? size - 1 : 0;
        capacity = newCapacity;
    }

    public int size() {
        return size;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        if (size == capacity) grow();

        if (size == 0) {
            head = 0;
            tail = 0;
        } else {
            tail = (tail + 1) % capacity;
        }
        elements[tail] = element;
        size++;
    }

    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        if (size == capacity) grow();

        if (size == 0) {
            head = 0;
            tail = 0;
        } else {
            head = (head - 1 + capacity) % capacity;
        }
        elements[head] = element;
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[tail];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) return null;

        E element = elements[head];
        elements[head] = null;

        if (size == 1) {
            head = 0;
            tail = 0;
        } else {
            head = (head + 1) % capacity;
        }
        size--;
        return element;
    }

    public E pollLast() {
        if (size == 0) return null;

        E element = elements[tail];
        elements[tail] = null;

        if (size == 1) {
            head = 0;
            tail = 0;
        } else {
            tail = (tail - 1 + capacity) % capacity;
        }
        size--;
        return element;
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < size; i++) {
            result.append(elements[(head + i) % capacity]);
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    // Optional methods

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