package by.it.group551001.docenko.lesson10;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements = (E[]) new Object[DEFAULT_CAPACITY];
    private int head = 0;
    private int size = 0;
    private int modCount = 0;

    private int physical(int logical) {
        int index = head + logical;
        if (index >= elements.length) {
            index -= elements.length;
        }
        return index;
    }

    private E at(int logical) {
        return elements[physical(logical)];
    }

    private void grow() {
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = at(i);
        }
        elements = bigger;
        head = 0;
    }

    private static void checkNotNull(Object e) {
        if (e == null) {
            throw new NullPointerException();
        }
    }

    private static boolean isEqual(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void removeAt(int logical) {
        for (int i = logical; i < size - 1; i++) {
            elements[physical(i)] = at(i + 1);
        }
        elements[physical(size - 1)] = null;
        size--;
        modCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(at(i));
        }
        return sb.append(']').toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void addFirst(E e) {
        checkNotNull(e);
        if (size == elements.length) {
            grow();
        }
        head = head == 0 ? elements.length - 1 : head - 1;
        elements[head] = e;
        size++;
        modCount++;
    }

    @Override
    public void addLast(E e) {
        checkNotNull(e);
        if (size == elements.length) {
            grow();
        }
        elements[physical(size)] = e;
        size++;
        modCount++;
    }

    @Override
    public boolean add(E e) {
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
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : at(size - 1);
    }

    @Override
    public E peek() {
        return peekFirst();
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
        return at(size - 1);
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
        E result = elements[head];
        elements[head] = null;
        head = physical(1);
        size--;
        modCount++;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int tail = physical(size - 1);
        E result = elements[tail];
        elements[tail] = null;
        size--;
        modCount++;
        return result;
    }

    @Override
    public E poll() {
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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            if (isEqual(o, at(i))) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (isEqual(o, at(i))) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (isEqual(o, at(i))) {
                return true;
            }
        }
        return false;
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
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
    }

    private boolean filter(Collection<?> c, boolean removeMatching) {
        int w = 0;
        for (int r = 0; r < size; r++) {
            E e = at(r);
            if (c.contains(e) != removeMatching) {
                elements[physical(w++)] = e;
            }
        }
        if (w == size) {
            return false;
        }
        for (int i = w; i < size; i++) {
            elements[physical(i)] = null;
        }
        size = w;
        modCount++;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, false);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[physical(i)] = null;
        }
        head = 0;
        size = 0;
        modCount++;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = at(i);
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) at(i);
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private class Itr implements Iterator<E> {
        private final boolean descending;
        private int cursor;
        private int lastRet = -1;
        private int expectedModCount = modCount;

        Itr(boolean descending) {
            this.descending = descending;
            this.cursor = descending ? size - 1 : 0;
        }

        @Override
        public boolean hasNext() {
            return descending ? cursor >= 0 : cursor < size;
        }

        @Override
        public E next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            cursor += descending ? -1 : 1;
            return at(lastRet);
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            removeAt(lastRet);
            if (!descending) {
                cursor = lastRet;
            }
            lastRet = -1;
            expectedModCount = modCount;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr(false);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Itr(true);
    }
}
