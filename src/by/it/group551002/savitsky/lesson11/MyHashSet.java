package by.it.group551002.savitsky.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] buckets;
    private int size;

    public MyHashSet() {
        buckets = new Node[1000];
        size = 0;
    }

    private int indexFor(Object o) {
        int hash = o.hashCode();
        return (hash & 0x7fffffff) % buckets.length;
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
    public void clear() {
        Arrays.fill(buckets, null);
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (contains(e)) {
            return false;
        }

        int index = indexFor(e);
        buckets[index] = new Node<>(e, buckets[index]);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o);
        Node<E> current = buckets[index];
        Node<E> prev = null;

        while (current != null) {

            if (current.item.equals(o)) {

                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }

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
        int index = indexFor(o);
        Node<E> current = buckets[index];
        while (current != null) {

            if (current.item.equals(o)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;

        for (Node<E> bucket : buckets) {

            Node<E> current = bucket;

            while (current != null) {

                if (!first) {
                    result = result + ", ";
                }

                result = result + current.item;
                first = false;
                current = current.next;
            }
        }

        return result + "]";
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
}