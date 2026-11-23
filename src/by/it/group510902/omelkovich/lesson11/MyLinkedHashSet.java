package by.it.group510902.omelkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E>{

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[10];
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    private int indexFor(Object o, int length) {
        if (o == null) return 0;
        return Math.abs(o.hashCode() % length);
    }

    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> n = head; n != null; n = n.after) {
            int idx = indexFor(n.value, bigger.length);
            n.next = bigger[idx];
            bigger[idx] = n;
        }
        table = bigger;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> n = head; n != null; n = n.after) {
            if (n != head) sb.append(", ");
            sb.append(n.value);
        }
        return sb.append("]").toString();
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o == null ? n.value == null : o.equals(n.value)) return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        if (size >= table.length * 3 / 4) resize();
        Node<E> n = new Node<>(e);
        int idx = indexFor(e, table.length);
        n.next = table[idx];
        table[idx] = n;
        if (tail == null) {
            head = tail = n;
        } else {
            n.before = tail;
            tail.after = n;
            tail = n;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length);
        Node<E> prev = null;
        for (Node<E> n = table[idx]; n != null; prev = n, n = n.next) {
            if (o == null ? n.value == null : o.equals(n.value)) {
                if (prev == null) {
                    table[idx] = n.next;
                } else {
                    prev.next = n.next;
                }
                if (n.before == null) {
                    head = n.after;
                } else {
                    n.before.after = n.after;
                }
                if (n.after == null) {
                    tail = n.before;
                } else {
                    n.after.before = n.before;
                }
                size--;
                return true;
            }
        }
        return false;
    }

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
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        Node<E> n = head;
        while (n != null) {
            Node<E> nextNode = n.after;
            if (c.contains(n.value)) {
                remove(n.value);
                changed = true;
            }
            n = nextNode;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        Node<E> n = head;
        while (n != null) {
            Node<E> nextNode = n.after;
            if (!c.contains(n.value)) {
                remove(n.value);
                changed = true;
            }
            n = nextNode;
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

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
}
