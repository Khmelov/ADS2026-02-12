package by.it.group510902.makaruk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // Приватный массив для физического хранения элементов
    private E[] elements;
    // Индекс головного (первого) элемента очереди
    private int head;
    // Индекс позиции, куда будет записан следующий элемент в конец
    private int tail;
    // Стартовая емкость массива (обязательно степень двойки)
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
    }

    // Метод автоматического удвоения массива при его полном заполнении
    @SuppressWarnings("unchecked")
    private void doubleCapacity() {
        int p = head;
        int r = elements.length;
        int n = r - p;
        int newCapacity = r << 1;
        E[] a = (E[]) new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, n);
        System.arraycopy(elements, 0, a, n, p);
        elements = a;
        head = 0;
        tail = r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int size = size();
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = (current + 1) & (elements.length - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        head = (head - 1) & (elements.length - 1);
        elements[head] = element;
        if (head == tail) doubleCapacity();
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        elements[tail] = element;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) doubleCapacity();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        E result = elements[head];
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E getLast() {
        E result = elements[(tail - 1) & (elements.length - 1)];
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        int h = head;
        E result = elements[h];
        if (result == null) return null;
        elements[h] = null;
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    @Override
    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
        if (result == null) return null;
        elements[t] = null;
        tail = t;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) {
            head = tail = 0;
            int i = h;
            int mask = elements.length - 1;
            do {
                elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = head;
            private final int fence = tail;
            @Override public boolean hasNext() { return cursor != fence; }
            @Override public E next() {
                if (cursor == fence) throw new NoSuchElementException();
                E result = elements[cursor];
                cursor = (cursor + 1) & (elements.length - 1);
                return result;
            }
        };
    }
    public boolean offerFirst(E e) { return false; }
    public boolean offerLast(E e) { return false; }
    public E removeFirst() { return null; }
    public E removeLast() { return null; }
    public E peekFirst() { return null; }
    public E peekLast() { return null; }
    public boolean removeFirstOccurrence(Object o) { return false; }
    public boolean removeLastOccurrence(Object o) { return false; }
    public boolean offer(E e) { return false; }
    public E remove() { return null; }
    public E peek() { return null; }
    public void push(E e) { addFirst(e); }
    public E pop() { return pollFirst(); }
    public boolean remove(Object o) { return false; }
    public boolean contains(Object o) { return false; }
    public Iterator<E> descendingIterator() { return null; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}