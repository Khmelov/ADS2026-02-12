package by.it.group510902.shimul.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyLinkedHashSet<E> implements Set<E> {

    // Узел содержит ссылки как для разрешающей коллизии цепочки (next),
    // так и для глобального порядка добавления (before / after)
    private static class Node<E> {
        E data;
        Node<E> next;   // Односвязный список для коллизий в бакете

        Node<E> before; // Ссылки для двусвязного списка порядка вставки
        Node<E> after;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    private int size = 0;

    // Голова и хвост двусвязного списка для сохранения порядка добавления
    private Node<E> head;
    private Node<E> tail;

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7fffffff) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        table = (Node<E>[]) new Node[table.length * 2];

        // Повторно распределяем узлы по новому массиву,
        // сохраняя существующие объекты Node и их глобальные связи before/after
        Node<E> current = head;
        while (current != null) {
            int index = getIndex(current.data);
            current.next = table[index];
            table[index] = current;
            current = current.after;
        }
    }

    // Вспомогательный метод связывания узла в конец глобального списка
    private void linkLast(Node<E> node) {
        Node<E> t = tail;
        tail = node;
        if (t == null) {
            head = node;
        } else {
            t.after = node;
            node.before = t;
        }
    }

    // Вспомогательный метод извлечения узла из глобального списка
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

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
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
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        linkLast(newNode); // Добавляем узел в конец глобального списка порядка
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                // Удаляем из односвязного списка коллизий
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                // Удаляем из двусвязного списка порядка добавления
                unlink(current);

                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Node<E> current = head;
        while (current != null) {
            sb.append(current.data == this ? "(this Collection)" : current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append(']');
        return sb.toString();
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
        Node<E> current = head;

        while (current != null) {
            Node<E> nextNode = current.after; // Запоминаем следующий до возможного удаления
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////   Заглушки для остальной части интерфейса java.util.Set   ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
