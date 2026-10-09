package by.it.group551002.liashkevich.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    static class Node {
        Object data;
        Node next;
        Node before;
        Node after;

        Node(Object object) {
            data = object;
            next = null;
            before = null;
            after = null;
        }
    }

    private int capacity = 16;
    private Node[] table = new MyLinkedHashSet.Node[capacity];
    private Node head;
    private Node tail;

    @Override
    public String toString() {
        boolean comma = false;
        String result = "[";
        Node current = head;
        while (current != null) {
            if (comma)
                result += ", ";
            result += String.valueOf(current.data);
            comma = true;
            current = current.after;
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
    public boolean isEmpty() {
        for (int i = 0; i < capacity; i++)
            if (table[i] != null)
                return false;
        return true;
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

    @Override
    public boolean add(E e) {
        int i = e.hashCode() % capacity;
        Node newNode = new Node(e);
        if (table[i] == null) {
            table[i] = newNode;
        }
        else {
            Node current = table[i];
            while ((current.next != null) && (!current.data.equals(newNode.data)))
                current = current.next;
            if (newNode.data.equals(current.data))
                return false;
            current.next = newNode;
        }
        if (head == null) {
            head = tail = newNode;
        }
        else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }
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
        if (current.before != null)
            current.before.after = current.after;
        else
            head = current.after;
        if (current.after != null)
            current.after.before = current.before;
        else
            tail = current.before;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Node current = head;
        int expected = 0;
        while (current != null) {
            if (c.contains(current.data))
                ++expected;
            current = current.after;
        }
        return expected == c.size();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int added = 0;
        for (E col : c) {
            this.add(col);
            ++added;
        }
        return added == c.size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Node current = head;
        boolean changed = false;
        while (current != null) {
            if (!c.contains(current.data)) {
                this.remove(current.data);
                changed = true;
            }
            current = current.after;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Node current = head;
        boolean changed = false;
        while (current != null) {
            if (c.contains(current.data))
                this.remove(current.data);
            current = current.after;
            changed = true;
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        head = tail = null;
    }
}
