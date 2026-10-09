package by.it.group551002.savitsky.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> hashNext;
        Node<E> orderPrev;
        Node<E> orderNext;

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E>[] buckets;
    private int size;

    private Node<E> orderHead;
    private Node<E> orderTail;

    public MyLinkedHashSet() {
        buckets = new Node[1000];
        size = 0;
    }

    private int indexFor(Object o) {
        int hash = o.hashCode();
        return (hash & 0x7fffffff) % buckets.length;
    }

    @Override
    public String toString() {
        String result = "[";
        Node<E> current = orderHead;

        while (current != null) {
            result = result + current.item;

            if (current.orderNext != null) {
                result = result + ", ";
            }

            current = current.orderNext;
        }

        return result + "]";
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
        Arrays.fill(buckets, null);

        orderHead = null;
        orderTail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = indexFor(o);
        Node<E> current = buckets[index];

        while (current != null) {

            if (current.item.equals(o)) {
                return true;
            }

            current = current.hashNext;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (contains(e)) {
            return false;
        }

        Node<E> node = new Node<>(e);

        int index = indexFor(e);
        node.hashNext = buckets[index];
        buckets[index] = node;

        if (size == 0) {
            orderHead = node;
            orderTail = node;
        } else {
            node.orderPrev = orderTail;
            orderTail.orderNext = node;
            orderTail = node;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o);
        Node<E> current = buckets[index];
        Node<E> prevInBucket = null;

        while (current != null) {

            if (current.item.equals(o)) {

                if (prevInBucket == null) {
                    buckets[index] = current.hashNext;
                } else {
                    prevInBucket.hashNext = current.hashNext;
                }

                if (current.orderPrev == null) {
                    orderHead = current.orderNext;
                } else {
                    current.orderPrev.orderNext = current.orderNext;
                }

                if (current.orderNext == null) {
                    orderTail = current.orderPrev;
                } else {
                    current.orderNext.orderPrev = current.orderPrev;
                }

                size--;
                return true;
            }

            prevInBucket = current;
            current = current.hashNext;
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
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, true);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, false);
    }

    private boolean filter(Collection<?> c, boolean keepIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;

        Node<E> current = orderHead;
        while (current != null) {
            Node<E> next = current.orderNext;
            boolean inC = c.contains(current.item);
            if (inC != keepIfContained) {
                remove(current.item);
                changed = true;
            }

            current = next;
        }

        return changed;
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
    public <T> T[] toArray(T[] a) {
        return null;
    }
}