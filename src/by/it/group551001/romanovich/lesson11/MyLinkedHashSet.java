package by.it.group551001.romanovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        final E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = head; node != null; node = node.after) {
            if (node != head) {
                sb.append(", ");
            }
            sb.append(node.value);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
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
        if (contains(element)) {
            return false;
        }
        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = indexFor(element, table.length);
        Node<E> node = new Node<>(element, table[index]);
        table[index] = node;
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
    public boolean remove(Object element) {
        int index = indexFor(element, table.length);
        Node<E> prev = null;
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (same(element, node.value)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                unlinkOrder(node);
                size--;
                return true;
            }
            prev = node;
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        for (Node<E> node = table[indexFor(element, table.length)]; node != null; node = node.next) {
            if (same(element, node.value)) {
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
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.after;
            if (!c.contains(node.value)) {
                remove(node.value);
                modified = true;
            }
            node = next;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private static int indexFor(Object o, int length) {
        int h = o == null ? 0 : o.hashCode();
        h ^= h >>> 16;
        return h & (length - 1);
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
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

    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> node = head; node != null; node = node.after) {
            int index = indexFor(node.value, newTable.length);
            node.next = newTable[index];
            newTable[index] = node;
        }
        table = newTable;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.after;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            result[i++] = node.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            result[i++] = (T) node.value;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }
}
