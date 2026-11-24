package by.it.group551002.kuzmenia.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private int indexFor(Object o) {
        return (o.hashCode() & 0x7fffffff) % table.length;
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
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        int ind = indexFor(e);
        Node<E> node = new Node<>(e);
        node.next = table[ind];
        table[ind] = node;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int ind = indexFor(o);
        Node<E> prev = null;
        for (Node<E> cur = table[ind]; cur != null; cur = cur.next) {
            if (o.equals(cur.value)) {
                if (prev != null) {
                    prev.next = cur.next;
                }
                else {
                    table[ind] = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int ind = indexFor(o);
        for (Node<E> cur = table[ind]; cur != null; cur = cur.next) {
            if (o.equals(cur.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> cur = table[i]; cur != null; cur = cur.next) {
                if (!first) {
                    result += ", ";
                }
                result += cur.value;
                first = false;
            }
        }
        return result + "]";
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
