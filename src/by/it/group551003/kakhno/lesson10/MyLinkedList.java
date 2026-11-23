package by.it.group551003.kakhno.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private class Node {
        private E data;
        private Node next;
        private Node prev;

        private Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node first;
    private Node last;
    private int size;

    @Override
    public String toString() {
        String result = "[";
        Node current = first;
        int i = 0;

        while (current != null) {
            if (i > 0)
                result += ", ";

            result += current.data;

            current = current.next;
            i++;
        }

        result += "]";

        return result;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        Node current;

        if (index < size / 2) {
            current = first;

            for (int i = 0; i < index; i++)
                current = current.next;
        } else {
            current = last;

            for (int i = size - 1; i > index; i--)
                current = current.prev;
        }

        E element = current.data;

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            first = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            last = current.prev;
        }

        size--;

        return element;
    }

    @Override
    public boolean remove(Object element) {
        Node current = first;

        while (current != null) {
            if (element == null && current.data == null ||
                    element != null && element.equals(current.data)) {

                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;

                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node node = new Node(element);

        if (size == 0) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }

        size++;
    }

    @Override
    public void addLast(E element) {
        Node node = new Node(element);

        if (size == 0) {
            first = node;
            last = node;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }

        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return first.data;
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0)
            return null;

        E element = first.data;

        first = first.next;

        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }

        size--;

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0)
            return null;

        E element = last.data;

        last = last.prev;

        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }

        size--;

        return element;
    }

    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E removeFirst() {
        E element = pollFirst();

        if (element == null)
            throw new java.util.NoSuchElementException();

        return element;
    }

    @Override
    public E removeLast() {
        E element = pollLast();

        if (element == null)
            throw new java.util.NoSuchElementException();

        return element;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0)
            return null;

        return first.data;
    }

    @Override
    public E peekLast() {
        if (size == 0)
            return null;

        return last.data;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean offer(E element) {
        return offerLast(element);
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        Node current = first;

        while (current != null) {
            if (element == null && current.data == null ||
                    element != null && element.equals(current.data)) {

                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;

                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object element) {
        Node current = last;

        while (current != null) {
            if (element == null && current.data == null ||
                    element != null && element.equals(current.data)) {

                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;

                return true;
            }

            current = current.prev;
        }

        return false;
    }

    @Override
    public boolean contains(Object element) {
        Node current = first;

        while (current != null) {
            if (element == null && current.data == null ||
                    element != null && element.equals(current.data))
                return true;

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];

        Node current = first;
        int i = 0;

        while (current != null) {
            result[i] = current.data;
            current = current.next;
            i++;
        }

        return result;
    }

    @Override
    public <T> T[] toArray(T[] result) {
        Node current = first;
        int i = 0;

        while (current != null && i < result.length) {
            result[i] = (T) current.data;
            current = current.next;
            i++;
        }

        return result;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection)
            if (!contains(element))
                return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;

        for (E element : collection) {
            addLast(element);
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;

        Node current = first;

        while (current != null) {
            Node next = current.next;

            if (collection.contains(current.data)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;
                changed = true;
            }

            current = next;
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;

        Node current = first;

        while (current != null) {
            Node next = current.next;

            if (!collection.contains(current.data)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;
                changed = true;
            }

            current = next;
        }

        return changed;
    }
}