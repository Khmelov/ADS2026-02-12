package by.it.group551001.Barusevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int LOAD_FACTOR_NUMERATOR = 3;
    private static final int LOAD_FACTOR_DENOMINATOR = 4;

    private Node<E>[] table;
    private int size;

    private static final class Node<E> {
        private final E value;
        private final int hash;
        private Node<E> next;

        private Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    private int hash(Object value) {
        if (value == null) {
            return 0;
        }
        int result = value.hashCode();
        return result ^ (result >>> 16);
    }

    private int indexFor(int hash, int capacity) {
        return (hash & 0x7fffffff) % capacity;
    }

    private Node<E> findNode(Object value, int hash) {
        int index = indexFor(hash, table.length);
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && equalValues(node.value, value)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean equalValues(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.hash, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
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
    public boolean contains(Object value) {
        return findNode(value, hash(value)) != null;
    }

    @Override
    public boolean add(E value) {
        int hash = hash(value);
        if (findNode(value, hash) != null) {
            return false;
        }
        if (size + 1 > table.length * LOAD_FACTOR_NUMERATOR / LOAD_FACTOR_DENOMINATOR) {
            resize();
        }
        int index = indexFor(hash, table.length);
        table[index] = new Node<>(value, hash, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object value) {
        int hash = hash(value);
        int index = indexFor(hash, table.length);
        Node<E> previous = null;
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && equalValues(node.value, value)) {
                if (previous == null) {
                    table[index] = node.next;
                } else {
                    previous.next = node.next;
                }
                size--;
                return true;
            }
            previous = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object value : collection) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;
        for (E value : collection) {
            if (add(value)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (collection.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int nextBucket;
            private Node<E> nextNode;
            private Node<E> lastReturned;

            {
                advanceToNextBucket();
            }

            private void advanceToNextBucket() {
                while (nextNode == null && nextBucket < table.length) {
                    nextNode = table[nextBucket++];
                }
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = nextNode;
                nextNode = nextNode.next;
                advanceToNextBucket();
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (E value : this) {
            result[index++] = value;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        T[] result = array.length >= size
                ? array
                : (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        int index = 0;
        for (E value : this) {
            result[index++] = (T) value;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (E value : this) {
            if (!first) {
                result += ", ";
            }
            result += value;
            first = false;
        }
        return result + "]";
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (E value : this) {
            if (value != null) {
                result += value.hashCode();
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Set<?>)) {
            return false;
        }
        Set<?> other = (Set<?>) object;
        if (other.size() != size) {
            return false;
        }
        try {
            return containsAll(other);
        } catch (ClassCastException | NullPointerException exception) {
            return false;
        }
    }
}
