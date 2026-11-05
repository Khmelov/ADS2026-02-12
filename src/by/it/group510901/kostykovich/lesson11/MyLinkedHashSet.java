package by.it.group510901.kostykovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Set<E> {


    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value) {
            this.value = value;
        }
    }

    private Object[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E> head;
    private Node<E> tail;

    public MyLinkedHashSet() {
        table = new Object[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
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
        head = null;
        tail = null;
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

        Node<E> currentBucketHead = (Node<E>) table[index];
        if (currentBucketHead != null) {
            newNode.next = currentBucketHead;
        }
        table[index] = newNode;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> current = (Node<E>) table[index];

        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) {
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

                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
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
            current = current.after;
        }

        result += "]";
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (!c.contains(element)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Object[] oldTable = table;
        table = new Object[oldTable.length * 2];

        Node<E> current = head;
        while (current != null) {
            int index = hash(current.value);

            current.next = (Node<E>) table[index];
            table[index] = current;

            current = current.after;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyLinkedHashSetIterator();
    }

    private class MyLinkedHashSetIterator implements Iterator<E> {
        private Node<E> nextNode = head;
        private Node<E> lastReturned = null;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode;
            nextNode = nextNode.after;
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
