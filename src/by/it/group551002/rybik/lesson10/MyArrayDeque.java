package by.it.group551002.rybik.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {


    private int capacity = 20;
    @SuppressWarnings("unchecked")
    private E[] array = (E[]) new Object[20];
    private int right = capacity-1, left = 0;
    private int size = 0;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = left - 1; i >= 0; i--) {
            sb.append(array[i]).append(", ");
        }

        for (int i = capacity - 1; i > right; i--) {
            sb.append(array[i]).append(", ");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (size == capacity || left > right) {
            int newCapacity = capacity * 2;
            @SuppressWarnings("unchecked")
            E[] newArray = (E[]) new Object[newCapacity];

            for (int i = 0; i < left; i++) {
                newArray[i] = array[i];
            }

            int rightCount = capacity - 1 - right;
            for (int i = 0; i < rightCount; i++) {
                newArray[newCapacity - 1 - i] = array[capacity - 1 - i];
            }

            right = newCapacity - 1 - rightCount;
            array = newArray;
            capacity = newCapacity;
        }

        array[left++] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == capacity || left > right) {
            int newCapacity = capacity * 2;
            @SuppressWarnings("unchecked")
            E[] newArray = (E[]) new Object[newCapacity];

            for (int i = 0; i < left; i++) {
                newArray[i] = array[i];
            }

            int rightCount = capacity - 1 - right;
            for (int i = 0; i < rightCount; i++) {
                newArray[newCapacity - 1 - i] = array[capacity - 1 - i];
            }

            right = newCapacity - 1 - rightCount;
            array = newArray;
            capacity = newCapacity;
        }

        array[right--] = e;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) return null;
        if (left > 0) {
            return array[left - 1];
        } else {
            return array[capacity - 1];
        }
    }

    @Override
    public E getLast() {
        if (size == 0) return null;
        if (right < capacity - 1) {
            return array[right + 1];
        } else {
            return array[0];
        }
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e;
        if (left > 0) {
            e = array[--left];
            array[left] = null;
        } else {
            e = array[capacity - 1];
            for (int i = capacity - 1; i > right + 1; i--) {
                array[i] = array[i - 1];
            }
            array[right + 1] = null;
            right++;
        }
        size--;
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E e;
        if (right < capacity - 1) {
            e = array[++right];
            array[right] = null;
        } else {
            e = array[0];
            for (int i = 0; i < left - 1; i++) {
                array[i] = array[i + 1];
            }
            array[--left] = null;
        }
        size--;
        return e;
    }

    /////////////////////////////////////////
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
