package by.it.group551003.kakhno.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] array;
    private int size;
    private int first;
    private int last;

    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayDeque() {
        array = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        first = 0;
        last = 0;
    }

    private void resize() {
        E[] newArray = (E[]) new Object[array.length * 2];

        for (int i = 0; i < size; i++)
            newArray[i] = array[(first + i) % array.length];

        array = newArray;
        first = 0;
        last = size;
    }

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            if (i > 0)
                result += ", ";

            result += array[(first + i) % array.length];
        }

        result += "]";

        return result;
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
        if (size == array.length)
            resize();

        first = (first - 1 + array.length) % array.length;
        array[first] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == array.length)
            resize();

        array[last] = element;
        last = (last + 1) % array.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return array[first];
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        int index = (last - 1 + array.length) % array.length;

        return array[index];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0)
            return null;

        E element = array[first];

        array[first] = null;

        first = (first + 1) % array.length;
        size--;

        if (size == 0) {
            first = 0;
            last = 0;
        }

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0)
            return null;

        last = (last - 1 + array.length) % array.length;

        E element = array[last];

        array[last] = null;

        size--;

        if (size == 0) {
            first = 0;
            last = 0;
        }

        return element;
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E removeFirst() {
        E element = pollFirst();

        if (element == null)
            throw new java.util.NoSuchElementException();

        return element;
    }

    @Override
    public E removeLast() {
        E element = pollLast();

        if (element == null)
            throw new java.util.NoSuchElementException();

        return element;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0)
            return null;

        return array[first];
    }

    @Override
    public E peekLast() {
        if (size == 0)
            return null;

        return getLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean offer(E element) {
        return offerLast(element);
    }

    @Override
    public boolean remove(Object element) {
        return removeFirstOccurrence(element);
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object element) {
        return false;
    }

    @Override
    public boolean contains(Object element) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            array[(first + i) % array.length] = null;

        size = 0;
        first = 0;
        last = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];

        for (int i = 0; i < size; i++)
            result[i] = array[(first + i) % array.length];

        return result;
    }

    @Override
    public <T> T[] toArray(T[] result) {
        for (int i = 0; i < size; i++)
            result[i] = (T) array[(first + i) % array.length];

        return result;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }
}