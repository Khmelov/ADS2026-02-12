package by.it.group510901.petsevich.lesson11;

import java.util.*;

/*
toString()
size()
clear()
isEmpty()
add(Object)
remove(Object)
contains(Object)

containsAll(Collection)
addAll(Collection)
removeAll(Collection)
retainAll(Collection)
 */

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] buckets;
    private Node<E> head, tail;
    private int size;
    private float threshold;

    MyLinkedHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = LOAD_FACTOR * DEFAULT_CAPACITY;
        head = tail = null;
        size = 0;
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
    public boolean contains(Object o) {
        int hash = o.hashCode();
        int idx = hash % buckets.length;
        Node<E> current = buckets[idx];
        while(current != null) {
            if(Objects.equals(current.value, o))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = e.hashCode();
        int idx = hash % buckets.length;
        Node<E> current = buckets[idx];
        while(current != null) {
            if(Objects.equals(current.value, e))
                return false;
            current = current.next;
        }

        Node<E> node = new Node<E>(e, hash, buckets[idx]);
        node.next = buckets[idx];
        buckets[idx] = node;
        linkLast(node);
        size++;

        if(size > threshold)
            resize();
        return true;
    }

    private void linkLast(Node<E> node) {
        Node<E> oldTail = tail;
        node.after = null;
        node.before = oldTail;
        tail = node;
        if(oldTail == null) head = node;
        else oldTail.after = node;
    }

    private void unlinkOrder(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if(before == null) {
            head = after;
            if(head != null) head.before = null;
        }
        else before.after = after;

        if(after == null) {
            tail = before;
            if(tail != null) tail.after = null;
        }
        else after.before = before;
    }

    private void resize() {
        int newCap = 2 * buckets.length;
        if(newCap <= 0)
            throw  new IllegalStateException("Set too large");

        Node<E>[] newBuckets = (Node<E>[]) new Node[newCap];
        for(Node<E> bucket : buckets) {
            while(bucket != null) {
                int idx = bucket.hash % newCap;
                Node<E> next = bucket.next;
                bucket.next = newBuckets[idx];
                newBuckets[idx] = bucket;
                bucket = next;
            }
        }

        buckets = newBuckets;
        threshold = newCap * LOAD_FACTOR;
    }

    @Override
    public boolean remove(Object o) {
        int hash = o.hashCode();
        int idx = hash % buckets.length;

        Node<E> prev = null;
        Node<E> current = buckets[idx];
        while(current != null) {
            if(Objects.equals(current.value, o)) {
                if(prev == null) buckets[idx] = current.next;
                else prev.next = current.next;

                unlinkOrder(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isChanged = false;
        for(E e : c) {
            if(add(e))
                isChanged = true;
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        Node<E> current = head;
        while(current != null) {
            Node<E> after = current.after;
            if(!c.contains(current.value)) {
                remove(current.value);
                isChanged = true;
            }
            current = after;
        }
        return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for(Object o : c) {
            if(remove(o))
                isChanged = true;
        }
        return isChanged;
    }

    @Override
    public void clear() {
        for(int i = 0; i < buckets.length; i++)
            buckets[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        Node<E> current = head;
        while (current != null) {
            builder.append(current.value);
            if(current.after != null)
                builder.append(", ");
            current = current.after;
        }
        builder.append(']');
        return builder.toString();
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

class Node<E> {
    E value;
    int hash;
    Node<E> before;
    Node<E> after;
    Node<E> next;

    Node(E value, int hash, Node<E> next) {
        this.value = value;
        this.hash = hash;
        this.next = next;
    }
}
