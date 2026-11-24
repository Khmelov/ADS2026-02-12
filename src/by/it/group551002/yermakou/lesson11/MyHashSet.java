package by.it.group551002.yermakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node {
        Object value;
        Node next;

        Node(Object value) {
            this.value = value;
        }
    }

    private Node[] table = new Node[16];
    private int size;

    private int index(Object o) {
        return o == null ? 0 : (o.hashCode() & 0x7fffffff) % table.length;
    }

    private boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        table = new Node[16];
        size = 0;
    }

    public boolean contains(Object o) {
        for (Node n = table[index(o)]; n != null; n = n.next) {
            if (eq(o, n.value)) return true;
        }
        return false;
    }

    public boolean add(E e) {
        if (contains(e)) return false;
        Node n = new Node(e);
        int i = index(e);
        n.next = table[i];
        table[i] = n;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int i = index(o);
        Node prev = null;
        for (Node n = table[i]; n != null; n = n.next) {
            if (eq(o, n.value)) {
                if (prev == null) table[i] = n.next;
                else prev.next = n.next;
                size--;
                return true;
            }
            prev = n;
        }
        return false;
    }

    public String toString() {
        String s = "[";
        boolean first = true;
        for (Node bucket : table) {
            for (Node n = bucket; n != null; n = n.next) {
                if (!first) s += ", ";
                s += n.value;
                first = false;
            }
        }
        return s + "]";
    }

    // не требуются по заданию
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}