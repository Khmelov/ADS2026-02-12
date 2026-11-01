package by.it.group551003.yakushev.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements;
    private int head; // Индекс первого элемента
    private int tail; // Индекс следующей свободной ячейки после последнего
    private int size;

    public MyArrayDeque() {
        this.elements = new Object[16];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity * 2;
        Object[] newElements = new Object[newCapacity];

        // Копируем элементы так, чтобы head стал 0
        System.arraycopy(elements, head, newElements, 0, oldCapacity - head);
        System.arraycopy(elements, 0, newElements, oldCapacity - head, head);

        elements = newElements;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            sb.append(elements[idx]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        if (size == elements.length) grow();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        if (size == elements.length) grow();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E element() {
        E e = getFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int idx = (tail - 1 + elements.length) % elements.length;
        return (E) elements[idx];
    }

    @Override
    public E poll() { return pollFirst(); }

    @Override
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (size == 0) return null;
        E e = (E) elements[head];
        elements[head] = null; // Help GC
        head = (head + 1) % elements.length;
        size--;
        return e;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E e = (E) elements[tail];
        elements[tail] = null; // Help GC
        size--;
        return e;
    }

    // Остальные методы Deque можно реализовать через базовые или выбросить UnsupportedOperationException,
    // если они не требуются по заданию явно. Ниже заглушки для компиляции.
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst(); } // Упрощено для примера
    @Override public E removeLast() { return pollLast(); }
    @Override public E peekFirst() { return (size == 0) ? null : getFirst(); }
    @Override public E peekLast() { return (size == 0) ? null : getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() {
        for(int i=0; i<elements.length; i++) elements[i]=null;
        head=tail=size=0;
    }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}