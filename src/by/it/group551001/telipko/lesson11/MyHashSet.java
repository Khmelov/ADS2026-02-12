package by.it.group551001.telipko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E key;
        final int hash;
        Node<E> next;

        Node(E key, int hash, Node<E> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    private static int hashOf(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return h ^ (h >>> 16);
    }

    private static boolean same(Object a, Object b) {
        return Objects.equals(a, b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] nt = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> head : table) {
            Node<E> x = head;
            while (x != null) {
                Node<E> next = x.next;
                int i = x.hash & (nt.length - 1);
                x.next = nt[i];
                nt[i] = x;
                x = next;
            }
        }
        table = nt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(x.key);
                first = false;
            }
        }
        return sb.append(']').toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int h = hashOf(o);
        for (Node<E> x = table[h & (table.length - 1)]; x != null; x = x.next) {
            if (x.hash == h && same(o, x.key)) {
                return true;
            }
        }
        return false;
    }

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
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        int h = hashOf(e);
        int i = h & (table.length - 1);
        table[i] = new Node<>(e, h, table[i]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hashOf(o);
        int i = h & (table.length - 1);
        Node<E> prev = null;
        for (Node<E> x = table[i]; x != null; prev = x, x = x.next) {
            if (x.hash == h && same(o, x.key)) {
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
                }
                size--;
                return true;
            }
        }
        return false;
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