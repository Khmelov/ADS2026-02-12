package by.it.group510901.sinyak.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private Node tail;
    private Node head;
    private int size = 0;

    private class Node {

        private E data;
        private Node left;
        private Node right;

        private Node(Node l, E d, Node r) {

            this.left = l;
            this.data = d;
            this.right = r;
        }
    }


    // Методы

    public E remove(int index) {

        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node curr = head;

        for (int i = 0; i < index; i++) {
            curr = curr.right;
        }

        E data = curr.data;
        Node prev = curr.left;
        Node next = curr.right;

        if (prev == null) {
            head = next;
        } else {
            prev.right = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.left = prev;
        }

        curr.right = null;
        curr.left = null;
        curr.data = null;

        size--;
        return data;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException("Список пуст");
        return pollFirst();
    }

    @Override
    public boolean remove(Object o) {

        Node curr = head;

        while (curr != null) {
            if (java.util.Objects.equals(curr.data, o)) {

                Node prev = curr.left;
                Node next = curr.right;

                if (prev == null) {
                    head = next;
                } else {
                    prev.right = next;
                }

                if (next == null) {
                    tail = prev;
                } else {
                    next.left = prev;
                }

                curr.right = null;
                curr.left = null;
                curr.data = null;

                size--;
                return true;
            }

            curr = curr.right;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        String result = "[]";

        Node curr = head;

        if (size > 0) {
            result = "[";
            while (curr.right != null) {
                result += curr.data + ", ";
                curr = curr.right;
            }
            result += curr.data + "]";
        }
        return result;
    }

    @Override
    public void addFirst(E e) {

        if (e == null) throw new NullPointerException();

        Node oldHead = head;
        Node node = new Node(null, e, head);
        head = node;

        if (size == 0) {
            tail = head;
        } else {
            oldHead.left = head;
        }

        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();

        Node oldTail = tail;
        Node node = new Node (tail, e, null);
        tail = node;

        if (size == 0) {
            head = tail;
        } else {
            oldTail.right = tail;
        }

        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Список пуст");
        return head.data;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Список пуст");
        return tail.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E data = head.data;

        head = head.right;
        if(head == null) {
            tail = null;
        }
        else {
            head.left = null;
        }

        size--;
        return data;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E data = tail.data;

        tail = tail.left;

        if(tail == null) {
            head = null;
        }
        else {
            tail.right = null;
        }

        size--;
        return data;
    }

    // Необязательные к реализации методы

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
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
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
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
}
