package by.it.group510902.shust.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] map = (Node<E>[]) new Node[16];
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < map.length; i++) map[i] = null;
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

        map[index] = new Node<>(e, map[index]);
        size++;

        if (size > map.length * 0.75) resize();
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newMap = (Node<E>[]) new Node[map.length * 2];
        for (Node<E> node : map) {
            while (node != null) {
                Node<E> next = node.next;
                int hash = (node.data == null ? 0 : node.data.hashCode());
                int index = (hash & 0x7FFFFFFF) % newMap.length;
                node.next = newMap[index];
                newMap[index] = node;
                node = next;
            }
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
        boolean first = true;
        for (Node<E> node : map) {
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.data);
                first = false;
                node = node.next;
            }
        }
        return sb.append("]").toString();
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
}