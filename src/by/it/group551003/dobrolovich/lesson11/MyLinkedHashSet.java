package by.it.group551003.dobrolovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        final int hash;
        Node<E> next;      // цепочка коллизий (односвязный список)
        Node<E> before;    // порядок добавления
        Node<E> after;

        Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
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
        head = null;
        tail = null;
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
        Node<E> n = new Node<>(e, h);
        n.next = table[idx];
        table[idx] = n;
        // в конец списка порядка добавления
        n.before = tail;
        if (tail == null) {
            head = n;
        } else {
            tail.after = n;
        }
        tail = n;
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
                if (n.before == null) {
                    head = n.after;
                } else {
                    n.before.after = n.after;
                }
                if (n.after == null) {
                    tail = n.before;
                } else {
                    n.after.before = n.before;
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
        Node<E> n = head;
        while (n != null) {
            Node<E> next = n.after;
            if (!c.contains(n.value)) {
                remove(n.value);
                changed = true;
            }
            n = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = head;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) {
                    throw new NoSuchElementException();
                }
                E v = cur.value;
                cur = cur.after;
                return v;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        int i = 0;
        for (Node<E> n = head; n != null; n = n.after) {
            res[i++] = n.value;
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
        for (Node<E> n = head; n != null; n = n.after) {
            a[i++] = (T) n.value;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> n = head; n != null; n = n.after) {
            if (n != head) {
                sb.append(", ");
            }
            sb.append(n.value);
        }
        return sb.append(']').toString();
    }
}