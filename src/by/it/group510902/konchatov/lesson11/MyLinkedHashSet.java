package by.it.group510902.konchatov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> after; 
        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private int capacity;
    private Node<E> head;
    private Node<E> tail;   

    public MyLinkedHashSet() {
        capacity = 16;
        table = (Node<E>[]) new Node[capacity];
        size = 0;
    }
    private int hash(Object o) {
        if (o == null) return 0;
        return Math.abs(o.hashCode() % capacity);
    }

    private boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void resize() {
        if (size < capacity * 3 / 4) return;
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int id=0;
                if(cur.value != null) id= Math.abs(cur.value.hashCode() % newCapacity);
                cur.next = newTable[id];
                newTable[id] = cur;
                cur = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private void linkLast(Node<E> n) {
        if (tail == null) {
            head = tail = n;
        } else {
            tail.after = n;
            tail = n;
        }
    }

    private void del(Node<E> n) {
        if (n == head && n == tail) {
            head = tail = null;
            return;
        }
        if (n == head) {
            head = n.after;
            return;
        }
        Node<E> prev = head;
        while (prev != null && prev.after != n) {
            prev = prev.after;
        }
        if (prev != null) {
            prev.after = n.after;
        }
        if (n == tail) {
            tail = prev;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.after;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        resize();
        int id = hash(element);
        Node<E> cur = table[id];
        while (cur != null) {
            if (eq(cur.value, element)) return false;
            cur = cur.next;
        }

        Node<E> n = new Node<>(element);
        n.next = table[id];
        table[id] = n;
        linkLast(n);             
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int id = hash(o);
        Node<E> cur = table[id];
        Node<E> prev = null;

        while (cur != null) {
            if (eq(cur.value, o)) {
                if (prev == null) table[id] = cur.next;
                else prev.next = cur.next;
                del(cur);
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
        int id= hash(o);
        Node<E> cur = table[id];
        while (cur != null) {
            if (eq(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
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
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}