package by.it.group551003.dobrolovich.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E item;
        Node<E> prev, next;
        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }
    private Node<E> first, last;
    private int size = 0;

    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = first;
        while (x != null) {
            sb.append(x.item);
            x = x.next;
            if (x != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public int size() { return size; }

    public boolean add(E element) { addLast(element); return true; }
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) last = newNode;
        else f.prev = newNode;
        size++;
    }
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) first = newNode;
        else l.next = newNode;
        size++;
    }

    public E element() { return getFirst(); }
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    public E poll() { return pollFirst(); }
    public E pollFirst() {
        if (first == null) return null;
        E e = first.item;
        Node<E> next = first.next;
        first.item = null;
        first.next = null;
        first = next;
        if (next == null) last = null;
        else next.prev = null;
        size--;
        return e;
    }
    public E pollLast() {
        if (last == null) return null;
        E e = last.item;
        Node<E> prev = last.prev;
        last.item = null;
        last.prev = null;
        last = prev;
        if (prev == null) first = null;
        else prev.next = null;
        size--;
        return e;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> x = first;
        for (int i = 0; i < index; i++) x = x.next;
        E e = x.item;
        Node<E> prev = x.prev;
        Node<E> next = x.next;
        if (prev == null) first = next;
        else { prev.next = next; x.prev = null; }
        if (next == null) last = prev;
        else { next.prev = prev; x.next = null; }
        x.item = null;
        size--;
        return e;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }
    private void unlink(Node<E> x) {
        Node<E> prev = x.prev;
        Node<E> next = x.next;
        if (prev == null) first = next;
        else { prev.next = next; x.prev = null; }
        if (next == null) last = prev;
        else { next.prev = prev; x.next = null; }
        x.item = null;
        size--;
    }

    // Заглушки
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    public void clear() { throw new UnsupportedOperationException(); }
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    public boolean isEmpty() { return size == 0; }
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    public boolean offer(E e) { return offerLast(e); }
    public boolean offerFirst(E e) { addFirst(e); return true; }
    public boolean offerLast(E e) { addLast(e); return true; }
    public E peek() { return peekFirst(); }
    public E peekFirst() { return first == null ? null : first.item; }
    public E peekLast() { return last == null ? null : last.item; }
    public E pop() { return removeFirst(); }
    public void push(E e) { addFirst(e); }
    public E remove() { return removeFirst(); }
    public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    public boolean removeFirstOccurrence(Object o) { return remove(o); }
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}