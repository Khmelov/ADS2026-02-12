package by.it.group510902.slesar.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
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

    private int size = 0;
    private Node<E> first;
    private Node<E> last;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = first;
        while (curr != null) {
            sb.append(curr.item);
            curr = curr.next;
            if (curr != null) sb.append(", ");
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

    @Override
    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) last = newNode;
        else f.prev = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) first = newNode;
        else l.next = newNode;
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> curr;
        if (index < (size >> 1)) {
            curr = first;
            for (int i = 0; i < index; i++) curr = curr.next;
        } else {
            curr = last;
            for (int i = size - 1; i > index; i--) curr = curr.prev;
        }
        return unlink(curr);
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (Objects.equals(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    private E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) first = next;
        else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) last = prev;
        else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return (first == null) ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return (last == null) ? null : unlink(last);
    }

    // --- Заглушки ---
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}