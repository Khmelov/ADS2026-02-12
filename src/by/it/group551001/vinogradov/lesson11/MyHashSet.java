package by.it.group551001.vinogradov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[100];
    private int size;

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }
        int hash = element.hashCode();
        hash ^= hash >>> 16;

        return (hash & 0x7FFFFFFF) % table.length;
    }

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    public MyHashSet() {
        for (int i = 0; i < 100; i++) {
            table[i] = new Node<E>(null);
        }
    }
    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < 100; i++) {
            Node<E> curr = table[i].next;
            while (curr != null) {
                if (!s.equals("[")) {
                    s += ", ";
                }
                s += curr.value;
                curr = curr.next;
            }
        }
        return s + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < 100; i++) {
            table[i].next = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx];
        while(curr.next != null) {
            if (Objects.equals(element, curr.next.value))
                return false;
            curr = curr.next;
        }
        curr.next = new Node<E>(element);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx];
        while(curr.next != null) {
            if (Objects.equals(element, curr.next.value)) {
                curr.next = curr.next.next;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx].next;
        while(curr != null) {
            if (Objects.equals(element, curr.value))
                return true;
            curr = curr.next;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw notImplemented();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw notImplemented();
    }
}
