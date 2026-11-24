package by.it.group510901.halavachou.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    private E unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        E value = node.data;
        node.data = null;
        size--;
        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> oldHead = head;
        Node<E> newNode = new Node<>(element, null, oldHead);
        head = newNode;
        if (oldHead == null) {
            tail = newNode;
        } else {
            oldHead.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> oldTail = tail;
        Node<E> newNode = new Node<>(element, oldTail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return unlink(current);
    }

    @Override
    public boolean remove(Object element) {
        for (Node<E> current = head; current != null; current = current.next) {
            if (Objects.equals(element, current.data)) {
                unlink(current);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        return sb.append("]").toString();
    }



    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst() != null ? getFirst() : throwNoSuch(); }
    @Override public E removeLast() { return pollLast() != null ? getLast() : throwNoSuch(); }
    @Override public E peekFirst() { return head == null ? null : head.data; }
    @Override public E peekLast() { return tail == null ? null : tail.data; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { head = tail = null; size = 0; }

    private E throwNoSuch() { throw new NoSuchElementException(); }
}