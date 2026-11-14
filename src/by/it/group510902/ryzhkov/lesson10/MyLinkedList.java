package by.it.group510902.ryzhkov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Objects;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        public Node(E data) {
            this.data = data;
        }
    }

    Node<E> head;
    Node<E> tail;
    int size = 0;

    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        Node<E> cur_node = head;
        while (cur_node != null) {
            builder.append(cur_node.data);
            if (cur_node != tail)
                builder.append(", ");

            cur_node = cur_node.next;
        }

        builder.append("]");
        return builder.toString();
    }

    public void addFirst(E element) {
        Node<E> newNode = new Node<E>(element);

        if (head != null) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            head = newNode;
            tail = newNode;
        }

        size++;
    }

    public void addLast(E element) {
        Node<E> newNode = new Node<E>(element);

        if (tail != null) {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        } else {
            head = newNode;
            tail = newNode;
        }

        size++;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        Node<E> rem_node = head;
        for (int i = 0; i < index; i++) {
            rem_node = rem_node.next;
        }

        E data = rem_node.data;
        if (rem_node.prev == null) {
            head = rem_node.next;
        } else {
            rem_node.prev.next = rem_node.next;
        }

        if (rem_node.next == null) {
            tail = rem_node.prev;
        } else {
            rem_node.next.prev = rem_node.prev;
        }

        size--;
        rem_node = null;
        return data;
    }

    public boolean remove(Object element) {
        Node<E> rem_node = head;
        while (rem_node != null) {
            if (Objects.equals(rem_node.data, element))
                break;

            rem_node = rem_node.next;
        }

        if (rem_node == null)
            return false;

        if (rem_node.prev == null) {
            head = rem_node.next;
        } else {
            rem_node.prev.next = rem_node.next;
        }

        if (rem_node.next == null) {
            tail = rem_node.prev;
        } else {
            rem_node.next.prev = rem_node.prev;
        }
        size--;

        rem_node = null;
        return true;
    }

    public int size() {
        return size;
    }

    public E getFirst() {
        return head.data;
    }

    public E getLast() {
        return tail.data;
    }

    public E element() {
        return getFirst();
    }

    public E pollFirst() {
        if (head == null)
            return null;
        E data = head.data;

        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return data;
    }

    public E pollLast() {
        if (tail == null)
            return null;
        E data = tail.data;

        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return data;
    }

    public E poll() {
        return pollFirst();
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
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
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
    public boolean contains(Object o) {
        return false;
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
        return null;
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
    public boolean containsAll(Collection<?> c) {
        return false;
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
}
