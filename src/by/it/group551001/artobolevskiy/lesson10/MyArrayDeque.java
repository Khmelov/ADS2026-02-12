package by.it.group551001.artobolevskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    E[] elements = (E[]) new Object[10];
    int size = 0;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            s.append(elements[i]);
            if (i < size - 1) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 1, size);
            elements = newElements;
        } else {
            System.arraycopy(elements, 0, elements, 1, size);
        }
        elements[0] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        elements[size] = element;
        size++;
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }



    @Override
    public E getFirst() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            return null;
        }
        return elements[size - 1];
    }

    @Override public E poll() {
        return pollFirst();
    }

    @Override public E pollFirst() {
        if (size == 0){
            return null;
        }

        E element = elements[0];
        System.arraycopy(elements, 1, elements, 0, size - 1);
        elements[size - 1] = null;
        size--;

        return element;
    }

    @Override public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[size - 1];
        elements[size - 1] = null;
        size--;

        return element;
    }


    @Override public boolean offerFirst(E e) { return false; }
    @Override
    public E peek() {
        return null;
    }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }

    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }
    @Override public boolean offer(E e) { return false; }
    @Override public E remove() { return null; }

    @Override public boolean remove(Object o) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean isEmpty() { return false; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
}

