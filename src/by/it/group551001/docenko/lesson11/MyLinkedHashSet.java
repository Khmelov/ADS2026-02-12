package by.it.group551001.docenko.lesson11;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        final int hash;
        final E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int modCount = 0;

    private static int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        return h ^ (h >>> 16);
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static boolean isEqual(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void resize() {
        Node<E>[] bigger = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> x = head; x != null; x = x.after) {
            int index = indexFor(x.hash, bigger.length);
            x.next = bigger[index];
            bigger[index] = x;
        }
        table = bigger;
        threshold = (int) (bigger.length * LOAD_FACTOR);
    }

    private void linkLast(Node<E> x) {
        if (tail == null) {
            head = x;
        } else {
            tail.after = x;
            x.before = tail;
        }
        tail = x;
    }

    private void unlinkOrder(Node<E> x) {
        if (x.before == null) {
            head = x.after;
        } else {
            x.before.after = x.after;
        }
        if (x.after == null) {
            tail = x.before;
        } else {
            x.after.before = x.before;
        }
        x.before = null;
        x.after = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = head; x != null; x = x.after) {
            if (x != head) {
                sb.append(", ");
            }
            sb.append(x.item);
        }
        return sb.append(']').toString();
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
        modCount++;
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int index = indexFor(h, table.length);
        for (Node<E> x = table[index]; x != null; x = x.next) {
            if (x.hash == h && isEqual(e, x.item)) {
                return false;
            }
        }
        Node<E> node = new Node<>(h, e, table[index]);
        table[index] = node;
        linkLast(node);
        size++;
        modCount++;
        if (size > threshold) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = indexFor(h, table.length);
        Node<E> prev = null;
        for (Node<E> x = table[index]; x != null; prev = x, x = x.next) {
            if (x.hash == h && isEqual(o, x.item)) {
                if (prev == null) {
                    table[index] = x.next;
                } else {
                    prev.next = x.next;
                }
                x.next = null;
                unlinkOrder(x);
                size--;
                modCount++;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        for (Node<E> x = table[indexFor(h, table.length)]; x != null; x = x.next) {
            if (x.hash == h && isEqual(o, x.item)) {
                return true;
            }
        }
        return false;
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
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.after;
            if (!c.contains(x.item)) {
                remove(x.item);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            result[i++] = x.item;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            a[i++] = (T) x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Collection<?> other = (Collection<?>) o;
        return other.size() == size && containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            h += x.item == null ? 0 : x.item.hashCode();
        }
        return h;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;
            private Node<E> lastReturned;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if (next == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = next;
                next = next.after;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                MyLinkedHashSet.this.remove(lastReturned.item);
                lastReturned = null;
                expectedModCount = modCount;
            }
        };
    }
}
