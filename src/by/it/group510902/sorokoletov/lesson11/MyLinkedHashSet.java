package by.it.group510902.sorokoletov.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> insertionNext;
        Node<E> insertionPrev;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    private int getIndex(Object key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16);
        return (hash & 0x7FFFFFFF) % table.length;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        newNode.next = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.insertionNext = newNode;
            newNode.insertionPrev = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prevInBucket = null;

        while (current != null) {
            boolean isMatch = (o == null) ? (current.value == null) : o.equals(current.value);
            if (isMatch) {
                if (prevInBucket == null) {
                    table[index] = current.next;
                } else {
                    prevInBucket.next = current.next;
                }

                if (current.insertionPrev != null) {
                    current.insertionPrev.insertionNext = current.insertionNext;
                } else {
                    head = current.insertionNext;
                }

                if (current.insertionNext != null) {
                    current.insertionNext.insertionPrev = current.insertionPrev;
                } else {
                    tail = current.insertionPrev;
                }

                current.next = null;
                current.insertionNext = null;
                current.insertionPrev = null;

                size--;
                return true;
            }
            prevInBucket = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            boolean isMatch = (o == null) ? (current.value == null) : o.equals(current.value);
            if (isMatch) {
                return true;
            }
            current = current.next;
        }
        return false;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        String result = "[";
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                result += ", ";
            }
            result += current.value;
            first = false;
            current = current.insertionNext;
        }
        result += "]";
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
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
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.insertionNext;
            if (c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.insertionNext;
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E value = currentNode.value;
                currentNode = currentNode.insertionNext;
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
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