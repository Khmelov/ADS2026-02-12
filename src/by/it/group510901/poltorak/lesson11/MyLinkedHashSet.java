package by.it.group510901.poltorak.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;
        Node<E> before, after;

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    private Node<E> head;
    private Node<E> tail;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Емкость должна быть больше 0");
        }
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int getIndex(int hash, int length) {
        return (length - 1) & hash;
    }

    private void linkNodeLast(Node<E> newNode) {
        Node<E> last = tail;
        tail = newNode;
        if (last == null) {
            head = newNode;
        } else {
            last.after = newNode;
            newNode.before = last;
        }
    }

    private void unlinkNode(Node<E> node) {
        Node<E> b = node.before;
        Node<E> a = node.after;

        if (b == null) {
            head = a;
        } else {
            b.after = a;
            node.before = null;
        }

        if (a == null) {
            tail = b;
        } else {
            a.before = b;
            node.after = null;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        Node<E> current = head;
        while (current != null) {
            int iNew = getIndex(current.hash, newCapacity);
            current.next = newTable[iNew];
            newTable[iNew] = current;
            current = current.after;
        }
        table = newTable;
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
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            head = null;
            tail = null;
            size = 0;
        }
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int index = getIndex(h, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == o || (o != null && o.equals(current.key)))) {
                return true;
            }
            current = current.next;
        }
        return false;
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

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int index = getIndex(h, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == e || (e != null && e.equals(current.key)))) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(h, e, table[index]);
        table[index] = newNode;

        linkNodeLast(newNode);
        size++;

        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = getIndex(h, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == h && (current.key == o || (o != null && o.equals(current.key)))) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                unlinkNode(current);

                current.next = null;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.key);
            first = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object e : c) {
            if (!contains(e)) {
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
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.after;
            if (!c.contains(current.key)) {
                remove(current.key);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }
}
