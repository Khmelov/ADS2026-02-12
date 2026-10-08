package by.it.group551001.romanovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        final E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean isFirst = true;
        for (Node<E> bucket : table) {
            for (Node<E> node = bucket; node != null; node = node.next) {
                if (!isFirst) {
                    sb.append(", ");
                }
                sb.append(node.value);
                isFirst = false;
            }
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
        table[index] = new Node<>(element, table[index]);
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

    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> bucket : table) {
            Node<E> node = bucket;
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.value, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
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
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> node = table[i];
            while (node != null) {
                if (!c.contains(node.value)) {
                    if (prev == null) {
                        table[i] = node.next;
                    } else {
                        prev.next = node.next;
                    }
                    size--;
                    modified = true;
                } else {
                    prev = node;
                }
                node = node.next;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        Object[] snapshot = toArray();
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < snapshot.length;
            }

            @Override
            public E next() {
                if (index >= snapshot.length) {
                    throw new NoSuchElementException();
                }
                return (E) snapshot[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> bucket : table) {
            for (Node<E> node = bucket; node != null; node = node.next) {
                result[i++] = node.value;
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] values = toArray();
        T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) values[i];
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }
}
