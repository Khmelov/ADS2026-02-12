package by.it.group510901.gulchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node<E>[] buckets = (Node<E>[]) new Node[16];
    private int size = 0;

    private int getIndex(Object element, int capacity) {
        int hash = element == null ? 0 : element.hashCode();
        return (hash & 0x7fffffff) % capacity;
    }

    private void resize() {
        Node<E>[] newBuckets = (Node<E>[]) new Node[buckets.length * 2];

        for (int i = 0; i < buckets.length; i++) {
            Node<E> current = buckets[i];

            while (current != null) {
                Node<E> next = current.next;
                int index = getIndex(current.element, newBuckets.length);

                current.next = newBuckets[index];
                newBuckets[index] = current;

                current = next;
            }
        }

        buckets = newBuckets;
    }

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;

        for (int i = 0; i < buckets.length; i++) {
            Node<E> current = buckets[i];

            while (current != null) {
                if (!first) {
                    result += ", ";
                }

                result += current.element == this
                        ? "(this Collection)"
                        : current.element;

                first = false;
                current = current.next;
            }
        }

        return result + "]";
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) {
            return false;
        }

        if (size + 1 > buckets.length * 3 / 4) {
            resize();
        }

        int index = getIndex(element, buckets.length);
        buckets[index] = new Node<>(element, buckets[index]);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o, buckets.length);
        Node<E> current = buckets[index];
        Node<E> previous = null;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o, buckets.length);
        Node<E> current = buckets[index];

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}