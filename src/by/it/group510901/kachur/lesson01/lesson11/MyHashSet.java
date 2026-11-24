package by.it.group510901.kachur.lesson01.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int indexFor(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return h & (buckets.length - 1);
    }

    private boolean equalsElem(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    @Override
    public boolean add(E e) {
        int idx = indexFor(e);
        Node<E> cur = buckets[idx];
        while (cur != null) {
            if (equalsElem(cur.value, e)) return false;
            cur = cur.next;
        }
        buckets[idx] = new Node<>(e, buckets[idx]);
        size++;
        if (size > buckets.length * LOAD_FACTOR) resize();
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = buckets;
        buckets = (Node<E>[]) new Node[old.length * 2];
        for (Node<E> head : old) {
            Node<E> cur = head;
            while (cur != null) {
                Node<E> next = cur.next;
                int newIdx = indexFor(cur.value);
                cur.next = buckets[newIdx];
                buckets[newIdx] = cur;
                cur = next;
            }
        }
    }

    @Override
    public boolean contains(Object o) {
        Node<E> cur = buckets[indexFor(o)];
        while (cur != null) {
            if (equalsElem(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o);
        Node<E> cur = buckets[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (equalsElem(cur.value, o)) {
                if (prev == null) buckets[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) buckets[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (Node<E> head : buckets) {
            Node<E> cur = head;
            while (cur != null) {
                if (!first) {
                    result = result + ", ";
                }
                result = result + cur.value;
                first = false;
                cur = cur.next;
            }
        }
        result = result + "]";
        return result;
    }

    // ===== Заглушки для остальных методов Set (не требуются по заданию) =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}
