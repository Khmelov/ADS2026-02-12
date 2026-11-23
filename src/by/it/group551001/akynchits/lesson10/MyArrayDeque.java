package by.it.group551001.akynchits.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[16];
        head = 0;
        tail = 0;
        size = 0;
    }

    private int wrap(int index) {
        return (index + data.length) % data.length;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newData = (E[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[wrap(head + i)];
        }
        data = newData;
        head = 0;
        tail = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[wrap(head + i)]);
        }
        return sb.append("]").toString();
    }


    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        if (size == data.length) grow();
        head = wrap(head - 1);
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        if (size == data.length) grow();
        data[tail] = element;
        tail = wrap(tail + 1);
        size++;
    }

    // ===== Чтение без удаления =====

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[wrap(tail - 1)];
    }

    // ===== Удаление с возвратом =====

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E result = data[head];
        data[head] = null;
        head = wrap(head + 1);
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = wrap(tail - 1);
        E result = data[tail];
        data[tail] = null;
        size--;
        return result;
    }

    // ===== Остальные методы Deque<E> — заглушки =====

    @Override public boolean offer(E e) { return false; }
    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peek() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
    @Override public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(java.util.Collection<?> c) { return false; }
    @Override public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return a; }
}