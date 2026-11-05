package by.it.group510901.kachur.lesson01.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int size;
    private int head;   // индекс первого элемента
    private int tail;   // индекс позиции ПОСЛЕ последнего

    private static final int DEFAULT_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    // ==================== Обязательные методы ====================

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i != size - 1) sb.append(", ");
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
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getFirst() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getLast() {
        E e = peekLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        if (size == 0) { head = 0; tail = 0; }
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        int last = (tail - 1 + elements.length) % elements.length;
        E e = elements[last];
        elements[last] = null;
        tail = last;
        size--;
        if (size == 0) { head = 0; tail = 0; }
        return e;
    }

    // ==================== Вспомогательное ====================

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) return;
        int newCap = elements.length * 2;
        E[] copy = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[(head + i) % elements.length];
        }
        elements = copy;
        head = 0;
        tail = size;
    }
    // ==================== Остальные методы Deque ====================
    // Не входят в обязательный список — оставлены для компиляции.

    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() {
        E e = pollFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }
    @Override public E removeLast() {
        E e = pollLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : elements[head]; }
    @Override public E peekLast() {
        return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
}
