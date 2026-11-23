package by.it.group551003.kovalchuk.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[8]; // стартовая ёмкость
        head = 0;
        tail = 0;
        size = 0;
    }

    private int inc(int i) {
        return (i + 1) % data.length;
    }

    private int dec(int i) {
        return (i - 1 + data.length) % data.length;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int needed) {
        if (needed <= data.length) return;

        int newCap = data.length * 2;
        if (newCap < needed) newCap = needed;

        E[] newData = (E[]) new Object[newCap];

        for (int i = 0; i < size; i++) {
            int idx = (head + i) % data.length;
            newData[i] = data[idx];
        }

        data = newData;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        String s = "[";
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % data.length;
            if (i > 0) s += ", ";
            s += data[idx];
        }
        s += "]";
        return s;
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
        if (element == null) throw new NullPointerException("Null elements are not supported");
        ensureCapacity(size + 1);

        head = dec(head);
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException("Null elements are not supported");
        ensureCapacity(size + 1);

        data[tail] = element;
        tail = inc(tail);
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[dec(tail)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;

        E res = data[head];
        data[head] = null;
        head = inc(head);
        size--;
        return res;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;

        tail = dec(tail);
        E res = data[tail];
        data[tail] = null;
        size--;
        return res;
    }








    @Override public boolean isEmpty() { return size == 0;}
    @Override public boolean offerFirst(E e) { addFirst(e); return true;}
    @Override public boolean offerLast(E e) {addLast(e);return true;}
    @Override public E removeFirst() {E x = pollFirst();if (x == null) throw new NoSuchElementException();return x;}
    @Override public E removeLast() {E x = pollLast();if (x == null) throw new NoSuchElementException();return x;}
    @Override public E peekFirst() {return (size == 0) ? null : data[head];}
    @Override public E peekLast() {return (size == 0) ? null : data[dec(tail)];}
    @Override public boolean offer(E e) {return offerLast(e);}
    @Override public E remove() {return removeFirst();}
    @Override public E peek() {return peekFirst();}
    @Override public void push(E e) {addFirst(e);}
    @Override public E pop() {return removeFirst();}
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }


}