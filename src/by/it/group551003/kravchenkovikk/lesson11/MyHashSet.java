package by.it.group551003.kravchenkovikk.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

public class MyHashSet<E> implements Set<E> {

    private Node<E> [] hashSet;
    private int size;

    static class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.hashSet = new Node[16];
        this.size = 0;
    }

    public int getIndex(E e) {
        if (e == null) {
            return 0;
        }
        return Math.abs(e.hashCode()) % hashSet.length;
    }

    @Override
    public String toString () {
        if (size == 0) {
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        boolean first = true;
        for (Node<E> curr: hashSet) {
            while (curr != null) {
                if (!first) {
                    str.append(", ");
                }
                str.append(curr.data);
                first = false;
                curr = curr.next;
            }
        }
        return str.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < hashSet.length; i++) {
            hashSet[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object e) {
        int index = getIndex((E)e);
        Node<E> curr = hashSet[index];
        while (curr != null) {
            if (e.equals(curr.data)) {
                return false;
            }
            curr = curr.next;
        }
        Node<E> newNode = new Node<E>((E)e);
        newNode.next = hashSet[index];
        hashSet[index] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex((E)o);
        Node<E> curr = hashSet[index];
        Node<E> prev = null;
        while (curr != null) {
            if (o.equals(curr.data)) {
                if (prev == null) {
                    hashSet[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
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
        int index = getIndex((E)o);
        Node<E> curr = hashSet[index];
        while (curr != null) {
            if (o.equals(curr.data)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}
