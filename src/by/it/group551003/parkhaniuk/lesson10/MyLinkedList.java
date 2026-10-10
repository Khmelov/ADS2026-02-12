package by.it.group551003.parkhaniuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private void unlink(Node<E> x) {
        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null)
            first = next;
        else
            prev.next = next;

        if (next == null)
            last = prev;
        else
            next.prev = prev;
        size--;
    }

    public static class Node<E> {
        E item;
        Node<E> next, prev;

        Node(Node<E> prev, E item, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    public String toString() {
        if (size == 0)
            return "[]";
        Node<E> cur = first;
        String res = "[" + cur.item;
        cur = cur.next;
        while (cur != null) {
            res += (", " + cur.item.toString());
            cur = cur.next;
        }
        res += "]";
        return res;
    }

    public boolean add (E element) {
        addLast(element);
        return true;
    }

    public E remove(int idx) {
        if (idx < 0 || idx >= size) return null;

        Node<E> cur = first;
        for (int i = 0; i < idx; i++) {
            cur = cur.next;
        }

        E item = cur.item;
        unlink(cur);
        return item;
    }


    @Override
    public boolean remove(Object o) {
        Node<E> cur = first;
        while (cur != null) {
            if ((o == null && cur.item == null) || (o != null && o.equals(cur.item))) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }


    public void addFirst(E element) {
        Node<E> prevF = first;
        Node<E> newNode = new Node<>(null, element, prevF);
        first = newNode;

        if (prevF != null)
            prevF.prev = newNode;
        else {
            last = newNode;
        }
        size++;
    }

    public void addLast(E element) {
        Node<E> prevL = last;
        Node<E> newNode = new Node<>(last, element, null);
        last = newNode;
        if (prevL != null)
            prevL.next = newNode;
        else
            first = newNode;
        size++;
    }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return first.item;
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return last.item;
    }

    public E pollFirst() {
        if (size == 0) return null;

        E pollItem = first.item;

        first = first.next;
        if (first == null)
            last = null;
        else
            first.prev = null;
        size--;

        return pollItem;
    }

    public E pollLast() {
        if (size == 0) return null;

        E pollItem = last.item;

        last = last.prev;
        if (last == null) {
            first = null;
        } else
            last.next = null;
        size--;
        return pollItem;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
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
    public Iterator<E> descendingIterator() {
        return null;
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
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
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
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
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
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    public E poll() {
        return pollFirst();
    }
}
