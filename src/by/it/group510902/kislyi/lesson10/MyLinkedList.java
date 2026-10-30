package by.it.group510902.kislyi.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
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

    // Обязательные методы

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
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

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head);
        if (head == null) {
            head = tail = newNode;
        } else {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }

        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }

        E value = cur.value;
        unlink(cur);
        return value;
    }


    public boolean removee(E element) {
        Node<E> cur = head;
        while (cur != null) {
            if (element == null ? cur.value == null : element.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E value = head.value;
        unlink(head);
        return value;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E value = tail.value;
        unlink(tail);
        return value;
    }

    private void unlink(Node<E> node) {
        Node<E> p = node.prev;
        Node<E> n = node.next;

        if (p == null) {
            head = n;
        } else {
            p.next = n;
            node.prev = null;
        }

        if (n == null) {
            tail = p;
        } else {
            n.prev = p;
            node.next = null;
        }

        node.value = null;
        size--;
    }

    //заглушки
    @Override public boolean offer(E e) { return add(e); }

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E remove() {
        E v = pollFirst();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override public E removeFirst() { return remove(); }

    @Override public E removeLast() {
        E v = pollLast();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override public E peek() { return peekFirst(); }

    @Override public E peekFirst() { return head == null ? null : head.value; }

    @Override public E peekLast() { return tail == null ? null : tail.value; }

    @Override public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E e = (E) o;
        return removee(e);
    }

    @Override public void push(E e) { addFirst(e); }

    @Override public E pop() { return removeFirst(); }

    @Override public boolean isEmpty() { return size == 0; }

    @Override public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.value = null;
            cur.prev = null;
            cur.next = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}