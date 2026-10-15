package by.it.group551004.karpovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] data;
    private int head;
    private int size;

    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MyArrayDeque(int capacity) {
        if (capacity < 1) {
            capacity = DEFAULT_CAPACITY;
        }
        data = (E[]) new Object[capacity];
        head = 0;
        size = 0;
    }

    private int index(int logical) {
        return (head + logical) % data.length;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newData = (E[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[index(i)];
        }
        data = newData;
        head = 0;
    }

    private void checkNotNull(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
    }

    private void removeAt(int logical) {
        for (int j = logical; j < size - 1; j++) {
            data[index(j)] = data[index(j + 1)];
        }
        data[index(size - 1)] = null;
        size--;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += data[index(i)];
            if (i < size - 1) {
                result += ", ";
            }
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
        checkNotNull(element);
        if (size == data.length) {
            grow();
        }
        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        checkNotNull(element);
        if (size == data.length) {
            grow();
        }
        data[index(size)] = element;
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
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return data[index(size - 1)];
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
        E element = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int i = index(size - 1);
        E element = data[i];
        data[i] = null;
        size--;
        return element;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : data[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : data[index(size - 1)];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(data[index(i)])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(data[index(i)])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(data[index(i)])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[index(i)] = null;
        }
        head = 0;
        size = 0;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(data[index(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(data[index(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[index(i)];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastReturned = cursor++;
                return data[index(lastReturned)];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                removeAt(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int cursor = size - 1;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor >= 0;
            }

            @Override
            public E next() {
                if (cursor < 0) {
                    throw new NoSuchElementException();
                }
                lastReturned = cursor--;
                return data[index(lastReturned)];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                removeAt(lastReturned);
                lastReturned = -1;
            }
        };
    }
}