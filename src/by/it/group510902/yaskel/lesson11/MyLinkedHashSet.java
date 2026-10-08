package by.it.group510902.yaskel.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        final E item;
        final int hash;
        Node<E> next;   // следующий узел в цепочке коллизий (односвязный список)
        Node<E> before; // предыдущий по порядку добавления
        Node<E> after;  // следующий по порядку добавления

        Node(E item, int hash) {
            this.item = item;
            this.hash = hash;
        }
    }

    private Node<E>[] table;
    private Node<E> head; // самый старый элемент
    private Node<E> tail; // самый новый элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
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

    private Node<E> findNode(Object o) {
        int h = hash(o);
        for (Node<E> current = table[indexFor(h, table.length)]; current != null; current = current.next) {
            if (current.hash == h && same(o, current.item)) {
                return current;
            }
        }
        return null;
    }

    private void linkLast(Node<E> node) {
        node.before = tail;
        node.after = null;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;
    }

    private void unlinkOrder(Node<E> node) {
        if (node.before == null) {
            head = node.after;
        } else {
            node.before.after = node.after;
        }
        if (node.after == null) {
            tail = node.before;
        } else {
            node.after.before = node.before;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        // идём по порядку добавления и заново раскладываем узлы по корзинам
        for (Node<E> current = head; current != null; current = current.after) {
            int index = indexFor(current.hash, bigger.length);
            current.next = bigger[index];
            bigger[index] = current;
        }
        table = bigger;
    }

    // обязательные методы

    @Override
    public String toString() {
        String result = "[";
        for (Node<E> current = head; current != null; current = current.after) {
            if (current != head) {
                result += ", ";
            }
            result += current.item;
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
    public boolean add(E element) {
        if (findNode(element) != null) {
            return false;
        }

        int h = hash(element);
        int index = indexFor(h, table.length);

        Node<E> node = new Node<>(element, h);
        node.next = table[index];
        table[index] = node;
        linkLast(node);
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
                unlinkOrder(current);
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
        return findNode(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
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
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            boolean changed = size > 0;
            clear();
            return changed;
        }
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
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            return false;
        }
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.after; // запоминаем до удаления
            if (!c.contains(current.item)) {
                remove(current.item);
                changed = true;
            }
            current = nextNode;
        }
        return changed;
    }

    // остальные методы интерфейса

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;
            private Node<E> lastReturned;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = next;
                next = next.after;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.item);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> current = head; current != null; current = current.after) {
            result[i++] = current.item;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}