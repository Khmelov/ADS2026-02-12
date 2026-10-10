package by.it.group551004.zubko.lesson11;

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

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        for (Node<E> head : oldTable) {
            Node<E> curr = head;
            while (curr != null) {
                Node<E> next = curr.next;
                int idx = getIndex(curr.data);
                curr.next = table[idx];
                table[idx] = curr;
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
        int idx = getIndex(e);
        Node<E> curr = table[idx];
        while (curr != null) {
            if (e == null ? curr.data == null : e.equals(curr.data)) {
                return false;
            }
            curr = curr.next;
        }
        table[idx] = new Node<>(e, table[idx]);
        size++;
        if (size >= table.length * 0.75) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = getIndex(o);
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
    public boolean contains(Object o) {
        int idx = getIndex(o);
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
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Set              ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
