package by.it.group510901.poltorak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Емкость должна быть больше 0");
        }
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.size = 0;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int getIndex(int hash, int length) {
        return (length - 1) & hash;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            Node<E> e = table[i];
            if (e != null) {
                table[i] = null;
                do {
                    Node<E> next = e.next;
                    int iNew = getIndex(e.hash, newCapacity);
                    e.next = newTable[iNew];
                    newTable[iNew] = e;
                    e = next;
                } while (e != null);
            }
        }
        table = newTable;
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
    public void clear() {
        if (table != null && size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int index = getIndex(h, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == o || (o != null && o.equals(current.key)))) {
                return true;
            }
            current = current.next;
        }
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
    public boolean add(E e) {
        int h = hash(e);
        int index = getIndex(h, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == e || (e != null && e.equals(current.key)))) {
                return false;
            }
            current = current.next;
        }

        table[index] = new Node<>(h, e, table[index]);
        size++;

        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = getIndex(h, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == o || (o != null && o.equals(current.key)))) {
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

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
