package by.it.group510902.kislyi.lesson11;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[15];
        threshold = 10;
    }

    private int index(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return (h & 0x7fffffff) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = table;
        table = new Node[old.length * 2];
        threshold = table.length * 3 / 4;
        size = 0;
        for (Node<E> head : old) {
            while (head != null) {
                add(head.value);
                head = head.next;
            }
        }
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
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int i = index(e);
        Node<E> cur = table[i];
        while (cur != null) {
            if (Objects.equals(cur.value, e)) {
                return false;
            }
            cur = cur.next;
        }
        Node<E> node = new Node<>(e);
        node.next = table[i];
        table[i] = node;
        size++;
        if (size > threshold) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = index(o);
        Node<E> cur = table[i];
        Node<E> prev = null;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
                if (prev == null) {
                    table[i] = cur.next;
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
    public boolean contains(Object o) {
        int i = index(o);
        Node<E> cur = table[i];
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            while (head != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(head.value);
                first = false;
                head = head.next;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}