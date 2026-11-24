package by.it.group510901.chibisov.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node {
        Object value;
        Node next;
        Node prev;

        public Node(Object value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }
    private Node head;
    private Node tail;
    private int size;

    @SuppressWarnings("unchecked")
    private E unlink(Node current) {
        E old = (E) current.value;
        Node prev = current.prev;
        Node next = current.next;

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

        current.value = null;
        size--;

        return old;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node current = head;
        boolean isFirst = true;
        while (current != null) {
            if (!isFirst) sb.append(", ");
            sb.append(current.value);
            current = current.next;
            isFirst = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        head = new Node(e, head, null);
        if (head.next == null) {
            tail = head;
        } else {
            head.next.prev = head;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        tail = new Node(e, null, tail);
        if (tail.prev == null) {
            head = tail;
        } else {
            tail.prev.next = tail;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return (E) head.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return (E) tail.value;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        return unlink(tail);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node current;
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
        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        Node current = head;
        while (current != null && !(Objects.equals(o, current.value))) {
            current = current.next;
        }
        if (current == null) return false;
        unlink(current);
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void clear() {
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.value = null;
            current.next = current.prev = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        Node current = head;
        while (current != null) {
            if (Objects.equals(o, current.value)) return true;
            current = current.next;
        }
        return false;
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
    public boolean offer(E e) {
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
    public E remove() {
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

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
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
    public boolean containsAll(Collection<?> c) {
        return false;
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
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        Node cursor = head;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor == null) throw new NoSuchElementException();
            E e = (E) cursor.value;
            cursor = cursor.next;
            return e;
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}
