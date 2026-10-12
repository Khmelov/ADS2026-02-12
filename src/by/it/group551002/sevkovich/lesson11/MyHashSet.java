package by.it.group551002.sevkovich.lesson11;

import java.util.Collection;
import java.util.Set;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private int size;
    private int count;
    private Object[] array;

    private static final int DEFAULT_SIZE = 16;
    private static final float GROW_THRESHOLD = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        size = DEFAULT_SIZE;
        array = new Object[size];
    }

    private static class Node<E> {
        private Node<E> next;
        public E data;

        public Node(E data) {
            this.data = data;
        }
    }

    private int indexFromHash(int hashCode, int currentSize) {
        return hashCode & (currentSize - 1);
    }

    private int indexFromHash(int hashCode) {
        return indexFromHash(hashCode, size);
    }

    private void append(Object[] dst, Node<E> node, int index) {
        node.next = null;
        if (dst[index] == null) {
            dst[index] = node;
        } else {
            Node<E> curr = (Node<E>)dst[index];
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = node;
        }
    }

    private void grow() {
        int newSize = size * 2;
        Object[] newArray = new Object[newSize];
        for (int i = 0; i < size; i++) {
            Node<E> curr = (Node<E>)array[i];
            while (curr != null) {
                Node<E> next = curr.next;
                int hash = curr.data == null ? 0 : curr.data.hashCode();
                append(newArray, curr, indexFromHash(hash, newSize));
                curr = next;
            }
        }
        this.size = newSize;
        this.array = newArray;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        count = 0;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if ((float) count / size >= GROW_THRESHOLD) {
            grow();
        }
        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);
        append(array, new Node<>(e), index);
        count++;
        return true;
    }

    @Override
    public boolean remove(Object e) {
        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);
        Node<E> curr = (Node<E>)array[index];
        Node<E> prev = null;

        while (curr != null) {
            boolean match = (e == null) ? (curr.data == null) : e.equals(curr.data);
            if (match) {
                break;
            }
            prev = curr;
            curr = curr.next;
        }

        if (curr == null) {
            return false;
        }

        if (prev == null) {
            array[index] = curr.next;
        } else {
            prev.next = curr.next;
        }
        count--;
        return true;
    }

    @Override
    public boolean contains(Object e) {
        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);
        Node<E> curr = (Node<E>)array[index];

        while (curr != null) {
            boolean match = (e == null) ? (curr.data == null) : e.equals(curr.data);
            if (match) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);
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
        Objects.requireNonNull(c);
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> currentNode = null;
            private Node<E> lastReturned = null;

            private void advance() {
                while (currentNode == null && bucketIndex < size) {
                    currentNode = (Node<E>)array[bucketIndex];
                    bucketIndex++;
                }
            }

            @Override
            public boolean hasNext() {
                advance();
                return currentNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = currentNode;
                currentNode = currentNode.next;
                return lastReturned.data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException("next() не был вызван или элемент уже удален");
                }
                MyHashSet.this.remove(lastReturned.data);
                lastReturned = null;
            }
        };
    }

    // Optional

    @Override
    public Object[] toArray() {
        Object[] result = new Object[count];
        int i = 0;
        for (E e : this) {
            result[i++] = e;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < count) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), count);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > count) {
            a[count] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;
        return containsAll(c);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(size * 4);
        sb.append("[");
        boolean first = true;
        for (E x : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(x);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
}