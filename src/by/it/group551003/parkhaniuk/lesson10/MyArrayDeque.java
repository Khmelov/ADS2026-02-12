package by.it.group551003.parkhaniuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int size = 0;
    private int head = 0;
    private int tail = 0;

    public MyArrayDeque(){
        data = (E[])new Object[5];
    }

    public MyArrayDeque(int capacity){
        data = (E[])new Object[capacity];
    }
    @Override
    public String toString() {
        if (size == 0){
            return "[]";
        }
        String res = "[" + data[head];
        for(int i = 1; i < size; i++) {
            res += (", " + data[(i+head) % data.length].toString());
        }
        res += "]";

        return res;
    }
    @Override
    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            E[] newArr = (E[]) new Object[data.length * 2];
            for (int i = 0; i < size; i++)
                newArr[i] = data[(i + head) % data.length];

            data = newArr;
            head = 0;
            tail = size;
        }
    }
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }
    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }
    @Override
    public void addLast(E element) {
        ensureCapacity();
        data[tail] = element;
        tail = (tail + 1) % data.length;
        size++;
    }
    @Override
    public E getFirst() {
        E el = null;
        if (size == 0)
            throw new NoSuchElementException();
        return data[head];
    }
    @Override
    public E getLast() {
        E el = null;
        if (size == 0)
            throw new NoSuchElementException();
        return data[(tail - 1 + data.length) % data.length];
    }
    @Override
    public E poll() {
        return pollFirst();
    }
    @Override
    public E pollFirst() {
        if (size == 0)
            throw new NoSuchElementException();

        E el = data[head];
        data[head] = null;

        size--;
        head = (head + 1) % data.length;

        return el;
    }
    @Override
    public E pollLast() {
        if (size == 0)
            throw new NoSuchElementException();
        size--;

        tail = (tail - 1 + data.length) % data.length;
        E el = data[tail];
        data[tail] = null;

        return el;
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

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
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

    public E element() {
        return getFirst();
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


}
