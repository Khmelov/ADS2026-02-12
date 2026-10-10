package by.it.group551002.liashkevich.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private int initialCapacity = 16;
    private E[] template = (E[]) new Object[initialCapacity];
    private int length = 0;
    private final double loadFactor = 0.75;

    @Override
    public String toString() {
        if (template == null)
            return "[]";
        String result = "[";
        boolean comma = false;
        for (int i = 0; i < length; i++) {
            if (comma)
                result += ", ";
            result += String.valueOf(template[i]);
            comma = true;
        }
        return result + ']';
    }

    @Override
    public void addFirst(E e) {
        if (((double) length / initialCapacity) >= loadFactor) {
            initialCapacity *= 2;
            E[] newTemplate = (E[]) new Object[initialCapacity];
            System.arraycopy(template, 0, newTemplate, 1, length);
            template = newTemplate;
        }
        else
            System.arraycopy(template, 0, template, 1, length);
        template[0] = e;
        ++length;
    }

    @Override
    public void addLast(E e) {
        if (((double) length / initialCapacity) >= loadFactor) {
            initialCapacity *= 2;
            E[] newTemplate = (E[]) new Object[initialCapacity];
            System.arraycopy(template, 0, newTemplate, 0, length);
            template = newTemplate;
        }
        template[length] = e;
        ++length;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        if (length == 0)
            return null;
        E toReturn = template[0];
        --length;
        System.arraycopy(template, 1, template, 0, length);
        template[length] = null;
        return toReturn;
    }

    @Override
    public E pollLast() {
        if (length == 0)
            return null;
        int lastIndex = (length - 1) % initialCapacity;
        E toReturn = template[lastIndex];
        template[lastIndex] = null;
        --length;
        return toReturn;
    }

    @Override
    public E getFirst() {
        if (length== 0)
            throw new NoSuchElementException("");
        return template[0];
    }

    @Override
    public E getLast() {
        if (length== 0)
            throw new NoSuchElementException("");
        return template[length - 1];
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        this.addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        if (length == 0)
            return null;
        E toReturn = template[0];
        --length;
        System.arraycopy(template, 1, template, 0, length);
        template[length] = null;
        return toReturn;
    }

    @Override
    public E element() {
        if (length == 0)
            throw new NoSuchElementException("");
        return template[0];
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}
