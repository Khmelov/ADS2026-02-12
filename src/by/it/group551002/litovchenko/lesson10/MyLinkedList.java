package by.it.group551002.litovchenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E> {

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

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    private E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder res = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            res.append(curr.item);
            if (curr.next != null) {
                res.append(", ");
            }
            curr = curr.next;
        }
        res.append("]");
        return res.toString();
    }

    @Override
    public void addFirst(E e) {
        final Node<E> f = head;
        final Node<E> newNode = new Node<>(null, e, f);
        head = newNode;
        if (f == null) {
            tail = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(l, e, null);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int pos) {
        if (pos < 0 || pos >= size) {
            return null;
        }

        Node<E> curr = head;
        for (int i = 0; i < pos; i++) {
            curr = curr.next;
        }
        return unlink(curr);
    }

    @Override
    public E removeFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> curr = head; curr != null; curr = curr.next) {
            if (Objects.equals(o, curr.item)) {
                unlink(curr);
                return true;
            }
        }
        return false;
    }

    @Override
    public E pollFirst() {
        return removeFirst();
    }

    @Override
    public E pollLast() {
        return removeLast();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E getFirst() {
        return (head != null) ? head.item : null;
    }

    @Override
    public E getLast() {
        return (tail != null) ? tail.item : null;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public int size() {
        return size;
    }

    ////////////////////////////////////// Заглушки Deque
    @Override public E peekFirst() { return getFirst(); }
    @Override public E peekLast() { return getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { head = tail = null; size = 0; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
}