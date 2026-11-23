package by.it.group551003.dobrolovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        final int hash;
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
        table = (Node<E>[]) new Node[16];
    }

    private static int hash(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return h ^ (h >>> 16);
    }

    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private int index(int hash) {
        return hash & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = table;
        table = (Node<E>[]) new Node[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Node<E> n = old[i];
            while (n != null) {
                Node<E> next = n.next;
                int idx = index(n.hash);
                n.next = table[idx];
                table[idx] = n;
                n = next;
            }
        }
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
        int h = hash(o);
        for (Node<E> n = table[index(h)]; n != null; n = n.next) {
            if (n.hash == h && eq(n.value, o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        int h = hash(e);
        int idx = index(h);
        table[idx] = new Node<>(e, h, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = index(h);
        Node<E> prev = null;
        for (Node<E> n = table[idx]; n != null; prev = n, n = n.next) {
            if (n.hash == h && eq(n.value, o)) {
                if (prev == null) {
                    table[idx] = n.next;
                } else {
                    prev.next = n.next;
                }
                size--;
                return true;
            }
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
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> n = table[i];
            while (n != null) {
                if (!c.contains(n.value)) {
                    if (prev == null) {
                        table[i] = n.next;
                    } else {
                        prev.next = n.next;
                    }
                    size--;
                    changed = true;
                } else {
                    prev = n;
                }
                n = n.next;
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> next = null;

            {
                advance();
            }

            private void advance() {
                while (next == null && bucket < table.length) {
                    next = table[bucket++];
                }
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
                Node<E> r = next;
                next = r.next;
                if (next == null) {
                    advance();
                }
                return r.value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        int i = 0;
        for (E e : this) {
            res[i++] = e;
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (E e : this) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(e);
        }
        return sb.append(']').toString();
    }
}
