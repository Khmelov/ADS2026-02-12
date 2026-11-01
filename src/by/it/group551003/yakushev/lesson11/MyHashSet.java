package by.it.group551003.yakushev.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;

    private static class Node<E> {
        final int hash;
        final E item;
        Node<E> next;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int i = indexFor(h, table.length);

        for (Node<E> curr = table[i]; curr != null; curr = curr.next) {
            if (curr.hash == h && (curr.item == e || (e != null && e.equals(curr.item)))) {
                return false;
            }
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
            i = indexFor(h, table.length);
        }

        table[i] = new Node<>(h, e, table[i]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int i = indexFor(h, table.length);

        Node<E> prev = null;
        Node<E> curr = table[i];

        while (curr != null) {
            if (curr.hash == h && (curr.item == o || (o != null && o.equals(curr.item)))) {
                if (prev == null) table[i] = curr.next;
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
        int h = hash(o);
        int i = indexFor(h, table.length);

        for (Node<E> curr = table[i]; curr != null; curr = curr.next) {
            if (curr.hash == h && (curr.item == o || (o != null && o.equals(curr.item)))) {
                return true;
            }
        }
        return false;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        for (Node<E> node : table) {
            while (node != null) {
                Node<E> next = node.next;
                int i = node.hash & (newCapacity - 1);
                node.next = newTable[i];
                newTable[i] = node;
                node = next;
            }
        }
        table = newTable;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator();
    }

    private class HashSetIterator implements Iterator<E> {
        int index;
        Node<E> current;
        Node<E> lastReturned;

        HashSetIterator() {
            index = 0;
            while (index < table.length && table[index] == null) index++;
            if (index < table.length) current = table[index];
        }

        public boolean hasNext() { return current != null; }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = current;
            if (current.next != null) {
                current = current.next;
            } else {
                index++;
                while (index < table.length && table[index] == null) index++;
                current = (index < table.length) ? table[index] : null;
            }
            return lastReturned.item;
        }

        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            MyHashSet.this.remove(lastReturned.item);
            lastReturned = null;
        }
    }

    // Обязательные методы интерфейса Set (базовая реализация)
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node : table) {
            while (node != null) {
                result[i++] = node.item;
                node = node.next;
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;
        try { return containsAll(c); }
        catch (ClassCastException | NullPointerException unused) { return false; }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) if (e != null) h += e.hashCode();
        return h;
    }
}