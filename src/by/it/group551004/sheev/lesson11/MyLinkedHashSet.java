package by.it.group551004.sheev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    // Начальная ёмкость таблицы (количество корзин)
    private static final int DEFAULT_CAPACITY = 32;
    // Коэффициент загрузки: при size >= threshold таблица расширяется
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Узел одновременно участвует в двух структурах:
    //  - next  — цепочка коллизий внутри корзины (как в обычном HashSet)
    //  - before/after — двусвязный список порядка вставки (как в LinkedHashSet)
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before, after;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;   // массив корзин (головы цепочек коллизий)
    private Node<E> head;      // голова списка порядка вставки (первый добавленный)
    private Node<E> tail;      // хвост списка порядка вставки (последний добавленный)
    private int size;          // текущее количество элементов
    private int threshold;     // порог для resize
    private float loadFactor;  // коэффициент загрузки

    public MyLinkedHashSet() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        // Порог = capacity * loadFactor (32 * 0.75 = 24)
        this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        // Обнуляем корзины — узлы удалит GC
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Обнуляем список порядка
        head = null;
        tail = null;
        size = 0;
    }

    public boolean add(E e) {
        // Вычисляем корзину по хэшу
        int index = indexFor(hash(e), table.length);
        Node<E> node = table[index];
        // Проверяем, нет ли уже такого элемента (дубликаты не добавляем)
        while (node != null) {
            if (node.value == e || (e != null && e.equals(node.value))) {
                return false;
            }
            node = node.next;
        }
        // Создаём узел и вставляем в голову цепочки корзины — O(1)
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        // Дополнительно добавляем узел в конец списка порядка вставки
        if (tail == null) {
            // Первый элемент — head и tail совпадают
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }
        size++;
        // При превышении порога удваиваем таблицу
        if (size >= threshold) {
            resize();
        }
        return true;
    }

    public boolean remove(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        Node<E> prev = null; // нужен для перелинковки цепочки коллизий
        while (node != null) {
            if (node.value == o || (o != null && o.equals(node.value))) {
                // Удаляем из цепочки корзины
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                // Удаляем из списка порядка вставки
                unlinkFromOrder(node);
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        // Проверяем, что каждый элемент коллекции присутствует
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы, отслеживая, изменилось ли множество
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        // Оставляем только элементы, входящие в c; идём по списку порядка
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.after; // запоминаем следующий до удаления
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, входящие в c
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean contains(Object o) {
        int index = indexFor(hash(o), table.length);
        Node<E> node = table[index];
        // Ищем в цепочке корзины
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

    // Перемешивание битов хэш-кода для уменьшения коллизий (как в HashMap)
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ key.hashCode() >>> 16;
    }

    // Быстрый аналог % length — работает только при length-степени двойки
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void resize() {
        int oldCapacity = table.length;
        // Удваивать дальше нельзя — упёрлись в предел int
        if (oldCapacity == 1 << 30) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        int newCapacity = oldCapacity << 1;
        Node<E>[] newTable = new Node[newCapacity];
        // Переносим узлы из старой таблицы в новую, пересчитывая индексы.
        // Список порядка (head/tail/before/after) НЕ трогаем — он не зависит от ёмкости.
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next; // запоминаем до перелинковки
                int index = indexFor(hash(node.value), newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        // Обходим список порядка вставки, а не корзины — так сохраняется порядок
        Node<E> node = head;
        boolean first = true;
        while (node != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(node.value);
            first = false;
            node = node.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // Отвязывает узел от двусвязного списка порядка вставки,
    // корректно обновляя head/tail и соседние ссылки before/after
    private void unlinkFromOrder(Node<E> node) {
        Node<E> b = node.before;
        Node<E> a = node.after;
        if (b == null) {
            // Узел был головой — сдвигаем head
            head = a;
        } else {
            b.after = a;
            node.before = null;
        }
        if (a == null) {
            // Узел был хвостом — сдвигаем tail
            tail = b;
        } else {
            a.before = b;
            node.after = null;
        }
    }
}