package by.it.group510902.shimul.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс для узла односвязного списка
    private static class Node<E> {
        E data;
        Node<E> next;

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

    // Вычисление индекса в массиве по хеш-коду
    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        // Маскируем знак, чтобы индекс всегда был положителен
        return (o.hashCode() & 0x7fffffff) % table.length;
    }

    // Увеличение размера массива при превышении порога загрузки
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0; // Пересчитается при повторном add

        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
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
            return false; // Элемент уже существует
        }

        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        // Вставляем новый элемент в начало списка (цепочки) по индексу
        table[index] = new Node<>(e, table[index]);
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
                if (previous == null) {
                    table[index] = current.next; // Удаление первого элемента цепочки
                } else {
                    previous.next = current.next; // Удаление из середины/конца
                }
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

        boolean first = true;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data == this ? "(this Collection)" : current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////   Заглушки для остальной части интерфейса java.util.Set   ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}
