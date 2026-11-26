package by.it.group510901.chibisov.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node {
        final int hash;
        Object value;
        Node next;

        public Node(Object value, int hash, Node next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node[] table;
    private int size;

    public MyHashSet() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashSet(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException();
        table = new Node[initialCapacity];
    }

    private int hash(Object o) {
        int h;
        return (o == null) ? 0 : ((h = o.hashCode()) ^ (h >>> 16)) & 0x7FFFFFFF;
    }

    private void resize() {
        Node[] oldTable = table;
        int newLength = table.length << 1;
        Node[] newTable = new Node[newLength];
        for (Node node : oldTable) {
            while (node != null) {
                Node next = node.next;
                int newIdx = node.hash % newLength;
                node.next = newTable[newIdx];
                newTable[newIdx] = node;
                node = next;
            }
        }
        table = newTable;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean isFirst = true;
        for (Node node : table) {
            while (node != null) {
                if (!isFirst) sb.append(", ");
                sb.append(node.value);
                node = node.next;
                isFirst = false;
            }
        }
        sb.append(']');
        return sb.toString();
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
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int idx = hash % table.length;

        Node current = table[idx];
        while (current != null) {
            if (hash == current.hash && Objects.equals(e, current.value)) {
                return false;
            }
            current = current.next;
        }
        table[idx] = new Node(e, hash, table[idx]);
        size++;

        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int idx = hash % table.length;

        Node prev = null;
        Node current = table[idx];
        while (current != null) {
            if (hash == current.hash && Objects.equals(o, current.value)) {
                if (prev == null) {
                    table[idx] = current.next;
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
    public boolean contains(Object o) {
        int hash = hash(o);
        int idx = hash % table.length;

        Node current = table[idx];
        while (current != null) {
            if (hash == current.hash && Objects.equals(o, current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
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
    public Iterator<E> iterator() {
        return null;
    }
}
