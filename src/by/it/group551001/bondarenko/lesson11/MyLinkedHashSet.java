package by.it.group551001.bondarenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел, совмещающий ссылки для хэш-таблицы (next)
    // и для двунаправленного списка порядка добавления (before, after)
    private static class Node<E> {
        E value;
        Node<E> next;      // Связь в цепочке коллизий хэш-таблицы
        Node<E> before;    // Связь с предыдущим добавленным элементом
        Node<E> after;     // Связь со следующим добавленным элементом

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head; // Голова глобального списка (самый старый элемент)
    private Node<E> tail; // Хвост глобального списка (самый новый элемент)
    private int size = 0;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    private int getIndex(Object o) {
        if (o == null) return 0;
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Обходим элементы в порядке добавления через head/after
        Node<E> current = head;
        while (current != null) {
            int newIndex = (current.value == null) ? 0 : (current.value.hashCode() & 0x7FFFFFFF) % newCapacity;
            current.next = newTable[newIndex];
            newTable[newIndex] = current;
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

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        // Проверяем наличие элемента в хэш-таблице
        while (current != null) {
            if (element == null ? current.value == null : element.equals(current.value)) {
                return false;
            }
            current = current.next;
        }

        // Вставляем узел в бакет хэш-таблицы
        Node<E> newNode = new Node<>(element, table[index]);
        table[index] = newNode;

        // Связываем узел в конец двунаправленного списка
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;

        if ((float) size / table.length >= LOAD_FACTOR) {
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
            if (o == null ? current.value == null : o.equals(current.value)) {
                // 1. Удаляем из цепочки хэш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // 2. Удаляем из двунаправленного списка порядка добавления
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
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
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return true;
            }
            current = current.next;
        }

        return false;
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
            Node<E> next = current.after;
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Заглушки для остатка интерфейса Set<E>            ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}