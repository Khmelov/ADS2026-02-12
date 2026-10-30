package by.it.group510902.kislyi.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> orderNext;
        Node<E> orderPrev;
        Node(E value) { this.value = value; }
    }

    private Node[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private final int capacity = 16;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[capacity];
    }

    private int hash(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return (h & 0x7fffffff) % capacity;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        table = new Node[capacity];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int idx = hash(e);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (Objects.equals(cur.value, e)) return false;
            cur = cur.next;
        }
        Node<E> node = new Node<>(e);
        node.next = table[idx];
        table[idx] = node;

        if (head == null) {
            head = tail = node;
        } else {
            tail.orderNext = node;
            node.orderPrev = tail;
            tail = node;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = hash(o);
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;

                if (cur.orderPrev != null) cur.orderPrev.orderNext = cur.orderNext;
                else head = cur.orderNext;
                if (cur.orderNext != null) cur.orderNext.orderPrev = cur.orderPrev;
                else tail = cur.orderPrev;

                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = hash(o);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (Objects.equals(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.orderNext;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.orderNext != null) sb.append(", ");
            cur = cur.orderNext;
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() { return null; }

    @Override
    public Object[] toArray() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }
}