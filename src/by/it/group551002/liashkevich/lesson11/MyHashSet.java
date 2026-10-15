package by.it.group551002.liashkevich.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    static class Node {
        Object data;
        Node next;

        Node(Object object) {
            data = object;
            next = null;
        }
    }

    private int capacity = 16;
    private Node[] table = new MyHashSet.Node[capacity];

    public String toString() {
        boolean comma = false;
        String result = "[";
        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                if (comma)
                    result += ", ";
                result += String.valueOf(current.data);
                comma = true;
                current = current.next;
            }
        }
        result += "]";
        return result;
    }
    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                ++size;
                current = current.next;
            }
        }
        return size;
    }
    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
    }
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < capacity; i++)
            if (table[i] != null)
                return false;
        return true;
    }
    @Override
    public boolean add(E e) {
        int i = e.hashCode() % capacity;
        if (table[i] == null) {
            table[i] = new Node(e);
            return true;
        }
        Node newNode = new Node(e);
        Node current = table[i];
        while ((current.next != null) && (!current.data.equals(newNode.data)))
            current = current.next;
        if (newNode.data.equals(current.data))
            return false;
        current.next = newNode;
        return true;
    }
    @Override
    public boolean remove(Object o) {
        int i = o.hashCode() % capacity;
        Node current = table[i];
        Node prev = null;
        while (current != null && !o.equals(current.data)) {
            prev = current;
            current = current.next;
        }
        if (current == null)
            return false;
        Node temp = current.next;
        if (prev != null)
            prev.next = temp;
        else
            table[i] = current.next;
        return true;
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

    @Override
    public boolean contains(Object o) {
        int i = o.hashCode() % capacity;
        Node current = table[i];
        while (current != null) {
            if (o.equals(current.data))
                return true;
            current = current.next;
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
}
