package by.it.group551001.bochilo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    //Создайте аналог хеш-множества на основе массива с односвязными списками для коллизий
    //БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;

    private boolean equalsElement(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> bucket : table) {
            Node<E> node = bucket;
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.item, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean firstItem = true;
        for (Node<E> bucket : table) {
            for (Node<E> node = bucket; node != null; node = node.next) {
                if (!firstItem) {
                    sb.append(", ");
                }
                sb.append(node.item);
                firstItem = false;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = indexFor(e, table.length);
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(e, node.item)) {
                return false;
            }
        }
        table[index] = new Node<>(e, table[index]);
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o, table.length);
        Node<E> prev = null;
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
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
    public boolean contains(Object o) {
        int index = indexFor(o, table.length);
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
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
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> node = table[i];
            while (node != null) {
                if (!c.contains(node.item)) {
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
        return new Iterator<E>() {
            private int bucket = -1;
            private Node<E> next = advance(null);

            private Node<E> advance(Node<E> current) {
                if (current != null && current.next != null) {
                    return current.next;
                }
                while (++bucket < table.length) {
                    if (table[bucket] != null) {
                        return table[bucket];
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                Node<E> current = next;
                next = advance(current);
                return current.item;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) {
            result[i++] = e;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
