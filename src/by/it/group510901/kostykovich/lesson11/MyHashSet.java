package by.it.group510901.kostykovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
            this.next = null;
        }
    }

    private Object[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    public MyHashSet() {
        table = new Object[DEFAULT_CAPACITY];
        size = 0;
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
        table = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = hash(e);
        Node<E> newNode = new Node<>(e);

        Node<E> currentHead = (Node<E>) table[index];
        if (currentHead == null) {
            table[index] = newNode;
        } else {
            newNode.next = currentHead;
            table[index] = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> current = (Node<E>) table[index];

        while (current != null) {
            if (o == null && current.value == null) {
                return true;
            }
            if (o != null && o.equals(current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> current = (Node<E>) table[index];
        Node<E> prev = null;

        while (current != null) {


            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) {
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

    private int hash(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Object[] oldTable = table;
        table = new Object[oldTable.length * 2];
        size = 0;

        for (Object item : oldTable) {
            Node<E> current = (Node<E>) item;
            while (current != null) {
                add(current.value);
                current = current.next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        String result = "[";
        boolean first = true;

        for (Object item : table) {
            Node<E> current = (Node<E>) item;
            while (current != null) {
                if (!first) {
                    result += ", ";
                }
                result += current.value;
                first = false;
                current = current.next;
            }
        }

        result += "]";
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyHashSetIterator();
    }

    // Собственная реализация итератора без использования коллекций
    private class MyHashSetIterator implements Iterator<E> {
        private int currentBucket = 0;
        private Node<E> currentNode = null;

        MyHashSetIterator() {
            moveToNextValidNode();
        }

        private void moveToNextValidNode() {
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                return;
            }

            currentNode = null;
            while (currentBucket < table.length && currentNode == null) {
                currentNode = (Node<E>) table[currentBucket];
                currentBucket++;
            }
        }

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
            moveToNextValidNode();
            return value;
        }
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
}
}