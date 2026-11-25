package by.it.group551001.telipko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E key;
        final int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E key, int hash) {
            this.key = key;
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

    private static int hashOf(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return h ^ (h >>> 16);
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] nt = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> x = head; x != null; x = x.after) {
            int i = x.hash & (nt.length - 1);
            x.next = nt[i];
            nt[i] = x;
        }
        table = nt;
    }

    private boolean bulkRemove(Collection<?> c, boolean removeIfContained) {
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> following = x.after;
            if (c.contains(x.key) == removeIfContained) {
                remove(x.key);
                changed = true;
            }
            x = following;
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = head; x != null; x = x.after) {
            sb.append(x.key);
            if (x.after != null) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (Node<E>[]) new Node[16];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int h = hashOf(o);
        for (Node<E> x = table[h & (table.length - 1)]; x != null; x = x.next) {
            if (x.hash == h && same(o, x.key)) {
                return true;
            }
        }
        return false;
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
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        Node<E> node = new Node<>(e, hashOf(e));
        int i = node.hash & (table.length - 1);
        node.next = table[i];
        table[i] = node;
        node.before = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hashOf(o);
        int i = h & (table.length - 1);
        Node<E> prev = null;
        for (Node<E> x = table[i]; x != null; prev = x, x = x.next) {
            if (x.hash == h && same(o, x.key)) {
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
                }
                if (x.before == null) {
                    head = x.after;
                } else {
                    x.before.after = x.after;
                }
                if (x.after == null) {
                    tail = x.before;
                } else {
                    x.after.before = x.before;
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
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

}