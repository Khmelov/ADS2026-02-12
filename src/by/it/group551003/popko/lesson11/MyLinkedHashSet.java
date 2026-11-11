package by.it.group551003.popko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E item;
        int hash;
        Node<E> next;      // следующий в той же корзине
        Node<E> before;    // предыдущий по порядку добавления
        Node<E> after;     // следующий по порядку добавления

        Node(E item, int hash, Node<E> next) {
            this.item = item;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head;   // первый добавленный
    private Node<E> tail;   // последний добавленный
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size >= threshold) {
            resize();
        }
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && current.item.equals(element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element, hash, table[index]);
        table[index] = newNode;

        // добавляем в конец общего списка порядка
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && current.item.equals(o)) {
                // вырезаем из корзины
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                // вырезаем из общего списка порядка
                unlinkFromOrder(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && current.item.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Set                         ///////
    /////////////////////////////////////////////////////////////////////////

    private int hash(Object o) {
        int h = o.hashCode();
        h ^= (h >>> 16);
        return h;
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    private void unlinkFromOrder(Node<E> node) {
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
        Node<E>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                Node<E> nextInBucket = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = nextInBucket;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextInOrder = current.after;
            if (c.contains(current.item)) {
                // вырезаем из корзины
                removeFromBucket(current);
                // вырезаем из общего списка
                unlinkFromOrder(current);
                size--;
                changed = true;
            }
            current = nextInOrder;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextInOrder = current.after;
            if (!c.contains(current.item)) {
                removeFromBucket(current);
                unlinkFromOrder(current);
                size--;
                changed = true;
            }
            current = nextInOrder;
        }
        return changed;
    }

    private void removeFromBucket(Node<E> node) {
        int index = indexFor(node.hash, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (current == node) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                return;
            }
            prev = current;
            current = current.next;
        }
    }

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
}