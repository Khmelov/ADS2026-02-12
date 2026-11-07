package by.it.group551001.bochilo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    //Создайте аналог хеш-множества с сохранением порядка добавления
    //БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node<E> {
        E item;
        Node<E> next;   // следующий в бакете (коллизии)
        Node<E> before; // предыдущий по порядку добавления
        Node<E> after;  // следующий по порядку добавления

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    private boolean equalsElement(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (Node<E> node = head; node != null; node = node.after) {
            int index = indexFor(node.item, newTable.length);
            node.next = newTable[index];
            newTable[index] = node;
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
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = head; node != null; node = node.after) {
            sb.append(node.item);
            if (node.after != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Node<E>[]) new Node[16];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = indexFor(e, table.length);
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(e, node.item)) {
                return false;
            }
        }
        Node<E> node = new Node<>(e, table[index]);
        table[index] = node;
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
        int index = indexFor(o, table.length);
        Node<E> prev = null;
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                unlinkOrder(node);
                size--;
                return true;
            }
            prev = node;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = indexFor(o, table.length);
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
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
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> after = node.after;
            if (!c.contains(node.item)) {
                remove(node.item);
                modified = true;
            }
            node = after;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E item = current.item;
                current = current.after;
                return item;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            result[i++] = node.item;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            a[i++] = (T) node.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
