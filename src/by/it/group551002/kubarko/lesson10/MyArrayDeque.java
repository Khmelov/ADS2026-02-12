package by.it.group551002.kubarko.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;


public class MyArrayDeque<E> implements Deque<E>{

    private Object[] elementData= new Object[16];
    private int head = 0;
    private int tail = 0;

    private void grow()
    {
        int n = elementData.length;
        int right = n - head;
        int newCapacity = n << 1;

        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, deque too big");
        }

        Object[] temp = new Object[newCapacity];
        System.arraycopy(elementData,head,temp,0,right);
        System.arraycopy(elementData,0,temp,right,head);

        elementData = temp;
        head = 0;
        tail = n;

    }

    @Override
    public String toString() {
        if (head == tail) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int current = head;
        while (current != tail) {
            sb.append(elementData[current]);
            current = (current + 1) & elementData.length - 1;
            if (current != tail) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size(){
        return (tail-head) & (elementData.length-1);
    }

    @Override
    public boolean add(E element)
    {
        if(element == null) throw new NullPointerException();

        elementData[tail] = element;
        tail = (tail+1) & (elementData.length - 1);

        if(head == tail) grow();

        return true;
    }


    @Override
    public void addFirst(E element){
        if(element == null) throw new NullPointerException();

        head = (head-1) & (elementData.length - 1);
        elementData[head] = element;

        if(head == tail) grow();
    }

    @Override
    public void addLast(E element){
        add(element);
    }

    @Override
    public E element(){
        if(head == tail) throw new NoSuchElementException("Deque is empty");
        return (E)elementData[head];
    }

    @Override
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if(head == tail) throw new NoSuchElementException("Deque is empty");
        return (E)elementData[(tail - 1) & (elementData.length - 1)];
    }

    @Override
    public E poll(){
        if(head == tail ) return null;
        E element = (E) elementData[head];

        elementData[head] = null;
        head = (head+1) & (elementData.length-1);

        return element;
    }

    @Override
    public E pollFirst(){
        return poll();
    }

    @Override
    public E pollLast(){
        if(head == tail ) return null;

        tail = (tail-1) & (elementData.length-1);
        E element = (E) elementData[tail];
        elementData[tail] = null;

        return element;
    }
    @Override
    public boolean isEmpty() {
        return head == tail;
    }


    //Boilerplate functions
    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E removeFirst() { E x = pollFirst(); if (x == null) throw new NoSuchElementException(); return x; }

    @Override
    public E removeLast() { E x = pollLast(); if (x == null) throw new NoSuchElementException(); return x; }

    @Override
    public E peekFirst() { return (head == tail) ? null : (E) elementData[head]; }

    @Override
    public E peekLast() { return (head == tail) ? null : (E) elementData[(tail - 1) & (elementData.length - 1)]; }

    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public void clear() { head = 0; tail = 0; java.util.Arrays.fill(elementData, null); }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

}