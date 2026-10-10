package by.it.group510902.slesar.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }

        int hash = o.hashCode();
        hash ^= (hash >>> 16);

        return (hash & 0x7FFFFFFF) % table.length;
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
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);

        Node<E> current = table[index];

        while (current != null) {
            if (e == null ? current.item == null : e.equals(current.item)) {
                return false;
            }

            current = current.next;
        }

        table[index] = new Node<>(e, table[index]);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];

            while (current != null) {
                if (!first) {
                    result.append(", ");
                }

                result.append(current.item);
                first = false;

                current = current.next;
            }
        }

        result.append("]");

        return result.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;

        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            Node<E> previous = null;

            while (current != null) {
                if (!c.contains(current.item)) {
                    if (previous == null) {
                        table[i] = current.next;
                    } else {
                        previous.next = current.next;
                    }

                    size--;
                    modified = true;
                    current = (previous == null) ? table[i] : previous.next;
                } else {
                    previous = current;
                    current = current.next;
                }
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (Object element : c) {
            while (remove(element)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public java.util.Iterator<E> iterator() {
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
}
