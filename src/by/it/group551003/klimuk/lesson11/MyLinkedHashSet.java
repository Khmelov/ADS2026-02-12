package by.it.group551003.klimuk.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    private Node<E>[] table;
    private int size;

    private Node<E> head;
    private Node<E> tail;


    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E item) {
            this.item = item;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
        head = null;
        tail = null;
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
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if (eq(current.item, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if (eq(current.item, e)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e);

        newNode.next = table[index];
        table[index] = newNode;

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        if (size > table.length * 0.75) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (eq(current.item, o)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                unlinkNode(current);

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    private void unlinkNode(Node<E> node) {
        Node<E> b = node.before;
        Node<E> a = node.after;

        if (b == null) {
            head = a;
        } else {
            b.after = a;
            node.before = null;
        }

        if (a == null) {
            tail = b;
        } else {
            a.before = b;
            node.after = null;
        }
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
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.item);
            first = false;
            current = current.after;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.after;
            if (!c.contains(current.item)) {
                remove(current.item);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return Math.abs(o.hashCode()) % table.length;
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        Node<E> current = head;
        while (current != null) {
            current.next = null;
            int index = getIndex(current.item);
            current.next = table[index];
            table[index] = current;
            current = current.after;
        }
    }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}