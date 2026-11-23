package by.it.group551001.akynchits.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Внутренний класс узла
    private static class Node<E> {
        final E item;
        final int hash;

        Node<E> next;    // Для разрешения коллизий (односвязный список в бакете)
        Node<E> before;  // Для поддержания порядка вставки (двусвязный список)
        Node<E> after;   // Для поддержания порядка вставки (двусвязный список)

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
            this.before = null;
            this.after = null;
        }
    }

    private Node<E>[] table;
    private Node<E> head; // Первый добавленный элемент
    private Node<E> tail; // Последний добавленный элемент
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    // =====================================================================
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ИЗ ЗАДАНИЯ
    // =====================================================================

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
        int h = hash(e);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];

        // Проверка на дубликат в цепочке коллизий
        while (current != null) {
            if (current.hash == h && (current.item == e || (e != null && e.equals(current.item)))) {
                return false; // Элемент уже есть, порядок не меняем
            }
            current = current.next;
        }

        // Создаем новый узел и добавляем его в начало цепочки коллизий
        Node<E> newNode = new Node<>(h, e, table[i]);
        table[i] = newNode;

        // Добавляем узел в конец списка порядка вставки
        if (tail == null) {
            head = tail = newNode;
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
        int h = hash(o);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == h && (current.item == o || (o != null && o.equals(current.item)))) {

                // 1. Удаляем из цепочки коллизий (бакета)
                if (prev == null) {
                    table[i] = current.next;
                } else {
                    prev.next = current.next;
                }

                // 2. Удаляем из списка порядка вставки
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after; // Удаляли головной элемент
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before; // Удаляли хвостовой элемент
                }

                // Помощь сборщику мусора
                current.next = null;
                current.before = null;
                current.after = null;

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
        int h = hash(o);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];

        while (current != null) {
            if (current.hash == h && (current.item == o || (o != null && o.equals(current.item)))) {
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

        String result = "[";
        boolean first = true;

        // Итерируемся по списку порядка вставки (head -> after), а не по таблице!
        Node<E> current = head;
        while (current != null) {
            if (!first) {
                result += ", ";
            }
            result += current.item;
            first = false;
            current = current.after;
        }

        return result + "]";
    }

    // =====================================================================
    // МАССОВЫЕ ОПЕРАЦИИ
    // =====================================================================

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        // Безопасная итерация по списку порядка вставки
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after; // Сохраняем ссылку на следующий ДО возможного удаления
            if (c.contains(current.item)) {
                remove(current.item); // Используем наш метод remove, он корректно обновит все связи
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.item)) {
                remove(current.item);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // =====================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =====================================================================

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    // =====================================================================
    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ИНТЕРФЕЙСА (для полной совместимости)
    // =====================================================================

    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            result[index++] = current.item;
            current = current.after;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            a[index++] = (T) current.item;
            current = current.after;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private class LinkedHashSetIterator implements Iterator<E> {
        private Node<E> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = current.item;
            current = current.after;
            return item;
        }
    }
}
