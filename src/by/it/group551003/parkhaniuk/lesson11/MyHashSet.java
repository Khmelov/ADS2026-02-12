package by.it.group551003.parkhaniuk.lesson11;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item) {
            this.item = item;
            this.next = null;
        }
    }

    private int size;
    private Node<E>[] data;
    private int capacity = 10;

    public MyHashSet() {
        data = (Node<E>[]) new Node[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty () {
        return size == 0;
    }


    private void rehash(Node<E>[] oldData, int oldCapacity) {
        size = 0;
        for(int i = 0; i < oldCapacity; i++) {
            if (oldData[i] != null)
                for (Node<E> cur = oldData[i]; cur != null; cur = cur.next) {
                    int code = cur.item.hashCode() % capacity;

                    if (data[code] == null) {
                        data[code] = new Node(cur.item);
                        size++;
                    } else {
                        if (appendVal(code, cur.item)){
                            size++;
                        }
                    }
                }
        }
    }

    private void ensureCapacity() {
        if ((double) size / capacity >= 0.75) {
            Node<E>[] oldData = (Node<E>[]) new Node[capacity];
            System.arraycopy(data, 0, oldData, 0, capacity);

            capacity *= 2;
            data = (Node<E>[]) new Node[capacity];

            rehash(oldData, capacity / 2);
        }
    }


    private boolean appendVal(int code, E val) {
        for(Node<E> cur = data[code]; (cur != null); cur = cur.next) {
            if (cur.item.equals(val)) {
                return false;
            }
            if (cur.next == null) {
                cur.next = new Node(val);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E val) {
        ensureCapacity();
        int code = val.hashCode() % capacity;
        boolean added = false;
        if (data[code] == null) {
            data[code] = new Node(val);
            added = true;
        } else {
            if (appendVal(code, val)){
                added = true;
            }
        }
        if (added)
            size++;

        return added;
    }

    @Override
    public boolean remove(Object val) {
        int code = val.hashCode() % capacity;
        boolean removed = false;
        if (data[code] == null)
            return false;

        if (val.equals(data[code].item)) {
            data[code] = data[code].next;
            size--;
            removed = true;
        } else
            for (Node<E> cur = data[code]; (cur != null); cur = cur.next)
                if (cur.next != null)
                    if (cur.next.item.equals(val)) {
                        cur.next = cur.next.next;
                        size--;
                        removed = true;
                    }

        return removed;
    }

    @Override
    public boolean contains(Object o) {
        int code = o.hashCode() % capacity;
        if (data[code] != null) {
            for (Node<E> cur = data[code]; cur != null; cur = cur.next) {
                if (cur.item.equals(o))
                    return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> head : data) {
            for (Node<E> cur = head; cur != null; cur = cur.next) {

                if (!first) {
                    sb.append(", ");
                }

                sb.append(cur.item);
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
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


