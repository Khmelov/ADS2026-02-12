package by.it.group551003.kalach.lesson11;


import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // узел: next - односвязный список коллизий в корзине,
    // before/after - двусвязный список для хранения порядка добавления
    private static class Node<E> {
        final E value;
        final int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head;   // самый старый элемент
    private Node<E> tail;   // самый новый элемент
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
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
        for (Node<E> bucketHead : table) {
            Node<E> cur = bucketHead;
            while (cur != null) {
                Node<E> nxt = cur.next;
                int idx = indexFor(cur.hash, bigger.length);
                cur.next = bigger[idx];
                bigger[idx] = cur;
                cur = nxt;
            }
        }
        table = bigger;
    }

    private void linkLast(Node<E> node) {
        node.before = tail;
        if (tail == null) head = node;
        else tail.after = node;
        tail = node;
    }

    private void unlinkOrder(Node<E> node) {
        if (node.before == null) head = node.after;
        else node.before.after = node.after;

        if (node.after == null) tail = node.before;
        else node.after.before = node.before;

        node.before = null;
        node.after = null;
    }

    private Node<E> findNode(Object element) {
        int h = hash(element);
        for (Node<E> cur = table[indexFor(h, table.length)]; cur != null; cur = cur.next) {
            if (cur.hash == h && same(element, cur.value)) return cur;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            sb.append(cur.value);
            if (cur.after != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
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
        if (findNode(element) != null) return false;
        int h = hash(element);
        int idx = indexFor(h, table.length);
        Node<E> node = new Node<>(element, h, table[idx]);
        table[idx] = node;
        linkLast(node);
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
                unlinkOrder(cur);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        return findNode(element) != null;
    }

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
        Node<E> cur = head;
        while (cur != null) {
            Node<E> nxt = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = nxt;
        }
        return changed;
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

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
                if (cur == null) throw new NoSuchElementException();
                E value = cur.value;
                cur = cur.after;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.after) result[i++] = cur.value;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.after) a[i++] = (T) cur.value;
        if (a.length > size) a[size] = null;
        return a;
    }
}
