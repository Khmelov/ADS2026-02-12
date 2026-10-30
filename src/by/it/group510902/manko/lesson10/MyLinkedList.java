package by.it.group510902.manko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            result.append(current.value);
            if (current.next != null) {
                result.append(", ");
            }
            current = current.next;
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // NOT an override: Deque has no remove(int). This is an extra helper.
    public E remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                unlink(current);
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
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.value;
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
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }

    // Helper: unlink a node from the list and return its value
    private E unlink(Node<E> node) {
        E value = node.value;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.value = null;
        size--;
        return value;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Deque           ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (head == null) {
            return null;
        }
        return head.value;
    }

    @Override
    public E peekLast() {
        if (tail == null) {
            return null;
        }
        return tail.value;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = tail;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.prev;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            result[i++] = current.value;
            current = current.next;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
        }
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            a[i++] = (T) current.value;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            addLast(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.value)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.value)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.value = null;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }
}