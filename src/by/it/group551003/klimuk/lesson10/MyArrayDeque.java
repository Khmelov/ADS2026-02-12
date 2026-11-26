package by.it.group551003.klimuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        head = 0;
        tail = 0;
        size = 0;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i = head;
        for (int count = 0; count < size; count++) {
            sb.append(elements[i]);
            if (count < size - 1) {
                sb.append(", ");
            }
            i = (i + 1) % elements.length;
        }
        sb.append(']');
        return sb.toString();
    }




    @SuppressWarnings("unchecked")
    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        E[] a = (E[]) new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public void addFirst(E e) {
        if (e != null){
            head = (head - 1 + elements.length) % elements.length;
            elements[head] = e;
            if (head == tail) {
                doubleCapacity();
            }
            size++;
        }
    }

    @Override
    public void addLast(E e) {
        if (e != null){
            elements[tail] = e;
            tail = (tail + 1) % elements.length;
            if (tail == head) {
                doubleCapacity();
            }
            size++;
        }

    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }





    @Override
    public E pollFirst() {
        E result = null;
        if (size != 0){
            result = elements[head];
            elements[head] = null;
            head = (head + 1) % elements.length;
            size--;
        }
        return result;
    }

    @Override
    public E pollLast() {
        E result = null;
        if (size != 0){
            tail = (tail - 1 + elements.length) % elements.length;
            result = elements[tail];
            elements[tail] = null; // Помощь сборщику мусора
            size--;
        }
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }




    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E element() {
        return getFirst();
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
    public E removeFirst() {
        E x = pollFirst();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E removeLast() {
        E x = pollLast();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : getLast();
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
    public void clear() {
        E[] empty = (E[]) new Object[16];
        elements = empty;
        head = 0;
        tail = 0;
        size = 0;
    }

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
        Object[] result = new Object[size];
        int i = head;
        for (int k = 0; k < size; k++) {
            result[k] = elements[i];
            i = (i + 1) % elements.length;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}