package by.it.group551001.kotsuba.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        head = 0;
        tail = 0;
        size = 0;
    }
    private void resize() {
        E[] newElements = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E element){
        addLast(element);
        return true;
    }
    @Override
    public void addFirst(E element){
        if(size==elements.length){
            resize();
        }
        head = (head -1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }
    @Override
    public void addLast(E element){
        if(size== elements.length) resize();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }
    @Override
    public E element(){
        return getFirst();
    }
    @Override
    public E getFirst(){
        if(size==0) return null;
        return elements[head];
    }
    @Override
    public E getLast(){
        if(size==0) return null;
        return elements[(tail-1+ elements.length) % elements.length];
    }
    @Override
    public E poll(){
        return pollFirst();
    }
    @Override
    public E pollFirst(){
        if(size==0) return null;
        E element = elements[head];
        elements[head] = null;
        head = (head+1) % elements.length;
        size--;
        return element;
    }
    @Override
    public E pollLast(){
        if(size==0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;


    }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public E peekFirst() { return getFirst(); }
    @Override public E peekLast() { return getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { size = 0; head = 0; tail = 0; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
}
