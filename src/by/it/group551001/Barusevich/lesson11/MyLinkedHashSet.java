package by.it.group551001.Barusevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;
    private Node<E> first;
    private Node<E> last;

    private static final class Node<E> {
        private final E value;
        private final int hash;
        private Node<E> bucketNext;
        private Node<E> orderNext;
        private Node<E> orderPrevious;

        private Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
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

    private boolean equalValues(Object firstValue, Object secondValue) {
        return firstValue == null ? secondValue == null : firstValue.equals(secondValue);
    }

    private Node<E> findNode(Object value, int hash) {
        Node<E> node = table[indexFor(hash, table.length)];
        while (node != null) {
            if (node.hash == hash && equalValues(node.value, value)) {
                return node;
            }
            node = node.bucketNext;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.bucketNext;
                int index = indexFor(node.hash, newTable.length);
                node.bucketNext = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private void unlinkFromOrder(Node<E> node) {
        if (node.orderPrevious == null) {
            first = node.orderNext;
        } else {
            node.orderPrevious.orderNext = node.orderNext;
        }
        if (node.orderNext == null) {
            last = node.orderPrevious;
        } else {
            node.orderNext.orderPrevious = node.orderPrevious;
        }
        node.orderNext = null;
        node.orderPrevious = null;
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
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        int index = indexFor(hash, table.length);
        Node<E> node = new Node<>(value, hash);
        node.bucketNext = table[index];
        table[index] = node;

        node.orderPrevious = last;
        if (last == null) {
            first = node;
        } else {
            last.orderNext = node;
        }
        last = node;
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
                    table[index] = node.bucketNext;
                } else {
                    previous.bucketNext = node.bucketNext;
                }
                unlinkFromOrder(node);
                node.bucketNext = null;
                size--;
                return true;
            }
            previous = node;
            node = node.bucketNext;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        first = null;
        last = null;
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
            private Node<E> nextNode = first;
            private Node<E> lastReturned;

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
                nextNode = nextNode.orderNext;
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
        Node<E> node = first;
        while (node != null) {
            if (node != first) {
                result += ", ";
            }
            result += node.value;
            node = node.orderNext;
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
