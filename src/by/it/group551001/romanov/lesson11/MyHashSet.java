package by.it.group551001.romanov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Вложенный класс для элемента односвязного списка
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        // Начальный размер массива (корзин). Обычно используется степень двойки.
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    // Вспомогательный метод для безопасного вычисления индекса
    private int getHash(Object o) {
        if (o == null) return 0;
        // Побитовое И с 0x7FFFFFFF убирает знак минуса, если хэш отрицательный
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    // Вспомогательный метод для увеличения таблицы при большом количестве элементов
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        // Перераспределяем элементы по новой, увеличенной таблице
        for (Node<E> node : oldTable) {
            while (node != null) {
                int index = getHash(node.data);
                table[index] = new Node<>(node.data, table[index]);
                node = node.next;
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int index = getHash(e);
        Node<E> current = table[index];

        // Проверяем, есть ли уже такой элемент в цепочке
        while (current != null) {
            if ((e == null && current.data == null) || (e != null && e.equals(current.data))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый узел в начало цепочки
        table[index] = new Node<>(e, table[index]);
        size++;

        // Если таблица заполнена больше чем на 75% (Load Factor), увеличиваем её
        if (size > table.length * 0.75) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getHash(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                // Если элемент первый в цепочке
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    // Пропускаем текущий узел, связывая предыдущий со следующим
                    prev.next = current.next;
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
        int index = getHash(o);
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
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        // Проходим по всем корзинам
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            // Проходим по всей цепочке внутри корзины
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
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
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
}