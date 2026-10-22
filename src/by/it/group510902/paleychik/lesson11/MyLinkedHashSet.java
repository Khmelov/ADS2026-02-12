package by.it.group510902.paleychik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> after;
        Node<E> before;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    private int getIndex(Object o, int length) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % length;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        Node<E> curr = head;
        while (curr != null) {
            int newIdx = getIndex(curr.data, newCapacity);
            curr.next = newTable[newIdx];
            newTable[newIdx] = curr;
            curr = curr.after;
        }
        table = newTable;
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
        int idx = getIndex(o, table.length);
        Node<E> curr = table[idx];
        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if ((float) (size + 1) / table.length >= LOAD_FACTOR) {
            resize();
        }
        int idx = getIndex(e, table.length);
        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;

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
    public boolean remove(Object o) {
        int idx = getIndex(o, table.length);
        Node<E> curr = table[idx];
        Node<E> prev = null;

        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                if (prev == null) {
                    table[idx] = curr.next;
                } else {
                    prev.next = curr.next;
                }

                if (curr.before == null) {
                    head = curr.after;
                } else {
                    curr.before.after = curr.after;
                }

                if (curr.after == null) {
                    tail = curr.before;
                } else {
                    curr.after.before = curr.before;
                }

                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
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
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> curr = head;
        while (curr != null) {
            Node<E> next = curr.after;
            if (!c.contains(curr.data)) {
                remove(curr.data);
                modified = true;
            }
            curr = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.after != null) {
                sb.append(", ");
            }
            curr = curr.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // оставшиеся методы Set
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}