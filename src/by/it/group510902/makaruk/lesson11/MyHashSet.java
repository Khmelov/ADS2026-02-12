package by.it.group510902.makaruk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс узла односвязного списка для хранения элементов в бакетах
    private static class Node<E> {
        E data;         // Хранимый элемент
        Node<E> next;   // Ссылка на следующий узел в цепочке коллизий

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    // Массив бакетов (таблица хеширования)
    private Node<E>[] table;
    // Количество фактически добавленных уникальных элементов в HashSet
    private int size;
    // Начальная емкость таблицы (обязательно степень двойки для быстрой битовой маски)
    private static final int DEFAULT_CAPACITY = 16;
    // Коэффициент загрузки (Load Factor), при превышении которого таблица будет расширяться
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод вычисления индекса бакета по хеш-коду объекта
    private int getIndex(Object o) {
        if (o == null) return 0; // null всегда кладем в нулевой бакет
        // Хешируем объект и с помощью битового И (&) зацикливаем индекс в пределах длины массива
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    // Служебный метод перехеширования (увеличения таблицы в 2 раза) при высокой заполненности
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        // Создаем новую таблицу, которая в два раза больше старой
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0; // Сбрасываем размер, так как метод add сам его инкрементирует

        // Переносим все элементы из старой таблицы в новую
        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                add(current.data); // Заново передобавляем элемент (его индекс в новой таблице изменится)
                current = current.next;
            }
        }
    }

    @Override
    public int size() {
        // Возвращаем текущее количество уникальных элементов
        return size;
    }

    @Override
    public void clear() {
        // Зануляем все ячейки массива бакетов, обрывая связи связных списков
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Сбрасываем счетчик элементов в ноль
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        // Множество пустое, если его размер равен 0
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        // Проверяем, не пора ли расширить таблицу по коэффициенту загрузки
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        // Вычисляем индекс бакета для добавляемого объекта
        int index = getIndex(e);
        Node<E> current = table[index];

        // Проверяем всю цепочку коллизий: дубликаты в Set добавлять запрещено
        while (current != null) {
            if (e == null ? current.data == null : e.equals(current.data)) {
                return false; // Элемент уже существует, добавление отклонено
            }
            current = current.next;
        }

        // Если элемента нет, создаем новый узел и вставляем его в САМОЕ НАЧАЛО цепочки бакета (быстрая вставка за O(1))
        table[index] = new Node<>(e, table[index]);
        size++; // Инкрементируем счетчик уникальных элементов
        return true;
    }

    @Override
    public boolean remove(Object o) {
        // Находим бакет, в котором должен лежать объект
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> previous = null;

        // Идем по цепочке коллизий бакета в поисках объекта
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                // Если удаляемый элемент был головой списка в бакете:
                if (previous == null) {
                    table[index] = current.next; // Головой становится следующий элемент
                } else {
                    previous.next = current.next; // Вырезаем узел из цепочки, связывая предыдущий со следующим
                }
                size--; // Уменьшаем счетчик элементов
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false; // Элемент для удаления не найден
    }

    @Override
    public boolean contains(Object o) {
        // Вычисляем целевой индекс бакета
        int index = getIndex(o);
        Node<E> current = table[index];

        // Ищем объект по цепочке связного списка
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return true; // Элемент найден
            }
            current = current.next;
        }
        return false; // Элемента в множестве нет
    }

    @Override
    public String toString() {
        // Используем StringBuilder для быстрой сборки строки
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        // Проходим по всем бакетам таблицы
        for (Node<E> head : table) {
            Node<E> current = head;
            // Проходим по цепочке коллизий каждого бакета
            while (current != null) {
                if (!first) {
                    sb.append(", "); // Ставим разделитель перед каждым элементом, кроме первого
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
    public boolean containsAll(Collection<?> c) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
}