package by.it.group510901.petsevich.lesson10;

import java.security.PrivateKey;
import java.util.*;

/*
toString()
size()

add(E element)
addFirst(E element)
addLast(E element)

element()
getFirst()
getLast()

poll()
pollFirst()
pollLast()
*/

public class MyArrayDeque<E> implements Deque<E>{

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] objects;

    private int front;
    private int rear;
    private int size;

    MyArrayDeque() {
        objects = new Object[DEFAULT_CAPACITY];
        front = rear = -1;
        size = 0;
    }

    @Override
    public void addFirst(E e) {
        if(size == objects.length)
            ensureCapacity();

        if(size == 0) front = rear = 0;
        else front = (front + objects.length - 1) % objects.length;
        objects[front] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if(size == objects.length)
            ensureCapacity();

        if(size == 0) front = rear = 0;
        else rear = (rear + 1) % objects.length;
        objects[rear] = e;
        size++;
    }

    @Override
    public E pollFirst() {
        if(size == 0)
            return null;

        size--;
        E object = (E)objects[front];
        objects[front] = null;
        if(size == 0) front = rear = -1;
        else front = (front + 1) % objects.length;

        return object;
    }

    @Override
    public E pollLast() {
        if(size == 0)
            return null;

        size--;
        E object = (E)objects[rear];
        objects[rear] = null;
        if(size == 0) front = rear = -1;
        else rear = (rear + objects.length - 1) % objects.length;

        return object;
    }

    @Override
    public E getFirst() {
        if(size == 0)
            throw new NoSuchElementException();
        return (E)objects[front];
    }

    @Override
    public E getLast() {
        if(size == 0)
            throw new NoSuchElementException();
        return (E)objects[rear];
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            objects[(i + front) % objects.length] = null;
        size = 0;
        front = rear = -1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(int i = 0; i < size; i++) {
            builder.append(objects[(i + front) % objects.length]);
            if(i != size - 1) builder.append(", ");
        }
        builder.append(']');
        return builder.toString();
    }

    private void ensureCapacity() {
        int capacity = objects.length;
        int newCapacity = 2 * capacity;
        Object[] bigger = new Object[newCapacity];

        for(int i = 0; i < size; i++)
            bigger[i] = objects[(i + front) % capacity];

        front = 0;
        rear = size - 1;
        objects = bigger;
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
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
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
    public void push(E e) {}

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
    public E removeLast() { return null; }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
