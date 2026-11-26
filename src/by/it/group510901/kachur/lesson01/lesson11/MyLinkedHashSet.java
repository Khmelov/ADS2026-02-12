package by.it.group510901.kachur.lesson01.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        E value;
        Node<E> next;    // цепочка коллизий
        Node<E> before;  // порядок
        Node<E> after;   // порядок

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] buckets;
    private int size;
    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
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
        Node<E> node = new Node<>(e, buckets[idx]);
        buckets[idx] = node;

        if (tail == null) {
            head = node;
            tail = node;
        } else {
            node.before = tail;
            tail.after = node;
            tail = node;
        }

        size++;
        if (size > buckets.length * LOAD_FACTOR) resize();
        return true;
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
                // 1) из цепочки коллизий
                if (prev == null) buckets[idx] = cur.next;
                else prev.next = cur.next;

                // 2) из списка порядка
                Node<E> b = cur.before;
                Node<E> a = cur.after;
                if (b == null) head = a; else b.after = a;
                if (a == null) tail = b; else a.before = b;

                cur.next = null;
                cur.before = null;
                cur.after = null;

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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        Node<E> cur = head;
        while (cur != null) {
            if (!first) result = result + ", ";
            result = result + cur.value;
            first = false;
            cur = cur.after;
        }
        return result + "]";
    }

    // ===== Методы с Collection =====

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = buckets;
        buckets = (Node<E>[]) new Node[old.length * 2];
        for (Node<E> bucketHead : old) {
            Node<E> cur = bucketHead;
            while (cur != null) {
                Node<E> next = cur.next;
                int newIdx = indexFor(cur.value);
                cur.next = buckets[newIdx];
                buckets[newIdx] = cur;
                cur = next;
            }
        }
    }

    // ===== Заглушки для оставшихся методов Set =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}