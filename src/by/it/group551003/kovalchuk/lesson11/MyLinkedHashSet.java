package by.it.group551003.kovalchuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int capacity;

    private Node<E> head;
    private Node<E> tail;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prevInsert;
        Node<E> nextInsert;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prevInsert = null;
            this.nextInsert = null;
        }
    }

    public MyLinkedHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
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
        head = null;
        tail = null;
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

        if (contains(o)) {
            return false;
        }

        int index = getIndex(o);

        Node<E> newNode = new Node<>((E)o);

        newNode.next = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextInsert = newNode;
            newNode.prevInsert = tail;
            tail = newNode;
        }

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

                if (current.prevInsert != null) {
                    current.prevInsert.nextInsert = current.nextInsert;
                } else {
                    head = current.nextInsert;
                }

                if (current.nextInsert != null) {
                    current.nextInsert.prevInsert = current.prevInsert;
                } else {
                    tail = current.prevInsert;
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

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.nextInsert;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

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
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

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
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.nextInsert;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    private int getIndex(Object o) {
        int hashCode = o.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = new Node[newCapacity];

        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextInsert;

            int newIndex = Math.abs(current.data.hashCode()) % newCapacity;
            current.next = newTable[newIndex];
            newTable[newIndex] = current;

            current = next;
        }

        table = newTable;
        capacity = newCapacity;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
