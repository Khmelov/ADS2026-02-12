package by.it.group510902.yaskel.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        final E item;
        final int hash;
        Node<E> next;

        Node(E item, int hash, Node<E> next) {
            this.item = item;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    // ---------- вспомогательные методы ----------

    private static int hash(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return h ^ (h >>> 16);
    }

    private static boolean same(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1); // длина всегда степень двойки
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int index = indexFor(current.hash, bigger.length);
                current.next = bigger[index];
                bigger[index] = current;
                current = next;
            }
        }
        table = bigger;
    }

    // ---------- обязательные методы ----------

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
        int h = hash(element);
        int index = indexFor(h, table.length);

        for (Node<E> current = table[index]; current != null; current = current.next) {
            if (current.hash == h && same(element, current.item)) {
                return false; // уже есть
            }
        }

        table[index] = new Node<>(element, h, table[index]);
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = indexFor(h, table.length);

        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == h && same(o, current.item)) {
                if (prev == null) {
                    table[index] = current.next;
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
        int h = hash(o);
        int index = indexFor(h, table.length);

        for (Node<E> current = table[index]; current != null; current = current.next) {
            if (current.hash == h && same(o, current.item)) {
                return true;
            }
        }
        return false;
    }

    // toString: формат как у обычной коллекции

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> current = table[i]; current != null; current = current.next) {
                if (!first) {
                    result += ", ";
                }
                result += current.item;
                first = false;
            }
        }
        return result + "]";
    }

    // остальные методы интерфейса

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> next = advance(null);

            // ищет следующий узел: либо дальше по цепочке, либо в следующей корзине
            private Node<E> advance(Node<E> current) {
                if (current != null && current.next != null) {
                    return current.next;
                }
                while (bucket < table.length) {
                    Node<E> head = table[bucket++];
                    if (head != null) {
                        return head;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                E item = next.item;
                next = advance(next);
                return item;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) {
            result[i++] = e;
        }
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    // не реализовано

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}