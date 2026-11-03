package by.it.group551003.kakhno.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int capacity;

    private Node<E> head;
    private Node<E> tail;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> bucketNext;
        Node<E> prev;
        Node<E> orderNext;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.bucketNext = null;
            this.prev = null;
            this.orderNext = null;
        }
    }

    public MyLinkedHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++)
            table[i] = null;
        head = null;
        tail = null;
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

        if (contains(element))
            return false;

        int index = getHashIndex(element);

        Node<E> newNode = new Node<>(element);

        newNode.bucketNext = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.orderNext = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

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
                    table[index] = current.bucketNext;
                else
                    previous.bucketNext = current.bucketNext;

                removeFromOrderList(current);

                size--;
                return true;
            }
            previous = current;
            current = current.bucketNext;
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
            current = current.bucketNext;
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;

        Node<E> current = head;
        while (current != null) {
            array[index++] = current.data;
            current = current.orderNext;
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
        Node<E> current = head;
        while (current != null) {
            a[index++] = (T) current.data;
            current = current.orderNext;
        }

        if (a.length > size)
            a[size] = null;

        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        for (Object element : c)
            if (!contains(element))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        for (E element : c)
            if (add(element))
                modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

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
    public boolean removeAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

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

        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first)
                sb.append(", ");
            E element = current.data;
            sb.append(element == this ? "(this Collection)" : element);
            first = false;
            current = current.orderNext;
        }

        sb.append(']');
        return sb.toString();
    }

    private int getHashIndex(Object element) {
        int hashCode = element.hashCode();
        hashCode = hashCode & 0x7FFFFFFF;
        return hashCode % capacity;
    }

    private void removeFromOrderList(Node<E> node) {
        if (node.prev != null)
            node.prev.orderNext = node.orderNext;
        else
            head = node.orderNext;

        if (node.orderNext != null)
            node.orderNext.prev = node.prev;
        else
            tail = node.prev;

        node.prev = null;
        node.orderNext = null;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = new Node[newCapacity];

        Node<E> current = head;
        while (current != null) {
            int newIndex = (current.data.hashCode() & 0x7FFFFFFF) % newCapacity;

            Node<E> nextInOrder = current.orderNext;

            current.bucketNext = newTable[newIndex];
            newTable[newIndex] = current;

            current = nextInOrder;
        }

        table = newTable;
        capacity = newCapacity;
    }

    private class LinkedHashSetIterator implements Iterator<E> {
        private Node<E> currentNode;
        private Node<E> lastReturned;
        private boolean canRemove;

        public LinkedHashSetIterator() {
            currentNode = head;
            lastReturned = null;
            canRemove = false;
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
            currentNode = currentNode.orderNext;
            canRemove = true;

            return data;
        }

        @Override
        public void remove() {
            if (!canRemove || lastReturned == null)
                throw new IllegalStateException();

            MyLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null;
            canRemove = false;
        }
    }
}
