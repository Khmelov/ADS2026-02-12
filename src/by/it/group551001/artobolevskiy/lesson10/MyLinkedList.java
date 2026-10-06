package by.it.group551001.artobolevskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
        }
    }

    Node<E> head = null;
    Node<E> tail = null;
    int size = 0;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder s = new StringBuilder("[");
        Node<E> current = head;

        while (current != null) {
            s.append(current.data);

            if (current.next != null) {
                s.append(", ");
            }

            current = current.next;
        }

        s.append("]");
        return s.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override public E remove() {
        if (size == 0) {
            return null;
        }

        E element = head.data;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }

        size--;
        return element;
    }

    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if ((element == null && current.data == null) || (element != null && element.equals(current.data))) {

                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> NewNode = new Node<>(element);

        if (size == 0) {
            head = NewNode;
            tail = NewNode;
        } else {
            head.prev = NewNode;
            NewNode.next = head;
            head = NewNode;
        }

        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> NewNode = new Node<>(element);

        if (size == 0) {
            head = NewNode;
            tail = NewNode;
        } else {
            tail.next = NewNode;
            NewNode.prev = tail;
            tail = NewNode;
        }

        size++;
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }

        return head.data;
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if (size == 0) {
            return null;
        }

        return tail.data;
    }

    @Override
    public E poll() {
        return remove();
    }

    @Override
    public E pollFirst() {
        return remove();
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = tail.data;

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }

        size--;
        return element;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }

        size--;
        return current.data;
    }

    @Override public boolean isEmpty() { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }



    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peek() { return null; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return false; }
    @Override
    public Deque<E> reversed() {
        return Deque.super.reversed();
    }
}

