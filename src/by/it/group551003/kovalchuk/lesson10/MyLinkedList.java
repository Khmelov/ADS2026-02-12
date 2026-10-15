package by.it.group551003.kovalchuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    private E unlink(Node<E> x) {
        final E element = x.data;
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

        x.data = null;
        size--;
        return element;
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
        final Node<E> f = head;
        final Node<E> newNode = new Node<>(element, null, f);
        head = newNode;
        if (f == null) {
            tail = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        final Node<E> t = tail;
        final Node<E> newNode = new Node<>(element, t, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node<E> target;
        if (index < (size >> 1)) {
            target = head;
            for (int i = 0; i < index; i++) {
                target = target.next;
            }
        } else {
            target = tail;
            for (int i = size - 1; i > index; i--) {
                target = target.prev;
            }
        }
        return unlink(target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = head; x != null; x = x.next) {
                if (o.equals(x.data)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
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

    @Override
    public String toString() {
        if (size == 0) {
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
        sb.append("]");
        return sb.toString();
    }










    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peekFirst() { return isEmpty() ? null : getFirst(); }
    @Override public E peekLast() { return isEmpty() ? null : getLast(); }
    @Override public E peek() { return peekFirst(); }

    @Override
    public E removeFirst() {
        E res = pollFirst();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E removeLast() {
        E res = pollLast();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override public E remove() { return removeFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}