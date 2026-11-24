package by.it.group551001.artobolevskiy.lesson11;

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

    private Node<E>[] table;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
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
    public boolean add(E e) {
        int idx = getIndex(e);

        Node<E> current = table[idx];
        while (current != null) {
            if (e.equals(current.value)) {
                return false;
            }
            current = current.next;
        }

        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    int getIndex(Object o) {
        return Math.abs(o.hashCode()) % table.length;
    }

    @Override
    public boolean remove(Object o) {
        int idx = getIndex(o);
        Node<E> curr = table[idx];
        Node<E> prev = null;

        while (curr != null) {
            if (o.equals(curr.value)) {
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
            if (o.equals(curr.value)) {
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
        StringBuilder s = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) {
                    s.append(", ");
                }
                s.append(current.value);
                first = false;
                current = current.next;
            }
        }
        return s.append("]").toString();
    }


    /////////////////////////////////////////////////////////////////////////
    //////          Остальные методы интерфейса Set (заглушки)        ///////
    /////////////////////////////////////////////////////////////////////////

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
