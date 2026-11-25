package by.it.group510902.shimul.lesson10;

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

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    // =========================================================================
    // ОБЯЗАТЕЛЬНЫЕ К РЕАЛИЗАЦИИ МЕТОДЫ
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
            throw new NullPointerException("Элемент не может быть null");
        }
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста");
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        E result = elements[head];
        elements[head] = null; // очищаем ссылку для Garbage Collector
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        E result = elements[lastIndex];
        elements[lastIndex] = null; // очищаем ссылку для Garbage Collector
        tail = lastIndex;
        size--;
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
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

    // Вспомогательный метод расширения массива при заполнении
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    // =========================================================================
    // ОСТАЛЬНЫЕ МЕТОДЫ ИНТЕРФЕЙСА DEQUE (Заглушки / Заглушки с исключением)
    // =========================================================================

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E removeFirst() { return getFirst(); }

    @Override
    public E removeLast() { return getLast(); }

    @Override
    public E peekFirst() { return isEmpty() ? null : getFirst(); }

    @Override
    public E peekLast() { return isEmpty() ? null : getLast(); }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }


    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

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
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
