package by.it.group510902.karcheuski.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }


    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
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

    public E remove(int index) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E removed = node.value;
        unlink(node);
        return removed;
    }

    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(current.value, element)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
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

        if (p == null) head = n;
        else { p.next = n; node.prev = null; }

        if (n == null) tail = p;
        else { n.prev = p; node.next = null; }

        node.value = null;
        size--;
    }

    private Node<E> nodeAt(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }


    @Override public boolean offer(E e)                          { return false; }
    @Override public boolean offerFirst(E e)                     { return false; }
    @Override public boolean offerLast(E e)                      { return false; }

    @Override public E remove()                                  { return null; }
    @Override public E removeFirst()                             { return null; }
    @Override public E removeLast()                              { return null; }

    @Override public E peek()                                    { return null; }
    @Override public E peekFirst()                               { return null; }
    @Override public E peekLast()                                { return null; }

    @Override public boolean removeFirstOccurrence(Object o)     { return false; }
    @Override public boolean removeLastOccurrence(Object o)      { return false; }

    @Override public boolean contains(Object o)                  { return false; }
    @Override public boolean isEmpty()                           { return false; }
    @Override public void push(E e)                              { }
    @Override public E pop()                                     { return null; }

    @Override public Iterator<E> iterator()                      { return null; }
    @Override public Iterator<E> descendingIterator()            { return null; }

    @Override public Object[] toArray()                          { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a)                      { return a; }

    @Override public boolean containsAll(Collection<?> c)        { return false; }
    @Override public boolean addAll(Collection<? extends E> c)   { return false; }
    @Override public boolean removeAll(Collection<?> c)          { return false; }
    @Override public boolean retainAll(Collection<?> c)          { return false; }
    @Override public void clear()                                { }
}
