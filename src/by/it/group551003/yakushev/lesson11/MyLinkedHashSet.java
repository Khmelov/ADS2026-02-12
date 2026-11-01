package by.it.group551003.yakushev.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head, tail;
    private int size;

    private static class Node<E> {
        final int hash;
        final E item;
        Node<E> next;      // для коллизий в хеш-таблице
        Node<E> before, after; // для порядка вставки

        Node(int hash, E item) {
            this.hash = hash;
            this.item = item;
        }
    }

    public MyLinkedHashSet() {
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

        Node<E> newNode = new Node<>(h, e);
        // Вставка в хеш-таблицу
        newNode.next = table[i];
        table[i] = newNode;

        // Вставка в список порядка
        if (tail != null) {
            tail.after = newNode;
        }
        newNode.before = tail;
        tail = newNode;
        if (head == null) head = newNode;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int i = indexFor(h, table.length);

        Node<E> prevHash = null;
        Node<E> currHash = table[i];

        while (currHash != null) {
            if (currHash.hash == h && (currHash.item == o || (o != null && o.equals(currHash.item)))) {
                // Удаляем из хеш-таблицы
                if (prevHash == null) table[i] = currHash.next;
                else prevHash.next = currHash.next;

                // Удаляем из списка порядка
                if (currHash.before != null) currHash.before.after = currHash.after;
                else head = currHash.after;

                if (currHash.after != null) currHash.after.before = currHash.before;
                else tail = currHash.before;

                size--;
                return true;
            }
            prevHash = currHash;
            currHash = currHash.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int i = indexFor(h, table.length);
        for (Node<E> curr = table[i]; curr != null; curr = curr.next) {
            if (curr.hash == h && (curr.item == o || (o != null && o.equals(curr.item)))) return true;
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
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.item);
            if (curr.after != null) sb.append(", ");
            curr = curr.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { table = new Node[DEFAULT_CAPACITY]; head = tail = null; size = 0; }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator<E> {
        Node<E> current = head;
        Node<E> lastReturned = null;

        public boolean hasNext() { return current != null; }
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = current;
            current = current.after;
            return lastReturned.item;
        }
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            MyLinkedHashSet.this.remove(lastReturned.item);
            lastReturned = null;
        }
    }

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
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) if (remove(e)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Копируем элементы, чтобы избежать проблем при удалении
        Object[] currentElements = new Object[size];
        Node<E> curr = head;
        int i = 0;
        while (curr != null) {
            currentElements[i++] = curr.item;
            curr = curr.after;
        }
        for (Object e : currentElements) {
            if (!c.contains(e)) {
                if (remove(e)) modified = true;
            }
        }
        return modified;
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;
        try { return containsAll(c); } catch (Exception unused) { return false; }
    }
    @Override public int hashCode() {
        int h = 0;
        for (E e : this) if (e != null) h += e.hashCode();
        return h;
    }
}