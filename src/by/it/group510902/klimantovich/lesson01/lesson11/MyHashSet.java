package by.it.group510902.klimantovich.lesson01.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data; // храню сам элемент
        Node<E> next; // ссылка на следующий узел при коллизии
        Node(E data) { this.data = data; }
    }
    private Node<E>[] table; // массив бакетов (корзин) хэш-таблицы
    private int size; // счетчик уникальных элементов во множестве
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.table = (Node<E>[]) new Node[16]; // начальная емкость таблицы по умолчанию
        this.size = 0;
    }
    private int getIndex(Object o) {
        if (o == null) return 0; // для null элементов всегда нулевой бакет
        return (o.hashCode() & 0x7FFFFFFF) % table.length; // убираю знак и беру остаток от деления
    }
    private void resize() {
        @SuppressWarnings("unchecked")
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2]; // удваиваю размер хэш-таблицы
        size = 0; // сбрасываю счетчик, так как add заново его инкрементирует
        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                add(current.data); // перехеширую все старые элементы в новую таблицу
                current = current.next;
            }
        }
    }
    //Задание на уровень А
    //    Создайте class MyHashSet<E>, который реализует интерфейс Set<E>
    //    и работает на основе массива с адресацией по хеш-коду
    //    и односвязным списком для элементов с коллизиями
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //    Метод toString() может выводить элементы в произвольном порядке
    //    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    //    быть такими же как в методе toString() обычной коллекции
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) sb.append(", "); // ставлю разделитель между элементами множества
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size() {
        return size; // возвращаю общее число элементов
    }
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null; // зануляю хэш-таблицу для GC
        size = 0;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю пустое ли множество
    }
    @Override
    public boolean add(E e) {
        int index = getIndex(e); // вычисляю бакет для вставки
        Node<E> current = table[index];
        while (current != null) {
            if ((e == null && current.data == null) || (e != null && e.equals(current.data))) {
                return false; // во множестве не может быть дубликатов, возвращаю false
            }
            current = current.next;
        }
        if (size >= table.length * 0.75) {
            resize(); // если превысили коэффициент загрузки 0.75, то расширяю таблицу
            index = getIndex(e); // пересчитываю индекс под новый размер
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[index]; // вставляю новый узел в начало односвязного списка бакета
        table[index] = newNode;
        size++;
        return true;
    }
    @Override
    public boolean remove(Object o) {
        int index = getIndex(o); // нахожу нужный бакет
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                if (prev == null) table[index] = current.next; // если удаляю голову списка корзины
                else prev.next = current.next; // переставляю ссылку в середине или конце цепочки
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
        int index = getIndex(o); // определяю бакет для поиска
        Node<E> current = table[index];
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                return true; // нашли совпадение по значению
            }
            current = current.next;
        }
        return false;
    }
    /////////////////////////////////////////////////////////////////////////
    //////        Методы заглушки для полной компиляции интерфейса   ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
}
