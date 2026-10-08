package by.it.group551002.puteev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    // Узел совмещает в себе односвязный список для коллизий (next)
    // и двусвязный список для сохранения порядка добавления (after, before)
    private static class Node<E> {
        E data;
        Node<E> next;   // Следующий элемент в цепочке коллизий (хэш-таблица)
        Node<E> before; // Предыдущий добавленный элемент в сет
        Node<E> after;  // Следующий добавленный элемент в сет

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size = 0;

    // Указатели на начало и конец двусвязного списка порядка добавления
    private Node<E> head = null;
    private Node<E> tail = null;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    // Связывание нового узла в конец двусвязного списка порядка
    private void linkLast(Node<E> node) {
        Node<E> last = tail;
        tail = node;
        if (last == null) {
            head = node;
        } else {
            last.after = node;
            node.before = last;
        }
    }

    // Исключение узла из двусвязного списка порядка при удалении
    private void unlink(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        for (Node<E> node = head; node != null; node = node.after) {
            int index = getIndex(node.data);
            node.next = table[index];
            table[index] = node;
        }
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
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if (e == null ? current.data == null : e.equals(current.data)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        linkLast(newNode);
        size++;

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                unlink(current);
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
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return true;
            }
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
        while (current != null) {
            sb.append(current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
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
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                  Заглушки интерфейса Set                   ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}