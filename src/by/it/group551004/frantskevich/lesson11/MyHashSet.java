package by.it.group551004.frantskevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // элемент односвязного списка внутри корзины
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = new Node[16];
    private int size = 0;

    private int bucket(Object o, int length) {
        int h = o == null ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % length;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void resize() {
        Node<E>[] table_new = new Node[table.length * 2];
        for (Node<E> head : table) {
            for (Node<E> x = head; x != null; ) {
                Node<E> next = x.next;
                int i = bucket(x.value, table_new.length);
                x.next = table_new[i];
                table_new[i] = x;
                x = next;
            }
        }
        table = table_new;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean firstItem = true;
        for (Node<E> head : table) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (!firstItem) {
                    sb.append(", ");
                }
                sb.append(x.value);
                firstItem = false;
            }
        }
        sb.append("]");
        return sb.toString();
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
        int i = bucket(e, table.length);
        for (Node<E> x = table[i]; x != null; x = x.next) {
            if (same(e, x.value)) {
                return false;
            }
        }
        table[i] = new Node<>(e, table[i]);
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = bucket(o, table.length);
        Node<E> prev = null;
        for (Node<E> x = table[i]; x != null; prev = x, x = x.next) {
            if (same(o, x.value)) {
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = table[bucket(o, table.length)]; x != null; x = x.next) {
            if (same(o, x.value)) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
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
            for (Node<E> x = table[i]; x != null; x = x.next) {
                if (!c.contains(x.value)) {
                    if (prev == null) {
                        table[i] = x.next;
                    } else {
                        prev.next = x.next;
                    }
                    size--;
                    modified = true;
                } else {
                    prev = x;
                }
            }
        }
        return modified;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E item : this) {
            result[i++] = item;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E item : this) {
            a[i++] = (T) item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> current = null;
            private int returned = 0;

            @Override
            public boolean hasNext() {
                return returned < size;
            }

            @Override
            public E next() {
                if (returned >= size) {
                    throw new NoSuchElementException();
                }
                if (current != null) {
                    current = current.next;
                }
                while (current == null) {
                    current = table[bucketIndex++];
                }
                returned++;
                return current.value;
            }
        };
    }
}
