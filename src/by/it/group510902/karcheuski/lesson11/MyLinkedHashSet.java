package by.it.group510902.karcheuski.lesson11;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private int size;

    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        table = new Node[initialCapacity];
        size = 0;
    }

    private static class Node<E> {
        E element;
        int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    private int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        h = h ^ (h >>> 16);
        return h;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];

        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                Node<E> nextInBucket = current.next;

                int index = indexFor(current.hash, newCapacity);
                current.next = table[index];
                table[index] = current;

                current = nextInBucket;
            }
        }
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
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (o == null ? current.element == null : o.equals(current.element))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (e == null ? current.element == null : e.equals(current.element))) {
                return false;
            }
            current = current.next;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
            index = indexFor(hash, table.length);
        }

        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.before = tail;
            tail.after = newNode;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && (o == null ? current.element == null : o.equals(current.element))) {
                // Удаляем из бакета
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка
                unlink(current);

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    private void unlink(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
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
            Node<E> next = current.after;
            if (!c.contains(current.element)) {
                remove(current.element);
                modified = true;
            }
            current = next;
        }
        return modified;
    }


    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.element);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
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
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}