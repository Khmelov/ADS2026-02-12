package by.it.group551004.frantskevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // next - односвязный список корзины (коллизии),
    // before/after - список всех элементов в порядке добавления
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = new Node[16];
    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    private int bucket(Object o, int length) {
        int h = o == null ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % length;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void resize() {
        Node<E>[] table_new = new Node[table.length * 2];
        for (Node<E> x = first; x != null; x = x.after) {
            int i = bucket(x.value, table_new.length);
            x.next = table_new[i];
            table_new[i] = x;
        }
        table = table_new;
    }

    // убирает узел из списка порядка добавления
    private void unlinkOrder(Node<E> x) {
        if (x.before == null) {
            first = x.after;
        } else {
            x.before.after = x.after;
        }
        if (x.after == null) {
            last = x.before;
        } else {
            x.after.before = x.before;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = first; x != null; x = x.after) {
            if (x != first) {
                sb.append(", ");
            }
            sb.append(x.value);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
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
    public boolean add(E e) {
        int i = bucket(e, table.length);
        for (Node<E> x = table[i]; x != null; x = x.next) {
            if (same(e, x.value)) {
                return false;
            }
        }
        Node<E> node = new Node<>(e, table[i]);
        table[i] = node;
        node.before = last;
        if (last == null) {
            first = node;
        } else {
            last.after = node;
        }
        last = node;
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = bucket(o, table.length);
        Node<E> prev = null;
        for (Node<E> x = table[i]; x != null; prev = x, x = x.next) {
            if (same(o, x.value)) {
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
                }
                unlinkOrder(x);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = table[bucket(o, table.length)]; x != null; x = x.next) {
            if (same(o, x.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Node<E> x = first; x != null; ) {
            Node<E> after = x.after;
            if (!c.contains(x.value)) {
                remove(x.value);
                modified = true;
            }
            x = after;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.after) {
            result[i++] = x.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = first; x != null; x = x.after) {
            a[i++] = (T) x.value;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

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
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.after;
                return value;
            }
        };
    }
}
