package by.it.group510902.paleychik.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // =========================================================================
    // Обязательные методы из задания B
    // =========================================================================

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
        Node<E> oldHead = head;
        Node<E> newNode = new Node<>(null, element, oldHead);
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
        Node<E> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E data = head.data;
        Node<E> nextNode = head.next;
        head.data = null;
        head.next = null;
        head = nextNode;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return data;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E data = tail.data;
        Node<E> prevNode = tail.prev;
        tail.data = null;
        tail.prev = null;
        tail = prevNode;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return data;
    }

    // Удаление элемента по индексу
    public E remove(int index) {
        checkElementIndex(index);
        Node<E> targetNode = node(index);
        return unlink(targetNode);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> curr = head; curr != null; curr = curr.next) {
                if (curr.data == null) {
                    unlink(curr);
                    return true;
                }
            }
        } else {
            for (Node<E> curr = head; curr != null; curr = curr.next) {
                if (o.equals(curr.data)) {
                    unlink(curr);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) {
                sb.append(", ");
            }
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // =========================================================================
    // Вспомогательные закрытые методы
    // =========================================================================

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr;
        } else {
            Node<E> curr = tail;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
            return curr;
        }
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

    // =========================================================================
    // Необязательные методы
    // =========================================================================

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E remove() { return removeFirst(); }

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
    public E peek() { return peekFirst(); }

    @Override
    public E peekFirst() { return (head == null) ? null : head.data; }

    @Override
    public E peekLast() { return (tail == null) ? null : tail.data; }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public void clear() {
        while (pollFirst() != null);
    }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}