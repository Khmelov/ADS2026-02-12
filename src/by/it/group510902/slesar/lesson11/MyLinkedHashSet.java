package by.it.group510902.slesar.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;

    private Node<E> orderHead;
    private Node<E> orderTail;

    private static class Node<E> {
        E item;

        Node<E> next;

        Node<E> orderNext;

        Node(E item) {
            this.item = item;
        }
    }

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }

        int hash = o.hashCode();
        hash ^= (hash >>> 16);

        return (hash & 0x7FFFFFFF) % table.length;
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
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
        orderHead = null;
        orderTail = null;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);

        Node<E> current = table[index];

        while (current != null) {
            if (e == null ? current.item == null : e.equals(current.item)) {
                return false;
            }

            current = current.next;
        }

        Node<E> newNode = new Node<>(e);

        newNode.next = table[index];
        table[index] = newNode;

        if (orderHead == null) {
            orderHead = newNode;
            orderTail = newNode;
        } else {
            orderTail.orderNext = newNode;
            orderTail = newNode;
        }

        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                removeFromOrder(current);

                size--;

                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    private void removeFromOrder(Node<E> node) {
        if (orderHead == node) {
            orderHead = node.orderNext;

            if (orderTail == node) {
                orderTail = null;
            }

            node.orderNext = null;
            return;
        }

        Node<E> current = orderHead;

        while (current != null && current.orderNext != node) {
            current = current.orderNext;
        }

        if (current != null) {
            current.orderNext = node.orderNext;

            if (orderTail == node) {
                orderTail = current;
            }
        }

        node.orderNext = null;
    }

    @Override
    public String toString() {
        String result = "[";

        Node<E> current = orderHead;

        while (current != null) {
            result += current.item;

            if (current.orderNext != null) {
                result += ", ";
            }

            current = current.orderNext;
        }

        result += "]";

        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
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

        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        Node<E> current = orderHead;

        while (current != null) {
            Node<E> next = current.orderNext;

            if (!c.contains(current.item)) {
                remove(current.item);
                modified = true;
            }

            current = next;
        }

        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
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