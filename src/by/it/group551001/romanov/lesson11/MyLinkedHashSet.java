package by.it.group551001.romanov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;   // Для цепочки коллизий в хеш-таблице (односвязный)
        Node<E> before; // Для порядка добавления (двусвязный)
        Node<E> after;  // Для порядка добавления (двусвязный)

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table; // Сама хеш-таблица
    private Node<E> head;    // Указатель на первый добавленный элемент
    private Node<E> tail;    // Указатель на последний добавленный элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    private int getHash(Object o, int length) {
        if (o == null) return 0;
        return (o.hashCode() & 0x7FFFFFFF) % length;
    }

    // Увеличение таблицы для сохранения производительности
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];

        Node<E> current = head;
        while (current != null) {
            int index = getHash(current.data, newTable.length);
            // Кладем в новую таблицу, обновляя ТОЛЬКО ссылку next
            current.next = newTable[index];
            newTable[index] = current;

            current = current.after;
        }
        table = newTable;
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int index = getHash(e, table.length);
        Node<E> current = table[index];

        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if ((e == null && current.data == null) || (e != null && e.equals(current.data))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Если не нашли — создаем новый узел
        Node<E> newNode = new Node<>(e);

        // 1. Встраиваем узел в хеш-таблицу (в начало цепочки ячейки)
        newNode.next = table[index];
        table[index] = newNode;

        // 2. Встраиваем узел в глобальный двусвязный список (в самый конец)
        if (tail == null) {
            // Это первый элемент вообще
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;

        // Если нужно, расширяем таблицу
        if (size > table.length * 0.75) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getHash(o, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                // 1. Вырезаем из хеш-таблицы (цепочки next)
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // 2. Вырезаем из двусвязного списка (связи before и after)
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after; // Удаляли первый элемент
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before; // Удаляли последний элемент
                }

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
        int index = getHash(o, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                return true;
            }
            current = current.next;
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
        // Удаляем каждый элемент коллекции c
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
        // Идем по нашему списку порядка добавления
        while (current != null) {
            Node<E> nextNode = current.after; // Сохраняем следующий, т.к. текущий может удалиться
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    @Override
    public String toString() {
        if (head == null) return "[]";

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

    /////////////////////////////////////////////////////////////////////////
    //////    Методы-заглушки (требуются интерфейсом, но не заданием) ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}