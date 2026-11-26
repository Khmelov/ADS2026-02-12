package by.it.group551004.pedko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> orderPrev;
        Node<E> orderNext;

        Node(E value) {
            this.value = value;
        }
    }

    private final int STD_SIZE = 64;
    private Node<E>[] table = (Node<E>[]) new Node[STD_SIZE];
    private int size = 0;

    private Node<E> orderHead;   // первый добавленный
    private Node<E> orderTail;   // последний добавленный

    private int findIndex(Object o) {
        return (o.hashCode() % table.length + table.length) % table.length;
    }

    private void linkOrder(Node<E> node) {
        node.orderPrev = orderTail;
        if (orderTail == null) {
            orderHead = node;
        } else {
            orderTail.orderNext = node;
        }
        orderTail = node;
    }

    private void unlinkOrder(Node<E> node) {
        if (node.orderPrev == null) {
            orderHead = node.orderNext;
        } else {
            node.orderPrev.orderNext = node.orderNext;
        }
        if (node.orderNext == null) {
            orderTail = node.orderPrev;
        } else {
            node.orderNext.orderPrev = node.orderPrev;
        }
        node.orderPrev = null;
        node.orderNext = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        int index = findIndex(e);
        Node<E> curr = table[index];
        while (curr != null) {
            if (curr.value.equals(e)) {
                return false;
            }
            curr = curr.next;
        }
        Node<E> node = new Node<>(e);
        node.next = table[index];
        table[index] = node;
        linkOrder(node);          // добавляем в конец списка порядка
        size++;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        orderHead = null;
        orderTail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(Object o) {
        int index = findIndex(o);
        Node<E> curr = table[index];
        Node<E> prev = null;
        while (curr != null) {
            if (curr.value.equals(o)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                unlinkOrder(curr);     // убираем и из списка порядка
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = findIndex(o);
        for (Node<E> curr = table[index]; curr != null; curr = curr.next) {
            if (curr.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            Node<E> prev = null;
            while (curr != null) {
                if (c.contains(curr.value)) {
                    Node<E> next = curr.next;
                    if (prev == null) {
                        table[i] = next;
                    } else {
                        prev.next = next;
                    }
                    unlinkOrder(curr);
                    size--;
                    modified = true;
                    curr = next;
                } else {
                    prev = curr;
                    curr = curr.next;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            Node<E> prev = null;
            while (curr != null) {
                if (!c.contains(curr.value)) {
                    Node<E> next = curr.next;
                    if (prev == null) {
                        table[i] = next;
                    } else {
                        prev.next = next;
                    }
                    unlinkOrder(curr);
                    size--;
                    modified = true;
                    curr = next;
                } else {
                    prev = curr;
                    curr = curr.next;
                }
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = orderHead;
        while (curr != null) {
            sb.append(curr.value);
            if (curr.orderNext != null) {
                sb.append(", ");
            }
            curr = curr.orderNext;
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}