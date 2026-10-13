package by.it.group510901.chernyavskaya.lesson11;

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

    private Node<E>[] buckets = (Node<E>[]) new Node[16];
    private int size = 0;

    private int getIndex(Object o) {
        return o == null ? 0 : Math.abs(o.hashCode() % buckets.length);
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
        int idx = getIndex(o);
        for (Node<E> n = buckets[idx]; n != null; n = n.next) {
            if (o == null ? n.data == null : o.equals(n.data)) return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int idx = getIndex(e);
        for (Node<E> n = buckets[idx]; n != null; n = n.next) {
            if (e == null ? n.data == null : e.equals(n.data)) return false;
        }
        buckets[idx] = new Node<>(e, buckets[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = getIndex(o);
        Node<E> prev = null;
        for (Node<E> n = buckets[idx]; n != null; prev = n, n = n.next) {
            if (o == null ? n.data == null : o.equals(n.data)) {
                if (prev == null) buckets[idx] = n.next;
                else prev.next = n.next;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) buckets[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : buckets) {
            for (Node<E> n = bucket; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(n.data);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    // --- Остальные методы интерфейса Set (заглушки для компиляции) ---
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}