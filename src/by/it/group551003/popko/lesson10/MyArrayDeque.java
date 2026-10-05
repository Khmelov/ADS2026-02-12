package by.it.group551003.popko.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 8;

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
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
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
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean offer(E e) {
        addLast(e);
        return true;
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
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
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
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null
                : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (o == null ? elements[idx] == null : o.equals(elements[idx])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (o == null ? elements[idx] == null : o.equals(elements[idx])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % elements.length];
            hash = 31 * hash + (e == null ? 0 : e.hashCode());
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque)) return false;
        Deque<?> other = (Deque<?>) o;
        if (other.size() != size) return false;
        Iterator<?> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object a = it1.next();
            Object b = it2.next();
            if (a == null ? b != null : !a.equals(b)) return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E value = elements[(head + i) % elements.length];
                i++;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[(head + i) % elements.length];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                result[i] = (T) elements[(head + i) % elements.length];
            }
            return result;
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
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (c.contains(elements[idx])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (!c.contains(elements[idx])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (o == null ? elements[idx] == null : o.equals(elements[idx])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int i = size - 1;

            @Override
            public boolean hasNext() {
                return i >= 0;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E value = elements[(head + i) % elements.length];
                i--;
                return value;
            }
        };
    }

    private void removeAt(int logicalIndex) {
        for (int i = logicalIndex; i < size - 1; i++) {
            int cur = (head + i) % elements.length;
            int next = (head + i + 1) % elements.length;
            elements[cur] = elements[next];
        }
        tail = (tail - 1 + elements.length) % elements.length;
        elements[tail] = null;
        size--;
    }
}