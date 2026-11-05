package by.it.group510901.chibisov.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {

    private static final int MAX_LEVEL = 16;
    private final Random random = new Random();

    private static class Node {
        Object value;
        Node[] next;
        public Node(Object value, int level) {
            this.value = value;
            this.next = new Node[level];
        }
    }

    private final Node head = new Node(null, MAX_LEVEL);
    private int level;
    private int size;

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<? super E>) first).compareTo((E) second);
    }

    private int randomLevel() {
        int l = 1;
        while (l < MAX_LEVEL && random.nextBoolean()) {
            l++;
        }
        return l;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node current = head.next[0];
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            current = current.next[0];
            first = false;
        }
        sb.append(']');
        return sb.toString();
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
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();

        Node[] update = new Node[level];

        Node current = head;
        for (int i = level - 1; i >= 0; i--) {
            while (current.next[i] != null && compare(current.next[i].value, e) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }
        if (current.next[0] != null && compare(current.next[0].value, e) == 0) return false;

        int newNodeLevel = randomLevel();
        Node newNode = new Node(e, newNodeLevel);

        for (int i = 0; i < Math.min(level, newNodeLevel); i++) {
            newNode.next[i] = update[i].next[i];
            update[i].next[i] = newNode;
        }

        for (int i = level; i < newNodeLevel; i++) {
            head.next[i] = newNode;
        }

        level = Math.max(level, newNodeLevel);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        Node[] update = new Node[level];

        Node current = head;
        for (int i = level - 1; i >= 0; i--) {
            while (current.next[i] != null && compare(current.next[i].value, o) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }

        Node target = current.next[0];
        if (target == null || compare(target.value, o) != 0) return false;

        for (int i = 0; i < target.next.length; i++) {
            update[i].next[i] = target.next[i];
        }

        while (level > 0 && head.next[level - 1] == null) {
            level--;
        }
        size--;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < level; i++) {
            head.next[i] = null;
        }
        level = 0;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        Node current = head;
        for (int i = level - 1; i >= 0; i--) {
            while (current.next[i] != null && compare(current.next[i].value, o) < 0) {
                current = current.next[i];
            }
        }
        return current.next[0] != null && compare(current.next[0].value, o) == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) return false;
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean keepIfContains) {
        if (c == null) throw new NullPointerException();
        Node current = head.next[0];
        boolean modified = false;
        while (current != null) {
            Node next = current.next[0];
            if (c.contains(current.value) != keepIfContains) {
                remove(current.value);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private Node cursor = head.next[0];

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor == null) throw new NoSuchElementException();
            E e = (E) cursor.value;
            cursor = cursor.next[0];
            return e;
        }
    }
}
