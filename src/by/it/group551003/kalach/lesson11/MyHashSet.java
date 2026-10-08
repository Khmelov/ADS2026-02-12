package by.it.group551003.kalach.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // узел односвязного списка для элементов с одинаковым индексом (коллизии)
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

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    // ---------- вспомогательные методы ----------

    private static int hash(Object o) {
        if (o == null) return 0;
        int h = o.hashCode();
        return h ^ (h >>> 16);
    }

    private static int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> head : table) {
            Node<E> cur = head;
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(cur.hash, bigger.length);
                cur.next = bigger[idx];
                bigger[idx] = cur;
                cur = next;
            }
        }
        table = bigger;
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int h = hash(element);
        int idx = indexFor(h, table.length);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (cur.hash == h && same(element, cur.value)) return false;
        }
        table[idx] = new Node<>(element, h, table[idx]);
        size++;
        if (size > table.length * LOAD_FACTOR) resize();
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int h = hash(element);
        int idx = indexFor(h, table.length);
        Node<E> prev = null;
        for (Node<E> cur = table[idx]; cur != null; prev = cur, cur = cur.next) {
            if (cur.hash == h && same(element, cur.value)) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        int h = hash(element);
        int idx = indexFor(h, table.length);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (cur.hash == h && same(element, cur.value)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            for (Node<E> cur = head; cur != null; cur = cur.next) {
                if (!first) sb.append(", ");
                sb.append(cur.value);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
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
                    if (prev == null) table[i] = cur.next;
                    else prev.next = cur.next;
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
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> next = advance(null);

            // находит следующий узел после current (или первый, если current == null)
            private Node<E> advance(Node<E> current) {
                if (current != null && current.next != null) return current.next;
                while (bucket < table.length) {
                    Node<E> head = table[bucket++];
                    if (head != null) return head;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) throw new NoSuchElementException();
                E value = next.value;
                next = advance(next);
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) result[i++] = e;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) a[i++] = (T) e;
        if (a.length > size) a[size] = null;
        return a;
    }
}
