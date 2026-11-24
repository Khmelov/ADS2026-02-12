package by.it.group551002.litovchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private int capacity = 16;
    private int size = 0;

    @SuppressWarnings("unchecked")
    private Node<E>[] elements = (Node<E>[]) new Node[capacity];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = o == null ? 0 : o.hashCode();
        int row = hash % capacity;
        Node<E> current = elements[row];
        while (current != null) {
            if (Objects.equals(current.value, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = e == null ? 0 : e.hashCode();
        int row = hash % capacity;

        Node<E> current = elements[row];
        while (current != null) {
            if (Objects.equals(current.value, e)) {
                return false;
            }
            current = current.next;
        }

        elements[row] = new Node<>(e, elements[row]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = o == null ? 0 : o.hashCode();
        int row = hash % capacity;
        Node<E> current = elements[row];

        if (current == null) {
            return false;
        }

        if (Objects.equals(current.value, o)) {
            elements[row] = current.next;
            size--;
            return true;
        }

        while (current.next != null) {
            if (Objects.equals(current.next.value, o)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node<E> current = elements[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append(']');
        return sb.toString();
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
    public <E> E[] toArray(E[] a) {
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
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}