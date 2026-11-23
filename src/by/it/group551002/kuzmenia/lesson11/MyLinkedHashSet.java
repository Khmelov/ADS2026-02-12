package by.it.group551002.kuzmenia.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////                      Вспомогательные методы                 //////
    /////////////////////////////////////////////////////////////////////////

    private int indexFor(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7fffffff) % table.length;
    }

    private static boolean same(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            if (cur != head) {
                result += ", ";
            }
            result += cur.value;
        }
        return result + "]";
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
        head = null;
        tail = null;
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
        Node<E> node = new Node<>(e);

        int ind = indexFor(e);
        node.next = table[ind];
        table[ind] = node;

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
        int ind = indexFor(o);
        Node<E> prev = null;
        for (Node<E> cur = table[ind]; cur != null; cur = cur.next) {
            if (same(o, cur.value)) {
                // Вырезание из цепочки коллизий
                if (prev != null) {
                    prev.next = cur.next;
                } else {
                    table[ind] = cur.next;
                }

                // Вырезание из списка порядка
                if (cur.before != null) {
                    cur.before.after = cur.after;
                } else {
                    head = cur.after;
                }
                if (cur.after != null) {
                    cur.after.before = cur.before;
                } else {
                    tail = cur.before;
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
        for (Node<E> cur = table[indexFor(o)]; cur != null; cur = cur.next) {
            if (same(o, cur.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            if (add(x)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) {
            if (remove(x)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> following = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = following;
        }
        return changed;
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
}