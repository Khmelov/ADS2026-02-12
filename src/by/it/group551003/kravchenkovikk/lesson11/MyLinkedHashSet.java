package by.it.group551003.kravchenkovikk.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    private Node<E>[] hashSet;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    static class Node<E> {
        E data;
        int hashIndex;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        public Node(E data, int hashIndex) {
            this.data = data;
            this.hashIndex = hashIndex;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
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
        Node<E> curr = head;
        while (curr != null) {
            str.append(curr.data);
            if (curr.after != null) {
                str.append(", ");
            }
            curr = curr.after;
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
        head = null;
        tail = null;
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
        Node<E> newNode = new Node<>((E)e, index);
        newNode.next = hashSet[index];
        hashSet[index] = newNode;
        if (tail == null) {
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
                if (curr.before != null) {
                    curr.before.after = curr.after;
                } else {
                    head = curr.after;
                }
                if (curr.after != null) {
                    curr.after.before = curr.before;
                } else {
                    tail = curr.before;
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

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj: c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (Object obj: c) {
            add(obj);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (size == 0) {
            return  false;
        }
        Node<E> curr = head;
        while (curr != null) {
            Node<E> nextNode = curr.after;
            if (c.contains(curr.data)) {
                remove(curr.data);
            }
            curr = nextNode;
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (size == 0) {
            return false;
        }
        Node<E> curr = head;
        while (curr != null) {
            Node<E> nextNode = curr.after;
            if (!c.contains(curr.data)) {
                remove(curr.data);
            }
            curr = nextNode;
        }
        return true;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
