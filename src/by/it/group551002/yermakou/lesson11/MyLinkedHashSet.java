package by.it.group551002.yermakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node {
        Object value;
        Node nextInBucket;
        Node nextInOrder;

        Node(Object value) {
            this.value = value;
        }
    }

    private Node[] table = new Node[16];
    private Node first, last;
    private int size;

    private int index(Object o) {
        return o == null ? 0 : (o.hashCode() & 0x7fffffff) % table.length;
    }

    private boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        table = new Node[16];
        first = last = null;
        size = 0;
    }

    public boolean contains(Object o) {
        for (Node n = table[index(o)]; n != null; n = n.nextInBucket) {
            if (eq(o, n.value)) return true;
        }
        return false;
    }

    public boolean add(E e) {
        if (contains(e)) return false;
        Node n = new Node(e);
        int i = index(e);
        n.nextInBucket = table[i];   // в начало корзины
        table[i] = n;
        if (last == null) first = n; // в конец порядка
        else last.nextInOrder = n;
        last = n;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if (!contains(o)) return false;

        int i = index(o);
        Node prev = null, n = table[i];
        while (!eq(o, n.value)) {
            prev = n;
            n = n.nextInBucket;
        }
        if (prev == null) table[i] = n.nextInBucket;
        else prev.nextInBucket = n.nextInBucket;

        prev = null;
        n = first;
        while (!eq(o, n.value)) {
            prev = n;
            n = n.nextInOrder;
        }
        if (prev == null) first = n.nextInOrder;
        else prev.nextInOrder = n.nextInOrder;
        if (n == last) last = prev;

        size--;
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : toArray()) {
            if (!c.contains(o)) {
                remove(o);
                changed = true;
            }
        }
        return changed;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int k = 0;
        for (Node n = first; n != null; n = n.nextInOrder) {
            result[k++] = n.value;
        }
        return result;
    }

    public String toString() {
        String s = "[";
        for (Node n = first; n != null; n = n.nextInOrder) {
            s += n.value;
            if (n.nextInOrder != null) s += ", ";
        }
        return s + "]";
    }

    // не требуются по заданию
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}