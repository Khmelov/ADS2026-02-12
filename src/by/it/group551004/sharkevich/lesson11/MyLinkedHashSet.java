package by.it.group551004.sharkevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 32;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before, after;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyLinkedHashSet() {
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
        head = null;
        tail = null;
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
                unlinkFromOrder(node);
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.after;
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
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
        Node<E> node = head;
        boolean first = true;
        while (node != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(node.value);
            first = false;
            node = node.after;
        }
        sb.append("]");
        return sb.toString();
    }

    private void unlinkFromOrder(Node<E> node) {
        Node<E> b = node.before;
        Node<E> a = node.after;
        if (b == null) {
            head = a;
        } else {
            b.after = a;
            node.before = null;
        }
        if (a == null) {
            tail= b;
        } else {
            a.before = b;
            node.after = null;
        }
    }
}
