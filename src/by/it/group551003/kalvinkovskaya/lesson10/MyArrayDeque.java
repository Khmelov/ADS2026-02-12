package by.it.group551003.kalvinkovskaya.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Collection;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void resize() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        String result = "[";
        result += elements[head % elements.length];
        for (int i = 1; i < size; i++) {
            result += ", " + elements[(head + i) % elements.length];
        }
        result += "]";
        return result;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        resize();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        resize();
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
        if (size == 0) {
            throw new RuntimeException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new RuntimeException("Deque is empty");
        }
        return elements[(tail - 1 + elements.length) % elements.length];
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
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        boolean modified = false;
        for (E e : c) {
            addLast(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        boolean modified = false;
        int writeIdx = 0;

        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % elements.length];
            if (!c.contains(e)) {
                elements[(head + writeIdx) % elements.length] = e;
                writeIdx++;
            } else {
                modified = true;
            }
        }

        for (int i = writeIdx; i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        size = writeIdx;
        tail = (head + size) % elements.length;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        boolean modified = false;
        int writeIdx = 0;

        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % elements.length];
            if (c.contains(e)) {
                elements[(head + writeIdx) % elements.length] = e;
                writeIdx++;
            } else {
                modified = true;
            }
        }

        for (int i = writeIdx; i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        size = writeIdx;
        tail = (head + size) % elements.length;
        return modified;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        E res = pollFirst();
        if (res == null) throw new RuntimeException("Deque is empty");
        return res;
    }

    @Override
    public E removeLast() {
        E res = pollLast();
        if (res == null) throw new RuntimeException("Deque is empty");
        return res;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        int idx = -1;
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % elements.length];
            if ((e == null && o == null) || (e != null && e.equals(o))) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;

        if (idx < size / 2) {
            for (int i = idx; i > 0; i--) {
                elements[(head + i) % elements.length] = elements[(head + i - 1) % elements.length];
            }
            elements[head] = null;
            head = (head + 1) % elements.length;
        } else {
            for (int i = idx; i < size - 1; i++) {
                elements[(head + i) % elements.length] = elements[(head + i + 1) % elements.length];
            }
            tail = (tail - 1 + elements.length) % elements.length;
            elements[tail] = null;
        }
        size--;
        return true;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        int idx = -1;
        for (int i = size - 1; i >= 0; i--) {
            E e = elements[(head + i) % elements.length];
            if ((e == null && o == null) || (e != null && e.equals(o))) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;

        if (idx < size / 2) {
            for (int i = idx; i > 0; i--) {
                elements[(head + i) % elements.length] = elements[(head + i - 1) % elements.length];
            }
            elements[head] = null;
            head = (head + 1) % elements.length;
        } else {
            for (int i = idx; i < size - 1; i++) {
                elements[(head + i) % elements.length] = elements[(head + i + 1) % elements.length];
            }
            tail = (tail - 1 + elements.length) % elements.length;
            elements[tail] = null;
        }
        size--;
        return true;
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
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % elements.length];
            if ((e == null && o == null) || (e != null && e.equals(o))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = elements[(head + i) % elements.length];
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Object[] res = new Object[size];
            for (int i = 0; i < size; i++) {
                res[i] = elements[(head + i) % elements.length];
            }
            return (T[]) res;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[(head + i) % elements.length];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new RuntimeException("No more elements");
                E result = elements[(head + currentIndex) % elements.length];
                currentIndex++;
                count++;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int currentIndex = size - 1;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new RuntimeException("No more elements");
                E result = elements[(head + currentIndex) % elements.length];
                currentIndex--;
                count++;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
