package by.it.group551004.podvitelskiymichael.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private void linkFirst(E element) {
        Node<E> node = new Node<>(element);
        node.next = head;
        if (head == null) {
            tail = node;
        } else {
            head.prev = node;
        }
        head = node;
        size++;
    }

    private void linkLast(E element) {
        Node<E> node = new Node<>(element);
        node.prev = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    private E unlink(Node<E> node) {
        E item = node.item;
        Node<E> before = node.prev;
        Node<E> after = node.next;

        if (before == null) {
            head = after;
        } else {
            before.next = after;
        }
        if (after == null) {
            tail = before;
        } else {
            after.prev = before;
        }

        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return item;
    }

    private Node<E> nodeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String result = "[";
        for (Node<E> n = head; n != null; n = n.next) {
            result += n.item;
            if (n.next != null) {
                result += ", ";
            }
        }
        return result + "]";
    }

    @Override
    public boolean add(E element) {
        linkLast(element);
        return true;
    }

    public E remove(int index) {
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        linkFirst(element);
    }

    @Override
    public void addLast(E element) {
        linkLast(element);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return head == null ? null : unlink(head);
    }

    @Override
    public E pollLast() {
        return tail == null ? null : unlink(tail);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (head != null) {
            unlink(head);
        }
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> n = head; n != null; n = n.next) {
            if (same(o, n.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return unlink(tail);
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> n = head; n != null; n = n.next) {
            if (same(o, n.item)) {
                unlink(n);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> n = tail; n != null; n = n.prev) {
            if (same(o, n.item)) {
                unlink(n);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}