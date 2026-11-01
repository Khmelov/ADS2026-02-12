package by.it.group510902.ryzhkov.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        public Node(E val) {
            this.value = val;
            this.next = null;
        }
    }

    private Node<E>[] map;
    private int size;
    private Node<E> head = null;
    private Node<E> tail = null;

    private int hashFunction(E value) {
        return Math.abs(value.hashCode()) % this.map.length;
    }

    public MyLinkedHashSet() {
        this.map = (Node<E>[]) new Node[10];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E value) {
        if (value == null)
            return false;
        int index = hashFunction(value);
        Node<E> node = new Node<E>(value);

        if (this.map[index] == null) {
            this.map[index] = node;
        } else {
            Node<E> cur = this.map[index];
            while (cur.next != null) {
                if (cur.value.equals(value)) {
                    return false;
                }
                cur = cur.next;
            }
            if (cur.value.equals(value)) {
                return false;
            }

            cur.next = node;
        }

        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }

        size++;
        return true;
    }

    public boolean remove(Object value_obj) {
        if (value_obj == null)
            return false;
        E value = (E) value_obj;
        int index = hashFunction(value);
        Node<E> node = this.map[index];

        if (node == null) {
            return false;
        } else {
            Node<E> prev = null;

            while (node != null) {
                if (node.value.equals(value)) {
                    if (prev == null) {
                        this.map[index] = node.next;
                    } else {
                        prev.next = node.next;
                    }

                    if (node.before != null) {
                        node.before.after = node.after;
                    } else {
                        head = node.after;
                    }
                    if (node.after != null) {
                        node.after.before = node.before;
                    } else {
                        tail = node.before;
                    }

                    node = null;
                    size--;
                    return true;
                }

                prev = node;
                node = node.next;
            }

            return false;
        }
    }

    public void clear() {
        for (int i = 0; i < this.map.length; i++) {
            this.map[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    public boolean contains(Object value_obj) {
        if (value_obj == null)
            return false;
        E value = (E) value_obj;
        int index = hashFunction(value);
        Node<E> node = this.map[index];

        if (node == null) {
            return false;
        } else {
            while (node != null) {
                if (node.value.equals(value)) {
                    return true;
                }
                node = node.next;
            }

            return false;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        boolean first = true;
        Node<E> node = head;
        while (node != null) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(node.value);
            first = false;
            node = node.after;
        }
        builder.append(']');
        return builder.toString();
    }

    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> nextNode = node.after;
            if (!c.contains(node.value)) {
                remove(node.value);
                modified = true;
            }
            node = nextNode;
        }
        return modified;
    }

    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
