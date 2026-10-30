package by.it.group551002.yorsh.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        Node<E> h = head;
        Node<E> newNode = new Node<>(null, e, h);
        head = newNode;
        if (h == null) {
            tail = newNode;
        } else {
            h.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> t = tail;
        Node<E> newNode = new Node<>(t, e, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E item = head.item;
        Node<E> next = head.next;
        head.item = null;
        head.next = null;
        head = next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return item;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E item = tail.item;
        Node<E> prev = tail.prev;
        tail.item = null;
        tail.prev = null;
        tail = prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return item;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        E item = current.item;
        Node<E> prev = current.prev;
        Node<E> next = current.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            current.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            current.next = null;
        }

        current.item = null;
        size--;
        return item;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                Node<E> prev = current.prev;
                Node<E> next = current.next;

                if (prev == null) {
                    head = next;
                } else {
                    prev.next = next;
                    current.prev = null;
                }

                if (next == null) {
                    tail = prev;
                } else {
                    next.prev = prev;
                    current.next = null;
                }

                current.item = null;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Заглушки с правильными сигнатурами методов Deque
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
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { head = tail = null; size = 0; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
}