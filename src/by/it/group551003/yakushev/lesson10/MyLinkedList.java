package by.it.group551003.yakushev.lesson10;

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

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) { linkFirst(e); }

    @Override
    public void addLast(E e) { linkLast(e); }

    private void linkFirst(E e) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) last = newNode;
        else f.prev = newNode;
        size++;
    }

    private void linkLast(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) first = newNode;
        else l.next = newNode;
        size++;
    }

    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        if (prev == null) first = next;
        else { prev.next = next; x.prev = null; }

        if (next == null) last = prev;
        else { next.prev = prev; x.next = null; }

        x.item = null;
        size--;
        return element;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> x = first;
        for (int i = 0; i < index; i++) x = x.next;
        return unlink(x);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> x = first;
        while (x != null) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
            x = x.next;
        }
        return false;
    }

    @Override public E element() { return getFirst(); }
    @Override public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }
    @Override public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }
    @Override public E poll() { return pollFirst(); }
    @Override public E pollFirst() { return (first == null) ? null : unlink(first); }
    @Override public E pollLast() { return (last == null) ? null : unlink(last); }

    // Заглушки для полной реализации интерфейса Deque
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public E peekFirst() { return (first == null) ? null : first.item; }
    @Override public E peekLast() { return (last == null) ? null : last.item; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { first = last = null; size = 0; }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}