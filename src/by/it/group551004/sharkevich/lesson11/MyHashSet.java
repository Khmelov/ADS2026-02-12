package by.it.group551004.sharkevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 32;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashSet() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public boolean add(E e) {
        int index = indexFor(hash(e), table.length);
        Node<E> node = table[index];
        while (node != null) {
            if (node.value == e || (e != null && e.equals(node.value))) {
                return false;
            }
            node = node.next;
        }
        table[index] = new Node<>(e, table[index]);
        size++;
        if (size >= threshold) {
            resize();
        }
        return true;
    }

    public boolean remove(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        Node<E> prev = null;
        while (node != null) {
            if (node.value == o || (o != null && o.equals(node.value))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
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

    public boolean contains(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        while (node != null) {
            if (node.value == o || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
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

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ key.hashCode() >>> 16;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void resize() {
        int oldCapacity = table.length;
        if (oldCapacity == 1 << 30) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        int newCapacity = oldCapacity << 1;
        Node<E>[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(hash(node.value), newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
