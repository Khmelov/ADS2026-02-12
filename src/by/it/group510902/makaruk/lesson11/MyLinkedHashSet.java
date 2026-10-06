package by.it.group510902.makaruk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Класс узла, который одновременно участвует в цепочке коллизий и общем списке порядка добавления
    private static class Node<E> {
        E data;
        Node<E> next;     // Ссылка на следующий узел в бакете (для коллизий)

        // Ссылки для поддержания общего двунаправленного списка порядка вставки:
        Node<E> before;
        Node<E> after;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    // Указатели на начало и конец списка в порядке добавления элементов
    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    private int getIndex(Object o) {
        if (o == null) return 0;
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    // Служебная линковка нового узла в конец общего списка порядка добавления
    private void linkAtTail(Node<E> node) {
        Node<E> t = tail;
        tail = node;
        if (t == null) {
            head = node;
        } else {
            t.after = node;
            node.before = t;
        }
    }

    // Служебное исключение узла из общего списка порядка при его удалении
    private void unlinkNode(Node<E> node) {
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

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        // ВАЖНО: Перехеширование идет строго по общему связному списку, чтобы сохранить правильные связи
        Node<E> current = head;
        while (current != null) {
            int index = getIndex(current.data);
            current.next = table[index];
            table[index] = current;
            current = current.after;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        // Обходим элементы строго по цепочке добавления (от head к tail)
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }
        sb.append("]");
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
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if (e == null ? current.data == null : e.equals(current.data)) {
                return false; // Дубликаты игнорируем
            }
            current = current.next;
        }

        // Создаем ноду и пушим ее в начало бакета хеш-таблицы
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        // Дописываем ноду в конец списка порядка вставки
        linkAtTail(newNode);
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
                // Извлекаем из бакета коллизий
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                // Извлекаем из списка порядка добавления
                unlinkNode(current);
                size--;
                return true;
            }
            previous = current;
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
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Обходим элементы с конца списка порядка вставки, чтобы удаление не ломало курсор ссылки
        Node<E> current = tail;
        while (current != null) {
            Node<E> before = current.before; // Запоминаем левый шаг заранее
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = before;
        }
        return modified;
    }

    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}