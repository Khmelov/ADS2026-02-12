package by.it.group510902.paleychik.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements;
    private int head;
    private int tail;
    private int size;


    public MyArrayDeque() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    // =========================================================================
    // Обязательные к реализации методы
    // =========================================================================

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
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        ensureCapacity();
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
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int lastIdx = (tail - 1 + elements.length) % elements.length;
        return elements[lastIdx];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        E result = elements[head];
        elements[head] = null; // Очищаем ссылку для garbage collector
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E result = elements[tail];
        elements[tail] = null; // Очищаем ссылку для garbage collector
        size--;
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // =========================================================================
    // Вспомогательные приватные методы
    // =========================================================================

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Копируем элементы с сохранением логического порядка
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            this.elements = newElements;
            this.head = 0;
            this.tail = size;
        }
    }

    // =========================================================================
    // Прочие методы интерфейса Deque (заглушки для соблюдения контракта)
    // =========================================================================

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) { return add(e); }
    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E remove() { return removeFirst(); }
    @Override
    public E removeFirst() {
        E x = pollFirst();
        if (x == null) throw new NoSuchElementException("Deque is empty");
        return x;
    }
    @Override
    public E removeLast() {
        E x = pollLast();
        if (x == null) throw new NoSuchElementException("Deque is empty");
        return x;
    }

    @Override
    public E peek() { return peekFirst(); }
    @Override
    public E peekFirst() { return isEmpty() ? null : getFirst(); }
    @Override
    public E peekLast() { return isEmpty() ? null : getLast(); }

    @Override
    public void push(E e) { addFirst(e); }
    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public void clear() {
        while (poll() != null);
    }
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}