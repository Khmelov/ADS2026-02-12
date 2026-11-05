package by.it.group510901.chibisov.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node {
        final int hash;
        Object value;
        Node next;
        Node before;
        Node after;

        public Node(Object value, int hash, Node next, Node before, Node after) {
            this.value = value;
            this.hash = hash;
            this.next = next;
            this.before = before;
            this.after = after;
        }
    }

    private Node[] table;
    private int size;
    private Node first;
    private Node last;

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY);
    }

    public MyLinkedHashSet(int initialCapacity) {
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
        Node current = first;
        boolean isFirst = true;
        while (current != null) {
            if (!isFirst) sb.append(", ");
            sb.append(current.value);
            current = current.after;
            isFirst = false;
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
        first = null;
        last = null;
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

        Node newNode = new Node(e, hash, table[idx], last, null);
        if (last == null) {
            first = newNode;
        } else {
            last.after = newNode;
        }
        last = newNode;
        table[idx] = newNode;
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
                if (current.before == null) {
                    first = current.after;
                } else {
                    current.before.after = current.after;
                }

                if (current.after == null) {
                    last = current.before;
                } else {
                    current.after.before = current.before;
                }

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

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) return false;
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean keepIfContains) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        Node current = first;
        while (current != null) {
            Node after = current.after;
            if (c.contains(current.value) != keepIfContains) {
                remove(current.value);
                modified = true;
            }
            current = after;
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
