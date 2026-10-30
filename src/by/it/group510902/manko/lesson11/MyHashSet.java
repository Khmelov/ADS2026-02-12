package by.it.group510902.manko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private int capacity = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[capacity];
    }

    private int hash(Object o) {
        int h = o == null ? 0 : o.hashCode();
        // spread bits to reduce collisions (same idea as HashMap)
        h ^= (h >>> 16);
        return h & (capacity - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = (current.value == null ? 0 : current.value.hashCode());
                newIndex ^= (newIndex >>> 16);
                newIndex &= (newCapacity - 1);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    result.append(", ");
                }
                result.append(current.value);
                first = false;
                current = current.next;
            }
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
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
        if (size + 1 > capacity * LOAD_FACTOR) {
            resize();
        }
        int index = hash(e);
        Node<E> current = table[index];
        while (current != null) {
            if (e == null ? current.value == null : e.equals(current.value)) {
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
        int index = hash(o);
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
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
        int index = hash(o);
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Set             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> nextNode = advance();

            private Node<E> advance() {
                while (bucketIndex < capacity) {
                    if (table[bucketIndex] != null) {
                        return table[bucketIndex];
                    }
                    bucketIndex++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                E value = nextNode.value;
                nextNode = nextNode.next;
                if (nextNode == null) {
                    bucketIndex++;
                    nextNode = advance();
                }
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (int b = 0; b < capacity; b++) {
            Node<E> current = table[b];
            while (current != null) {
                result[i++] = current.value;
                current = current.next;
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
        }
        int i = 0;
        for (int b = 0; b < capacity; b++) {
            Node<E> current = table[b];
            while (current != null) {
                a[i++] = (T) current.value;
                current = current.next;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            Node<E> prev = null;
            while (current != null) {
                if (!c.contains(current.value)) {
                    if (prev == null) {
                        table[i] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    modified = true;
                    current = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = current;
                    current = current.next;
                }
            }
        }
        return modified;
    }
}