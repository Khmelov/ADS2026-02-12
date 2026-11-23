package by.it.group551004.pedko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private final int STD_SIZE = 64;
    private Node<E>[] table = (Node<E>[]) new Node[STD_SIZE];
    private int size = 0;

    private int findIndex(Object o) {
        return (o.hashCode() % table.length + table.length) % table.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        int hash = findIndex(e);

        if (table[hash] != null) {
            Node<E> curr;
            curr = table[hash];
            while (curr.next != null) {
                if (curr.value.equals(e)) {
                    return false;
                }
                curr = curr.next;
            }
            if (curr.value.equals(e)) {
                return false;
            }

            curr.next = new Node<>(e);
            curr.next.next = null;
        } else {
            table[hash] = new Node<>(e);
            table[hash].next = null;
        }

        size++;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;   // отпускаем цепочки для GC
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(Object o) {
        int index = findIndex(o);
        Node<E> curr = table[index];
        Node<E> prev = null;

        while (curr != null) {
            if (curr.value.equals(o)) {
                if (prev == null) {
                    table[index] = curr.next;   // удаляем голову цепочки
                } else {
                    prev.next = curr.next;      // вырезаем узел из середины/хвоста
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
        int index = (o.hashCode() % table.length + table.length) % table.length;
        for (Node<E> curr = table[index]; curr != null; curr = curr.next) {
            if (curr.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            for (Node<E> cur = bucket; cur != null; cur = cur.next) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(cur.value);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Iterator<E> iterator() { return null; }
}
