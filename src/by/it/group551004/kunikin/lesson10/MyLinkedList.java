package by.it.group551004.kunikin.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first = null;
    private Node<E> last = null;
    private int size = 0;

    private E unlink(Node<E> node) {
        E element = node.data;
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = first;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) {
                sb.append(", ");
            }
            curr = curr.next;
        }
        return sb.append("]").toString();
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

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> curr;
        if (index < (size >> 1)) {
            curr = first;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        } else {
            curr = last;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
        }
        return unlink(curr);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> curr = first; curr != null; curr = curr.next) {
                if (curr.data == null) {
                    unlink(curr);
                    return true;
                }
            }
        } else {
            for (Node<E> curr = first; curr != null; curr = curr.next) {
                if (o.equals(curr.data)) {
                    unlink(curr);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlink(first);
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlink(last);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные методы (заглушки)              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E peek() { return null; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
}