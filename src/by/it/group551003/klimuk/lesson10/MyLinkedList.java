package by.it.group551003.klimuk.lesson10;

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException("Element cannot be null");
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
        if (e == null) throw new NullPointerException("Element cannot be null");
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
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return unlink(getNode(index));
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (Node<E> x = head; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

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
    public E element() {
        return getFirst();
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> x = head;
        for (int i = 0; i < size; i++) {
            sb.append(x.item);
            if (i < size - 1) {
                sb.append(", ");
            }
            x = x.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E removeFirst() {
        E x = pollFirst();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E removeLast() {
        E x = pollLast();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peekFirst() { return head == null ? null : head.item; }

    @Override
    public E peekLast() { return tail == null ? null : tail.item; }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) return false;
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public void clear() {
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (Node<E> x = head; x != null; x = x.next) {
            if (o.equals(x.item)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}