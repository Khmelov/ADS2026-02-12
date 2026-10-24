package by.it.group551002.voroshko.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<E>[] table = new Node[10];
    private int size = 0;

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }
        return Math.abs(element.hashCode()) % table.length;
    }

    private boolean elementsEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 3 / 2];
        size = 0;

        for (Node<E> head : oldTable) {
            Node<E> current = head;

            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
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
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (size >= table.length * 0.75) {
            resize();
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (elementsEqual(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (elementsEqual(current.data, element)) {
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
    public boolean contains(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (elementsEqual(current.data, element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> head : table) {
            Node<E> current = head;
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
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }
}
