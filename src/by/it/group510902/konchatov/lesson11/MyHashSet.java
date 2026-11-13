package by.it.group510902.konchatov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private int capacity;

    public MyHashSet() {
        capacity = 16;
        table = (Node<E>[]) new Node[capacity];
        size = 0;
    }

    private int hash(Object o) {
    if (o == null) return 0;
    return Math.abs(o.hashCode()) % capacity;
    }

    private boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

   private void resize() {
    if (size < capacity * 0.75) return;   
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        resize();
        int i = hash(element);

        Node<E> cur = table[i];
        while (cur != null) {
            if (eq(cur.value, element)) {
                return false;
            }
            cur = cur.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = table[i];
        table[i] = newNode;
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
                if (prev == null) {
                    table[id] = cur.next;
                } else {
                    prev.next = cur.next;
                }
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
            if (eq(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                if (!first) sb.append(", ");
                sb.append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
}