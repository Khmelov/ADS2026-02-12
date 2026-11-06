package by.it.group510902.omelkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
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

    private Node<E>[] table = (Node<E>[]) new Node[10];
    private int size = 0;

    private int indexFor(Object o, int length) {
        if (o == null) return 0;
        return Math.abs(o.hashCode() % length);
    }

    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> n = table[i];
            while (n != null) {
                Node<E> next = n.next;
                int idx = indexFor(n.value, bigger.length);
                n.next = bigger[idx];
                bigger[idx] = n;
                n = next;
            }
        }
        table = bigger;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(n.value);
                first = false;
            }
        }
        return sb.append("]").toString();
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
    public boolean add(E e) {
        if (contains(e)) return false;
        if (size >= table.length * 3 / 4) resize();
        int idx = indexFor(e, table.length);
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length);
        Node<E> prev = null;
        for (Node<E> n = table[idx]; n != null; prev = n, n = n.next) {
            if (o == null ? n.value == null : o.equals(n.value)) {
                if (prev == null) {
                    table[idx] = n.next;
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
        int idx = indexFor(o, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o == null ? n.value == null : o.equals(n.value)) return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

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
