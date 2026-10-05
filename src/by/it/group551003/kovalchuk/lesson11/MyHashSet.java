package by.it.group551003.kovalchuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int capacity;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    public MyHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object o) {
        if (o == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        if ((float)(size + 1) / capacity > LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(o);

        Node<E> current = table[index];
        while (current != null) {
            if (current.data.equals(o)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>((E)o);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (current.data.equals(o)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    private int getIndex(Object o) {
        int hashCode = o.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = new Node[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;

                int newIndex = Math.abs(current.data.hashCode()) % newCapacity;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented for level A");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented for level A");
    }
}
