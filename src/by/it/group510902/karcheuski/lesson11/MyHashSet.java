package by.it.group510902.karcheuski.lesson11;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        table = new Node[initialCapacity];
        size = 0;
    }

    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        h = h ^ (h >>> 16);
        return h;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        size = 0;

        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                add(current.element);
                current = current.next;
            }
        }
    }

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
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (e == null ? current.element == null : e.equals(current.element)) {
                return false;
            }
            current = current.next;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
            hash = hash(e);
            index = indexFor(hash, table.length);
        }

        table[index] = new Node<>(e, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.element);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
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