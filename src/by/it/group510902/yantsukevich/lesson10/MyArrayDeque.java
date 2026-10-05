package by.it.group510902.yantsukevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E>{
    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size(){
        return size;
    }


    @Override
    public boolean add(E element){
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element){
        if(size == elements.length){
            E[] newElements = (E[]) new Object[elements.length * 2];
            newElements[0] = element;
            for (int i = 1; i < size; i++) {
                newElements[i] = elements[i-1];
            }
            elements = newElements;
            size++;
        }
        else {
            for(int i = size; i > 0; i--){
                elements[i] = elements[i-1];
            }
            elements[0] = element;
            size++;
        }
    }
    @Override
    public void addLast(E element){
        if(size == elements.length){
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            newElements[size] = element;
            elements = newElements;
            size++;
        }
        else {
            elements[size] = element;
            size++;
        }

    }
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[0];

        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[size - 1];
        elements[size - 1] = null;
        size--;

        return element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        return elements[0];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        return elements[size - 1];
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

    /*size()

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
}
