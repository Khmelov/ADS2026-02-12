package by.it.group510901.sinyak.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private Object[] elements;
    private int size = 0;
    private int head = 0;
    private int tail = 0;
    private double arrIncreaseKoef = 2.0;

    // Конструктор

    public MyArrayDeque() {

        elements = new Object[10];
    }

    // Методы

    private void increaseCapacity() {
        Object[] newEls = new Object[(int)(elements.length * arrIncreaseKoef)];
        int oldCapacity = elements.length;
        int curr = head;
        for (int i = 0; i < size; i++) {

            newEls[i] = elements[curr];
            curr++;

            if (curr == oldCapacity) {
                curr = 0;
            }
        }

        this.elements = newEls;
        tail = size;
        head = 0;
    }

    @Override
    public String toString() {

        String result = "[]";
        if (size > 0) {
            int curr = head;
            result = "[";

            for (int i = 0; i < size - 1; i++) {
                result += elements[curr] + ", ";
                curr++;
                if (curr == elements.length) curr = 0;
            }
            result += elements[curr] + "]";
        }
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

        if(element == null) throw new NullPointerException();
        if (size == elements.length) increaseCapacity();
        head--;
        if(head == -1) head = elements.length - 1;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {

        if (element == null) throw new NullPointerException();
        if (size == elements.length) increaseCapacity();

        elements[tail] = element;
        tail++;
        if(tail == elements.length) tail = 0;
        size++;
    }


    @Override
    public E element() {
        return getFirst();
    }


    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int index = tail - 1;
        if (index < 0) index = elements.length - 1;
        return (E) elements[index];
    }


    @Override
    public E poll() {

        return pollFirst();
    }

    @Override
    public E pollFirst() {

        if(size == 0) return null;

        E result = (E) elements[head];
        elements[head++] = null;
        if(head == elements.length) head = 0;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;

        tail--;
        if (tail == -1) tail = elements.length-1;

        E result = (E) elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    // Необязательные к реализации методы

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


}
