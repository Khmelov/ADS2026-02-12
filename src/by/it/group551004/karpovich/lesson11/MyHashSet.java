package by.it.group551004.karpovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private static class Node<E> {
        E value;
        int hash;
        Node<E> next;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static int hashOf(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(cur.hash, newTable.length);
                cur.next = newTable[idx];
                newTable[idx] = cur;
                cur = next;
            }
        }
        table = newTable;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                if (!first) {
                    result += ", ";
                }
                result += cur.value;
                first = false;
                cur = cur.next;
            }
        }
        return result + "]";
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hashOf(o);
        Node<E> cur = table[indexFor(hash, table.length)];
        while (cur != null) {
            if (cur.hash == hash && same(o, cur.value)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean add(E element) {
        int hash = hashOf(element);
        int idx = indexFor(hash, table.length);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == hash && same(element, cur.value)) {
                return false;
            }
            cur = cur.next;
        }
        table[idx] = new Node<>(element, hash, table[idx]);
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hashOf(o);
        int idx = indexFor(hash, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == hash && same(o, cur.value)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
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
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (c.contains(cur.value)) {
                    if (prev == null) {
                        table[i] = cur.next;
                    } else {
                        prev.next = cur.next;
                    }
                    size--;
                    changed = true;
                } else {
                    prev = cur;
                }
                cur = cur.next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (!c.contains(cur.value)) {
                    if (prev == null) {
                        table[i] = cur.next;
                    } else {
                        prev.next = cur.next;
                    }
                    size--;
                    changed = true;
                } else {
                    prev = cur;
                }
                cur = cur.next;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int k = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                result[k++] = cur.value;
                cur = cur.next;
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> next = advance(null);
            private E last;
            private boolean canRemove = false;

            private Node<E> advance(Node<E> from) {
                if (from != null && from.next != null) {
                    return from.next;
                }
                while (bucket < table.length) {
                    Node<E> n = table[bucket++];
                    if (n != null) {
                        return n;
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
                last = next.value;
                next = advance(next);
                canRemove = true;
                return last;
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(last);
                canRemove = false;
            }
        };
    }
}