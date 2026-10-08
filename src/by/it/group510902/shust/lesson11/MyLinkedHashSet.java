package by.it.group510902.shust.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> after, before;
        Node(E data) { this.data = data; }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] map = (Node<E>[]) new Node[16];
    private Node<E> head, tail;
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < map.length; i++) map[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int hash = (e == null ? 0 : e.hashCode());
        int index = (hash & 0x7FFFFFFF) % map.length;
        Node<E> curr = map[index];

        while (curr != null) {
            if (e == curr.data || (e != null && e.equals(curr.data))) return false;
            curr = curr.next;
        }

        Node<E> newNode = new Node<>(e);
        newNode.next = map[index];
        map[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        if (size > map.length * 0.75) resize();
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newMap = (Node<E>[]) new Node[map.length * 2];
        Node<E> curr = head;
        while (curr != null) {
            int hash = (curr.data == null ? 0 : curr.data.hashCode());
            int index = (hash & 0x7FFFFFFF) % newMap.length;
            curr.next = newMap[index];
            newMap[index] = curr;
            curr = curr.after;
        }
        map = newMap;
    }

    @Override
    public boolean remove(Object o) {
        int hash = (o == null ? 0 : o.hashCode());
        int index = (hash & 0x7FFFFFFF) % map.length;
        Node<E> curr = map[index];
        Node<E> prev = null;

        while (curr != null) {
            if (o == curr.data || (o != null && o.equals(curr.data))) {
                if (prev == null) map[index] = curr.next;
                else prev.next = curr.next;

                if (curr.before != null) curr.before.after = curr.after;
                else head = curr.after;

                if (curr.after != null) curr.after.before = curr.before;
                else tail = curr.before;

                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int hash = (o == null ? 0 : o.hashCode());
        int index = (hash & 0x7FFFFFFF) % map.length;
        Node<E> curr = map[index];
        while (curr != null) {
            if (o == curr.data || (o != null && o.equals(curr.data))) return true;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.after != null) sb.append(", ");
            curr = curr.after;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> curr = head;
        while (curr != null) {
            Node<E> next = curr.after;
            if (!c.contains(curr.data)) {
                remove(curr.data);
                modified = true;
            }
            curr = next;
        }
        return modified;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}