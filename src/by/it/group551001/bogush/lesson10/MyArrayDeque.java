package by.it.group551001.bogush.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements;
    private int head; // индекс первого элемента
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        size = 0;
    }

    // ------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private void growIfFull() {
        if (size < elements.length) {
            return;
        }
        E[] bigger = (E[]) new Object[elements.length * 2];
        // переписываем элементы по порядку, head становится 0
        for (int i = 0; i < size; i++) {
            int idx = head + i;
            if (idx >= elements.length) {
                idx = idx - elements.length;
            }
            bigger[i] = elements[idx];
        }
        elements = bigger;
        head = 0;
    }

    private int physicalIndex(int logicalIndex) {
        int idx = head + logicalIndex;
        if (idx >= elements.length) {
            idx = idx - elements.length;
        }
        return idx;
    }

    private void checkNotNull(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[physicalIndex(i)]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
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
        checkNotNull(element);
        growIfFull();
        head = head - 1;
        if (head < 0) {
            head = elements.length - 1;
        }
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        checkNotNull(element);
        growIfFull();
        elements[physicalIndex(size)] = element;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[physicalIndex(size - 1)];
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
        elements[head] = null; // чтобы не держать ссылку
        head = head + 1;
        if (head == elements.length) {
            head = 0;
        }
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int lastIdx = physicalIndex(size - 1);
        E result = elements[lastIdx];
        elements[lastIdx] = null;
        size--;
        return result;
    }

    // ------------------------------------------------------------------
    // Простые бонусные методы (дёшево реализуются через обязательные)
    // ------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (size > 0) {
            pollFirst();
        }
        head = 0;
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
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return elements[head];
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return elements[physicalIndex(size - 1)];
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // ------------------------------------------------------------------
    // Остальное из интерфейса -- не обязательно, заглушки
    // ------------------------------------------------------------------

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // ------------------------------------------------------------------
    // Быстрая проверка
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        MyArrayDeque<Integer> d = new MyArrayDeque<>();
        for (int i = 1; i <= 20; i++) {
            d.addLast(i);          // проверка роста массива
        }
        d.addFirst(0);
        System.out.println(d + " size=" + d.size());
        System.out.println(d.getFirst() + " " + d.getLast() + " " + d.element());
        System.out.println(d.poll() + " " + d.pollLast() + " " + d.pollFirst());
        System.out.println(d + " size=" + d.size());

        MyArrayDeque<String> e = new MyArrayDeque<>();
        System.out.println(e + " " + e.poll() + " " + e.pollFirst() + " " + e.pollLast());
        try {
            e.getFirst();
        } catch (NoSuchElementException ex) {
            System.out.println("getFirst on empty -> NoSuchElementException");
        }
    }
}