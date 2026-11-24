package by.it.group551002.litovchenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque <E> implements Deque<E>{
    private Object[] elements = new Object[10];
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");
        if (size > 0){
            res.append(elements[0]);
        }
        for (int i = 1; i < size; i++){
            res.append(", ");
            res.append(elements[i]);
        }
        res.append("]");

        return res.toString();
    }

    @Override
    public boolean add(E e) {
        if (size >= elements.length){
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 0, size);
            elements = bigger;
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public void addLast(E e) {
        if (size >= elements.length){
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 0, size);
            elements = bigger;
        }
        elements[size] = e;
        size++;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public void addFirst(E e) {
        if (size >= elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 1, size);
            elements = bigger;
        } else {
            System.arraycopy(elements, 0, elements, 1, size);
        }
        elements[0] = e;
        size++;
    }

    @Override
    public E element(){
        if (size <= 0){
            return null;
        }
        return (E) elements[0];
    }

    @Override
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if (size <= 0){
            return null;
        }
        return (E) elements[size - 1];
    }

    @Override
    public E poll(){
        if (size <= 0){
            return null;
        }
        E res = (E) elements[0];
        for (int i = 0; i < size - 1; i++){
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;
        return res;
    }

    @Override
    public E pollLast(){
        if (size <= 0){
            return null;
        }
        E res = (E) elements[size - 1];
        elements[size - 1] = null;
        size--;
        return res;
    }

    @Override
    public E pollFirst() {
        return poll();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    //////////////////////////////////
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
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }

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

    @Override public boolean offer(E e) { return add(e); }
    @Override public void push(E e) { addFirst(e);}
    @Override public E pop() { return removeFirst(); }

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
}
