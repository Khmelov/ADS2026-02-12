package by.it.group510902.zenkovich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private int capacity;

    public MyArrayDeque(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be 1 or higher");
        }
        this.capacity = capacity;
        this.elements = (E[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public MyArrayDeque() {
        this(100);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        E[] newElements = (E[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % capacity];
        }

        this.elements = newElements;
        this.head = 0;
        this.tail = size;
        this.capacity = newCapacity;
    }



    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % capacity;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }


    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("");
        }
        if (size == capacity) {
            resize();
        }
        head = (head - 1 + capacity) % capacity;
        elements[head] = element;
        size++;

    }


    @Override
    public void addLast(E element){
        if (element == null) {
            throw new NullPointerException("");
        }
        if (size == capacity) {
            resize();
        }
        elements[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
    }


    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NullPointerException("");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NullPointerException("");
        }
        int lastIdx = (tail - 1 + capacity) % capacity;
        return elements[lastIdx];
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
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % capacity;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + capacity) % capacity;
        E result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NullPointerException("");
                }
                E item = elements[(head + currentIndex) % capacity];
                currentIndex++;
                return item;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E removeFirst() { return pollFirst(); }

    @Override
    public E removeLast() { return pollLast(); }

    @Override
    public E peekFirst() { return size == 0 ? null : elements[head]; }

    @Override
    public E peekLast() { return size == 0 ? null : elements[(tail - 1 + capacity) % capacity]; }

    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(head + i) % capacity] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }
    @Override
    public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

}
