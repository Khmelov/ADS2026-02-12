package by.it.group551001.vinogradov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    @SuppressWarnings("unchecked")
    private E[] elements = (E[]) new Object[10];
    private int last = 0;
    private int front = 0;


    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newLength = elements.length * 2;
        E[] newElements = (E[]) new Object[newLength];

        for (int i = front; i < last; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String s = "[";

        for (int i = front; i < last; i++) {
            if (i > front) {
                s += ", ";
            }
            s += elements[i];
        }
        return s + "]";
    }

    @Override
    public int size() {
        return last-front;
    }

    @Override
    public boolean add(E element) {
        if (last == elements.length) grow();
        elements[last] = element;
        last++;
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (last == elements.length) grow();
        if (front == 0) {
            for (int i = last; i > front; i--) {
                elements[i] = elements[i - 1];

            }
            last++;
        }
        else front--;
        elements[front] = element;
    }

    @Override
    public void addLast(E element) {
        if (last == elements.length) grow();
        elements[last] = element;
        last++;
    }

    @Override
    public E element() {
        if (front == last) throw new NoSuchElementException();
        return elements[front];
    }

    @Override
    public E getFirst() {
        if (front == last) throw new NoSuchElementException();
        return elements[front];
    }

    @Override
    public E getLast() {
        if (front == last) throw new NoSuchElementException();
        return elements[last-1];
    }

    @Override
    public E poll() {
        if (front == last) return null;
        else {
            front++;
            return elements[front-1];
        }
    }

    @Override
    public E pollFirst() {
        if (front == last) return null;
        else {
            front++;
            return elements[front-1];
        }
    }

    @Override
    public E pollLast() {
        if (front == last) return null;
        else {
            last--;
            return elements[last];
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E element) {
        throw notImplemented();
    }

    @Override
    public boolean offerLast(E element) {
        throw notImplemented();
    }

    @Override
    public E removeFirst() {
        throw notImplemented();
    }

    @Override
    public E removeLast() {
        throw notImplemented();
    }

    @Override
    public E peekFirst() {
        throw notImplemented();
    }

    @Override
    public E peekLast() {
        throw notImplemented();
    }

    @Override
    public boolean removeFirstOccurrence(Object object) {
        throw notImplemented();
    }

    @Override
    public boolean removeLastOccurrence(Object object) {
        throw notImplemented();
    }

    @Override
    public boolean offer(E element) {
        throw notImplemented();
    }

    @Override
    public E remove() {
        throw notImplemented();
    }

    @Override
    public E peek() {
        throw notImplemented();
    }

    @Override
    public void push(E element) {
        throw notImplemented();
    }

    @Override
    public E pop() {
        throw notImplemented();
    }

    @Override
    public boolean remove(Object object) {
        throw notImplemented();
    }

    @Override
    public boolean contains(Object object) {
        throw notImplemented();
    }

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw notImplemented();
    }

    @Override
    public boolean isEmpty() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw notImplemented();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public void clear() {
        throw notImplemented();
    }
}
