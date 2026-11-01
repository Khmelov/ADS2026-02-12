package by.it.group510901.petsevich.lesson11;

import java.util.*;

/*
size()
clear()
isEmpty()
add(Object)
remove(Object)
contains(Object)
*/

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 1000;
    private static final float LOAD_FACTOR = 0.75f;

    private ListNode<E>[] buckets;
    private int size;
    private float threshold;

    MyHashSet() {
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
        buckets = new ListNode[DEFAULT_CAPACITY];
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
        ListNode<E> current = buckets[idx];
        while (current != null) {
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
        ListNode<E> node = new ListNode<E>(e, hash);
        if(buckets[idx] == null) {
            buckets[idx] = node;
            size++;
            return true;
        }

        ListNode<E> current = buckets[idx];
        while(current != null) {
            if(Objects.equals(current.value, e))
                return false;
            current = current.next;
        }
        node.next = buckets[idx];
        buckets[idx] = node;
        size++;

        if(size > threshold)
            resize();

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = o.hashCode();
        int idx = hash % buckets.length;
        ListNode<E> current = buckets[idx];

        if(current != null && Objects.equals(current.value, o)) {
            buckets[idx] = buckets[idx].next;
            size--;
            return true;
        }

        while (current != null) {
            if(current.next != null && Objects.equals(current.next.value, o)) {
                current.next = current.next.next;
                size--;
                return true;
            }
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
        for(E e : c)
            add(e);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Boolean isChanged = false;
        for(Object o : c)
            isChanged = isChanged || remove(o);
        return isChanged;
    }

    @Override
    public void clear() {
        for(int i = 0; i < buckets.length; i++)
            buckets[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        int it = 0;
        for(int i = 0; i < buckets.length; i++) {
            if(buckets[i] != null) {
                ListNode<E> current = buckets[i];
                while (current != null) {
                    builder.append(current.value);
                    if(it < size - 1)
                        builder.append(", ");
                    current = current.next;
                    it++;
                }
            }
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

    private void resize() {
        int newCap = 2 * buckets.length;
        if(newCap <= 0)
            throw  new IllegalStateException("Set too large");

        ListNode<E>[] newBuckets = (ListNode<E>[]) new ListNode[newCap];
        for(ListNode<E> bucket : buckets) {
            while(bucket != null) {
                int idx = bucket.hash % newCap;
                ListNode<E> next = bucket.next;
                bucket.next = newBuckets[idx];
                newBuckets[idx] = bucket;
                bucket = next;
            }
        }

        buckets = newBuckets;
        threshold = newCap * LOAD_FACTOR;
    }
}

class ListNode<E> {
    E value;
    int hash;
    ListNode<E> next;
    
    ListNode(E value, int hash) {
        this.value = value;
        next = null;
    }
}

