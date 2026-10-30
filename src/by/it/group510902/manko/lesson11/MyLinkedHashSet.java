package by.it.group510902.manko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private static class Node<E> {
        E value;
        int hash;
        Node<E> next;      // next in the same bucket (collision chain)
        Node<E> before;    // previous in insertion order
        Node<E> after;     // next in insertion order

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private int capacity = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E> head; // first in insertion order
    private Node<E> tail; // last in insertion order

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[capacity];
    }

    private int hash(Object o) {
        int h = o == null ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return h;
    }

    private int indexFor(int hash, int cap) {
        return hash & (cap - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
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
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                result.append(", ");
            }
            result.append(current.value);
            first = false;
            current = current.after;
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
        head = tail = null;
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
        int h = hash(e);
        int index = indexFor(h, capacity);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == h &&
                    (e == null ? current.value == null : e.equals(current.value))) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e, h, table[index]);
        table[index] = newNode;

        // append to insertion-order list
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = indexFor(h, capacity);

        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if (current.hash == h &&
                    (o == null ? current.value == null : o.equals(current.value))) {
                // unlink from bucket
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                // unlink from insertion-order list
                unlinkOrder(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    private void unlinkOrder(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int index = indexFor(h, capacity);
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == h &&
                    (o == null ? current.value == null : o.equals(current.value))) {
                return true;
            }
            current = current.next;
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
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.value)) {
                // unlink from bucket
                int index = indexFor(current.hash, capacity);
                Node<E> bucketCur = table[index];
                Node<E> prev = null;
                while (bucketCur != null) {
                    if (bucketCur == current) {
                        if (prev == null) {
                            table[index] = bucketCur.next;
                        } else {
                            prev.next = bucketCur.next;
                        }
                        break;
                    }
                    prev = bucketCur;
                    bucketCur = bucketCur.next;
                }
                // unlink from insertion-order list
                unlinkOrder(current);
                size--;
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Set             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.after;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            result[i++] = current.value;
            current = current.after;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(toArray(), size, a.getClass());
        }
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            a[i++] = (T) current.value;
            current = current.after;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}