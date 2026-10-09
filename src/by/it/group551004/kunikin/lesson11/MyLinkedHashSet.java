package by.it.group551004.kunikin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    private int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        return (h ^ (h >>> 16)) & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        table = (Node<E>[]) new Node[table.length << 1];
        Node<E> curr = head;
        while (curr != null) {
            int index = hash(curr.data);
            curr.next = table[index];
            table[index] = curr;
            curr = curr.after;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = hash(e);
        Node<E> curr = table[index];
        while (curr != null) {
            if (e == null ? curr.data == null : e.equals(curr.data)) {
                return false;
            }
            curr = curr.next;
        }
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> curr = table[index];
        Node<E> prev = null;
        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }

                if (curr.before != null) {
                    curr.before.after = curr.after;
                } else {
                    head = curr.after;
                }

                if (curr.after != null) {
                    curr.after.before = curr.before;
                } else {
                    tail = curr.before;
                }

                curr.before = null;
                curr.after = null;
                curr.next = null;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> curr = table[index];
        while (curr != null) {
            if (o == null ? curr.data == null : o.equals(curr.data)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные методы (заглушки)              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
