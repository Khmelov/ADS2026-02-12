package by.it.group551003.klimuk.lesson11;


import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private Node<E>[] table;
    private int size;

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
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
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if (eq(current.item, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if (eq(current.item, e)) {
                return false;
            }
            current = current.next;
        }

        table[index] = new Node<>(e, table[index]);
        size++;

        if (size > table.length * 0.75) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (eq(current.item, o)) {
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
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.item);
                first = false;
                current = current.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return Math.abs(o.hashCode()) % table.length;
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                add(current.item);
                    current = current.next;
            }
        }
    }




    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}