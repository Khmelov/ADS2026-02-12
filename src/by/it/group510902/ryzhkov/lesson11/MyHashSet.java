package by.it.group510902.ryzhkov.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;

        public Node(E val) {
            this.value = val;
            this.next = null;
        }
    }

    private Node<E>[] map;
    private int size;

    private int hashFunction(E value) {
        return Math.abs(value.hashCode()) % this.map.length;
    }

    public MyHashSet() {
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
        for (int i = 0; i < this.map.length; i++) {
            Node<E> node = this.map[i];
            while (node != null) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append(node.value);
                first = false;
                node = node.next;
            }
        }
        builder.append(']');
        return builder.toString();
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

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
