package by.it.group510902.paleychik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;

    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private int getIndex(Object o, int length) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % length;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                Node<E> next = curr.next;
                int newIdx = getIndex(curr.data, newCapacity);
                curr.next = newTable[newIdx];
                newTable[newIdx] = curr;
                curr = next;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = getIndex(o, table.length);
        Node<E> curr = table[idx];
        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if ((float) (size + 1) / table.length >= LOAD_FACTOR) {
            resize();
        }
        int idx = getIndex(e, table.length);
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = getIndex(o, table.length);
        Node<E> curr = table[idx];
        Node<E> prev = null;

        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                if (prev == null) {
                    table[idx] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
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
            Node<E> curr = table[i];
            while (curr != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(curr.data);
                first = false;
                curr = curr.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //остальные методы интерфейса Set
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}