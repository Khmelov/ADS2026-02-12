package by.it.group551002.voroshko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> orderNext;
        Node<E> orderPrev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.orderNext = null;
            this.orderPrev = null;
        }
    }

    private Node<E>[] table = new Node[10];
    private int size = 0;

    private Node<E> head = null;
    private Node<E> tail = null;

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }
        return Math.abs(element.hashCode()) % table.length;
    }

    private boolean elementsEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 3 / 2];

        Node<E> current = head;
        while(current != null) {
            int newIndex = getIndex(current.data);
            Node<E> bucketHead = table[newIndex];
            current.next = bucketHead;
            table[newIndex] = current;

            current = current.orderNext;
        }
    }

    private void removeFromOrderList(Node<E> node) {
        Node<E> prev = node.orderPrev;
        Node<E> next = node.orderNext;

        if (prev == null) {
            head = next;
        } else {
            prev.orderNext = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.orderPrev = prev;
        }

        node.orderPrev = prev;
        node.orderNext = next;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.orderNext;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (size >= table.length * 0.75) {
            resize();
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (elementsEqual(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.orderNext = newNode;
            newNode.orderPrev = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (elementsEqual(current.data, element)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                removeFromOrderList(current);

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
        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (elementsEqual(current.data, element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean modified = false;
        for (E element : collection) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;

        Node<E> current = head;
        while (current != null) {
            Node<E> nextInOrder = current.orderNext;
            if (collection.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = nextInOrder;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;

        Node<E> current = head;
        while (current != null) {
            Node<E> nextInOrder = current.orderNext;
            if (!collection.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = nextInOrder;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }
}
