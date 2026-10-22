package by.it.group551003.parkhaniuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E item) {
            this.item = item;
        }
    }

    private int size = 0;
    private int capacity = 16;
    private Node<E>[] data;

    private Node<E> head = null;
    private Node<E> tail = null;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        data = (Node<E>[]) new Node[capacity];
    }

    private int getIndex(Object val) {
        if (val == null) {
            return 0;
        }
        return (val.hashCode() & 0x7FFFFFFF) % capacity;
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
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if ((double) size / capacity >= 0.75) {
            capacity *= 2;
            Node<E>[] newData = (Node<E>[]) new Node[capacity];

            Node<E> cur = head;
            while (cur != null) {
                cur.next = null;
                int index = getIndex(cur.item);

                if (newData[index] == null) {
                    newData[index] = cur;
                } else {
                    Node<E> p = newData[index];
                    while (p.next != null) {
                        p = p.next;
                    }
                    p.next = cur;
                }
                cur = cur.after;
            }
            data = newData;
        }
    }

    @Override
    public boolean add(E val) {
        if (contains(val)) {
            return false;
        }

        ensureCapacity();
        int index = getIndex(val);
        Node<E> newNode = new Node<>(val);

        if (data[index] == null) {
            data[index] = newNode;
        } else {
            Node<E> cur = data[index];
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = newNode;
        }

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object val) {
        int index = getIndex(val);
        Node<E> cur = data[index];
        Node<E> prev = null;

        while (cur != null) {
            if ((val == null && cur.item == null) || (val != null && val.equals(cur.item))) {

                if (prev == null) {
                    data[index] = cur.next;
                } else {
                    prev.next = cur.next;
                }

                if (cur.before != null) {
                    cur.before.after = cur.after;
                } else {
                    head = cur.after;
                }

                if (cur.after != null) {
                    cur.after.before = cur.before;
                } else {
                    tail = cur.before;
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
        int index = getIndex(o);
        Node<E> cur = data[index];
        while (cur != null) {
            if ((o == null && cur.item == null) || (o != null && o.equals(cur.item))) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;

        while (cur != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(cur.item);
            first = false;
            cur = cur.after;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            if (add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            if (remove(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;

        while (cur != null) {
            Node<E> nextNode = cur.after;
            if (!c.contains(cur.item)) {
                remove(cur.item);
                changed = true;
            }
            cur = nextNode;
        }

        return changed;
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
}