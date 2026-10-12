package by.it.group551002.sevkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private int size;
    private int count;
    private Object[] table;

    private Node<E> head;
    private Node<E> tail;

    private static final int DEFAULT_SIZE = 16;
    private static final float GROW_THRESHOLD = 0.75f;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        size = DEFAULT_SIZE;
        table = new Object[size];
    }

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data) {
            this.data = data;
        }
    }

    private int indexFromHash(int hashCode, int currentSize) {
        return hashCode & (currentSize - 1);
    }

    private int indexFromHash(int hashCode) {
        return indexFromHash(hashCode, size);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newSize = size * 2;
        Object[] newTable = new Object[newSize];

        for (int i = 0; i < size; i++) {
            Node<E> curr = (Node<E>) table[i];
            while (curr != null) {
                Node<E> next = curr.next;
                int hash = curr.data == null ? 0 : curr.data.hashCode();
                int index = indexFromHash(hash, newSize);
                curr.next = (Node<E>) newTable[index];
                newTable[index] = curr;
                curr = next;
            }
        }

        this.size = newSize;
        this.table = newTable;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object e) {
        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);

        @SuppressWarnings("unchecked")
        Node<E> curr = (Node<E>) table[index];

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
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        if ((float) count / size >= GROW_THRESHOLD) {
            grow();
        }

        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);

        Node<E> node = new Node<>(e);
        node.next = (Node<E>) table[index];
        table[index] = node;

        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }

        count++;
        return true;
    }

    @Override
    public boolean remove(Object e) {
        int hash = e == null ? 0 : e.hashCode();
        int index = indexFromHash(hash);

        @SuppressWarnings("unchecked")
        Node<E> curr = (Node<E>) table[index];
        Node<E> prevBucket = null;

        while (curr != null) {
            boolean match = (e == null) ? (curr.data == null) : e.equals(curr.data);
            if (match) {
                if (prevBucket == null) {
                    table[index] = curr.next;
                } else {
                    prevBucket.next = curr.next;
                }

                if (curr.before != null) {
                    curr.before.after = curr.after;
                } else {
                    head = curr.after;
                }

                if (curr.after != null) {
                    curr.after.before = curr.before;
                } else {
                    tail = curr.before;
                }

                count--;
                return true;
            }

            prevBucket = curr;
            curr = curr.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            table[i] = null;
        }
        head = tail = null;
        count = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
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
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            boolean modified = !isEmpty();
            clear();
            return modified;
        }

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
        if (c == null) {
            throw new NullPointerException();
        }

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
            private Node<E> nextNode = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = nextNode;
                nextNode = nextNode.after;
                return lastReturned.data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.data);
                lastReturned = null;
            }
        };
    }

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
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), count);
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
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        boolean first = true;
        for (E e : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(e);
            first = false;
        }

        sb.append(']');
        return sb.toString();
    }
}