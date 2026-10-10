package by.it.group551004.kunikin.lesson11;

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

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    private int size = 0;

    private int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        return (h ^ (h >>> 16)) & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length << 1];
        for (Node<E> head : oldTable) {
            Node<E> curr = head;
            while (curr != null) {
                Node<E> next = curr.next;
                int index = hash(curr.data);
                curr.next = table[index];
                table[index] = curr;
                curr = next;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        int index = hash(o);
        Node<E> curr = table[index];
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
        int index = hash(e);
        Node<E> curr = table[index];
        while (curr != null) {
            if (e == null ? curr.data == null : e.equals(curr.data)) {
                return false;
            }
            curr = curr.next;
        }
        table[index] = new Node<>(e, table[index]);
        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> curr = table[index];
        Node<E> prev = null;
        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                if (prev == null) {
                    table[index] = curr.next;
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
        for (Node<E> head : table) {
            Node<E> curr = head;
            while (curr != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(curr.data);
                first = false;
                curr = curr.next;
            }
        }
        return sb.append("]").toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные методы (заглушки)              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
}
