package by.it.group510901.gulchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node<E>[] buckets = (Node<E>[]) new Node[16];
    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    private int getIndex(Object element, int capacity) {
        int hash = element == null ? 0 : element.hashCode();
        return (hash & 0x7fffffff) % capacity;
    }

    private void resize() {
        Node<E>[] newBuckets = (Node<E>[]) new Node[buckets.length * 2];
        Node<E> current = first;

        while (current != null) {
            int index = getIndex(current.element, newBuckets.length);
            current.next = newBuckets[index];
            newBuckets[index] = current;
            current = current.after;
        }

        buckets = newBuckets;
    }

    private void unlinkOrder(Node<E> node) {
        if (node.before == null) {
            first = node.after;
        } else {
            node.before.after = node.after;
        }

        if (node.after == null) {
            last = node.before;
        } else {
            node.after.before = node.before;
        }

        node.before = null;
        node.after = null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        Node<E> current = first;

        while (current != null) {
            if (current != first) {
                result += ", ";
            }

            result += current.element == this
                    ? "(this Collection)"
                    : current.element;

            current = current.after;
        }

        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }

        first = null;
        last = null;
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
        Node<E> node = new Node<>(element, buckets[index]);
        buckets[index] = node;

        node.before = last;

        if (last == null) {
            first = node;
        } else {
            last.after = node;
        }

        last = node;
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

                unlinkOrder(current);
                current.next = null;
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

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == this) {
            return true;
        }

        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == this) {
            return false;
        }

        boolean changed = false;

        for (E element : c) {
            if (add(element)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            boolean changed = size > 0;
            clear();
            return changed;
        }

        boolean changed = false;
        Node<E> current = first;

        while (current != null) {
            Node<E> next = current.after;

            if (c.contains(current.element)) {
                remove(current.element);
                changed = true;
            }

            current = next;
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c == this) {
            return false;
        }

        boolean changed = false;
        Node<E> current = first;

        while (current != null) {
            Node<E> next = current.after;

            if (!c.contains(current.element)) {
                remove(current.element);
                changed = true;
            }

            current = next;
        }

        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new java.util.NoSuchElementException();
                }

                E element = current.element;
                current = current.after;
                return element;
            }
        };
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