package by.it.group551002.dyachkov.lesson11;

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

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];

    private Node<E> head;
    private Node<E> tail;

    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    public MyLinkedHashSet() {
    }

    private int getIndex(Object o, int tableLength) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % tableLength;
    }

    private void linkAtEnd(Node<E> node) {
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlink(Node<E> node) {
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

        node.before = null;
        node.after = null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        Node<E> current = head;
        while (current != null) {
            int newIndex = getIndex(current.data, newCapacity);

            current.next = newTable[newIndex];
            newTable[newIndex] = current;

            current = current.after;
        }

        table = newTable;
    }

    /// //////////////////////////////////////////////////////////////
    /// ///               Обязательные                        ///////
    /// /////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
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
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (e == null ? current.data == null : e.equals(current.data)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        linkAtEnd(newNode);

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                unlink(current);

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return true;
            }
            current = current.next;
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
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    /// //////////////////////////////////////////////////////////////////////
    /// ///               Опциональные                                 ///////
    /// //////////////////////////////////////////////////////////////////////

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
}


//slovar lolololololololololololololoololololololololololololololololololololololol