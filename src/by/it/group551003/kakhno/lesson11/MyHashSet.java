package by.it.group551003.kakhno.lesson11;

import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int capacity;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    public MyHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++)
            table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException("Null elements are not allowed");

        if ((float)(size + 1) / capacity > LOAD_FACTOR)
            resize();

        int index = getHashIndex(element);

        Node<E> current = table[index];
        while (current != null) {
            if (current.data.equals(element))
                return false;
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null)
            return false;

        int index = getHashIndex(element);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (current.data.equals(element)) {
                if (previous == null)
                    table[index] = current.next;
                else
                    previous.next = current.next;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null)
            return false;

        int index = getHashIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (current.data.equals(element))
                return true;
            current = current.next;
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                array[index++] = current.data;
                current = current.next;
            }
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            a = newArray;
        }

        int index = 0;
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                a[index++] = (T) current.data;
                current = current.next;
            }
        }

        if (a.length > size)
            a[size] = null;

        return a;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object element : c)
            if (!contains(element))
                return false;
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c)
            if (add(element))
                modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (!c.contains(element)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object element : c)
            if (remove(element))
                modified = true;
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Iterator<E> iterator = iterator();
        boolean first = true;

        while (iterator.hasNext()) {
            if (!first)
                sb.append(", ");
            E element = iterator.next();
            sb.append(element == this ? "(this Collection)" : element);
            first = false;
        }

        sb.append(']');
        return sb.toString();
    }

    private int getHashIndex(Object element) {
        int hashCode = element.hashCode();
        hashCode = hashCode & 0x7FFFFFFF;
        return hashCode % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = new Node[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;

                int newIndex = (current.data.hashCode() & 0x7FFFFFFF) % newCapacity;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private class HashSetIterator implements Iterator<E> {
        private int currentIndex;
        private Node<E> currentNode;
        private Node<E> lastReturned;
        private boolean canRemove;

        public HashSetIterator() {
            currentIndex = 0;
            currentNode = null;
            lastReturned = null;
            canRemove = false;

            findNextNonEmptyBucket();
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            lastReturned = currentNode;
            E data = currentNode.data;
            currentNode = currentNode.next;
            canRemove = true;

            if (currentNode == null) {
                currentIndex++;
                findNextNonEmptyBucket();
            }

            return data;
        }

        @Override
        public void remove() {
            if (!canRemove || lastReturned == null)
                throw new IllegalStateException();

            MyHashSet.this.remove(lastReturned.data);
            lastReturned = null;
            canRemove = false;
        }

        private void findNextNonEmptyBucket() {
            while (currentIndex < capacity && table[currentIndex] == null)
                currentIndex++;

            if (currentIndex < capacity)
                currentNode = table[currentIndex];
            else
                currentNode = null;
        }
    }
}
