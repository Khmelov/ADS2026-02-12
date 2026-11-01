package by.it.group551004.karpovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private static class Node<E> {
        E value;
        int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static int hashOf(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(cur.hash, newTable.length);
                cur.next = newTable[idx];
                newTable[idx] = cur;
                cur = next;
            }
        }
        table = newTable;
    }

    private void unlinkOrder(Node<E> node) {
        if (node.before == null) {
            head = node.after;
        } else {
            node.before.after = node.after;
        }
        if (node.after == null) {
            tail = node.before;
        } else {
            node.after.before = node.before;
        }
        node.before = null;
        node.after = null;
    }

    private void removeNode(Node<E> target) {
        int idx = indexFor(target.hash, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur == target) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                break;
            }
            prev = cur;
            cur = cur.next;
        }
        unlinkOrder(target);
        size--;
    }

    private Node<E> find(Object o) {
        int hash = hashOf(o);
        Node<E> cur = table[indexFor(hash, table.length)];
        while (cur != null) {
            if (cur.hash == hash && same(o, cur.value)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    @Override
    public String toString() {
        String result = "[";
        Node<E> cur = head;
        while (cur != null) {
            result += cur.value;
            if (cur.after != null) {
                result += ", ";
            }
            cur = cur.after;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return find(o) != null;
    }

    @Override
    public boolean add(E element) {
        if (find(element) != null) {
            return false;
        }
        int hash = hashOf(element);
        int idx = indexFor(hash, table.length);
        Node<E> node = new Node<>(element, hash, table[idx]);
        table[idx] = node;
        node.before = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> node = find(o);
        if (node == null) {
            return false;
        }
        removeNode(node);
        return true;
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
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (c.contains(cur.value)) {
                removeNode(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (!c.contains(cur.value)) {
                removeNode(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            result[i++] = cur.value;
            cur = cur.after;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;
            private Node<E> last = null;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                last = next;
                next = next.after;
                return last.value;
            }

            @Override
            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                removeNode(last);
                last = null;
            }
        };
    }
}