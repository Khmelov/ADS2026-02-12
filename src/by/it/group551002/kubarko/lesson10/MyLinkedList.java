package by.it.group551002.kubarko.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;


public class MyLinkedList<E> implements Deque<E>{

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private int size = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    @Override
    public String toString() {
        if (first == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.item);
            current = current.next;
            if (current != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean add(E element)
    {
        if(element == null) throw new NullPointerException();

        if(size == 0) {
            Node<E> temp = new Node<E>(null, element, null);
            first = temp;
            last = first;
        }
        else{
            Node<E> temp = new Node<E>(last, element, null);
            last.next = temp;
            last = temp;
        }
        size++;
        return true;
    }


    @Override
    public void addFirst(E element){
        if(element == null) throw new NullPointerException();

        if(size == 0){
            Node<E> temp = new Node<E>(null, element, null);
            first = temp;
            last = first;
        }else{
            Node<E> temp = new Node<E>(null, element, first);
            first.prev = temp;
            first = temp;
        }
        size++;

    }

    @Override
    public void addLast(E element){
        add(element);
    }

    @Override
    public E element(){
        if(first == null) throw new NoSuchElementException("Deque is empty");
        return first.item;
    }

    @Override
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if(first == null) throw new NoSuchElementException("Deque is empty");
        return last.item;
    }

    @Override
    public E poll(){
        if(first == null ) return null;
        E element = first.item;

        Node<E> next = first.next;
        first.item = null;
        first.next = null;
        first = next;

        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;

        return element;
    }

    @Override
    public E pollFirst(){
        return poll();
    }

    @Override
    public E pollLast(){
        if(last == null ) return null;
        E element = last.item;

        Node<E> prev = last.prev;
        last.item = null;
        last.prev = null;
        last = prev;

        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;

        return element;
    }
    @Override
    public boolean isEmpty() {
        return first == null;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;

        if (index < (size >> 1)) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        E savedItem = current.item;

        if (current.prev == null) {
            first = current.next;
        } else {
            current.prev.next = current.next;
        }

        if (current.next == null) {
            last = current.prev;
        } else {
            current.next.prev = current.prev;
        }

        current.item = null;
        current.next = null;
        current.prev = null;

        size--;
        return savedItem;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        Node<E> current = first;
        while (current != null) {
            if (o.equals(current.item)) {
                if (current.prev == null) {
                    first = current.next;
                } else {
                    current.prev.next = current.next;
                }

                if (current.next == null) {
                    last = current.prev;
                } else {
                    current.next.prev = current.prev;
                }

                current.item = null;
                current.next = null;
                current.prev = null;

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        Node<E> current = first;
        while (current != null) {
            Node<E> next = current.next;
            current.item = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        first = null;
        last = null;
        size = 0;
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
    public E peekFirst() { return (first == null) ? null : first.item; }

    @Override
    public E peekLast() { return (first == null) ? null : last.item; }

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