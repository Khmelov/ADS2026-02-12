package by.it.group551004.podvitelskiymichael.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E item;
        final int hash;
        Node<E> next;

        Node(E item, int hash, Node<E> next) {
            this.item = item;
            this.hash = hash;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    private static int hashOf(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    private static int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.hash, bigger.length);
                node.next = bigger[index];
                bigger[index] = node;
                node = next;
            }
        }
        table = bigger;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int hash = hashOf(element);
        int index = indexFor(hash, table.length);
        for (Node<E> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && same(element, n.item)) {
                return false;
            }
        }
        table[index] = new Node<>(element, hash, table[index]);
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hashOf(o);
        int index = indexFor(hash, table.length);
        Node<E> prev = null;
        for (Node<E> n = table[index]; n != null; prev = n, n = n.next) {
            if (n.hash == hash && same(o, n.item)) {
                if (prev == null) {
                    table[index] = n.next;
                } else {
                    prev.next = n.next;
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hashOf(o);
        for (Node<E> n = table[indexFor(hash, table.length)]; n != null; n = n.next) {
            if (n.hash == hash && same(o, n.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String result = "[";
        int printed = 0;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                result += n.item;
                if (++printed < size) {
                    result += ", ";
                }
            }
        }
        return result + "]";
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}