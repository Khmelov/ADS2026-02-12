package by.it.group551004.sheev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    // Начальная ёмкость таблицы (количество корзин)
    private static final int DEFAULT_CAPACITY = 32;
    // Коэффициент загрузки: при достижении size >= threshold таблица расширяется
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Узел односвязного списка — используется для разрешения коллизий методом цепочек
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;   // массив корзин (каждая корзина — голова списка)
    private int size;          // текущее количество элементов
    private int threshold;     // порог, при котором происходит resize
    private float loadFactor;  // коэффициент загрузки

    public MyHashSet() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        // Порог = capacity * loadFactor (например, 32 * 0.75 = 24)
        this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        // Просто обнуляем все корзины — сборщик мусора удалит узлы
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public boolean add(E e) {
        // Вычисляем индекс корзины по хэшу
        int index = indexFor(hash(e), table.length);
        Node<E> node = table[index];
        // Проверяем, нет ли уже такого элемента в цепочке (дубликаты не добавляем)
        while (node != null) {
            // Сравнение через == для null и equals для остальных
            if (node.value == e || (e != null && e.equals(node.value))) {
                return false;
            }
            node = node.next;
        }
        // Вставляем новый узел в голову списка корзины — O(1)
        table[index] = new Node<>(e, table[index]);
        size++;
        // Если превысили порог — увеличиваем таблицу вдвое
        if (size >= threshold) {
            resize();
        }
        return true;
    }

    public boolean remove(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        Node<E> prev = null; // нужен для перелинковки списка при удалении
        while (node != null) {
            if (node.value == o || (o != null && o.equals(node.value))) {
                if (prev == null) {
                    // Удаляем голову списка
                    table[index] = node.next;
                } else {
                    // Удаляем из середины/конца: предыдущий узел перескакивает через удаляемый
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean contains(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        // Ищем элемент в цепочке корзины
        while (node != null) {
            if (node.value == o || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // "Размешивание" хэша: старшие биты XOR-ятся с младшими,
    // чтобы уменьшить число коллизий при взятии остатка по маске
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ key.hashCode() >>> 16;
    }

    // Быстрый аналог % length, работает только когда length — степень двойки
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void resize() {
        int oldCapacity = table.length;
        // Дальше удваивать нельзя — упёрлись в предел int
        if (oldCapacity == 1 << 30) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        int newCapacity = oldCapacity << 1; // удвоение
        Node<E>[] newTable = new Node[newCapacity];
        // Переносим все узлы из старой таблицы в новую, пересчитывая индексы
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next; // запоминаем следующий до перелинковки
                int index = indexFor(hash(node.value), newCapacity);
                // Вставляем в голову списка новой корзины
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        // Пересчитываем порог для новой ёмкости
        threshold = (int) (newCapacity * loadFactor);
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        // Обходим все корзины и все цепочки в них
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}