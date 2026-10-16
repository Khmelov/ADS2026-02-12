package by.it.group510902.klimantovich.lesson01.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data; // храню сам элемент
        Node<E> next; // ссылка на следующий узел в хэш-бакете при коллизии
        Node<E> after; // ссылка на следующий узел в порядке добавления
        Node<E> before; // ссылка на предыдущий узел в порядке добавления
        Node(E data) { this.data = data; }
    }
    private Node<E>[] table; // массив бакетов хэш-таблицы
    private int size; // счетчик уникальных элементов
    private Node<E> head; // голова двусвязного списка для сохранения порядка
    private Node<E> tail; // хвост двусвязного списка для сохранения порядка
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (Node<E>[]) new Node[16]; // стартовая емкость хэш-таблицы
        this.size = 0;
        this.head = null;
        this.tail = null;
    }
    private int getIndex(Object o) {
        if (o == null) return 0; // для null элементов всегда нулевой бакет
        return (o.hashCode() & 0x7FFFFFFF) % table.length; // вычисляю бакет без знака
    }
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2]; // удваиваю размер таблицы
        for (int i = 0; i < table.length; i++) table[i] = null;
        Node<E> current = head;
        while (current != null) {
            int index = getIndex(current.data); // перехеширую элементы сохраняя связи порядка
            current.next = table[index];
            table[index] = current;
            current = current.after;
        }
    }
    //Задание на уровень B
    //    Создайте class MyLinkedHashSet<E>, который реализует интерфейс Set<E>
    //    и работает на основе массива с адресацией по хеш-коду
    //    и односвязным списком для элементов с коллизиями
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //    Метод toString() должен выводить элементы в порядке их добавления в коллекцию
    //    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    //    быть такими же как в методе toString() обычной коллекции
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; // бегу строго по списку порядка добавления от head к tail
        while (current != null) {
            sb.append(current.data);
            current = current.after;
            if (current != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size() {
        return size; // отдаю размер множества
    }
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null; // очищаю массив хэшей
        head = null; // сбрасываю указатели порядка добавления
        tail = null;
        size = 0;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю пустоту множества
    }
    @Override
    public boolean add(E e) {
        int index = getIndex(e); // ищу нужный бакет в таблице
        Node<E> current = table[index];
        while (current != null) {
            if ((e == null && current.data == null) || (e != null && e.equals(current.data))) {
                return false; // дубликаты добавлять нельзя, возвращаю false
            }
            current = current.next;
        }
        if (size >= table.length * 0.75) {
            resize(); // расширяю таблицу при сильной загрузке
            index = getIndex(e);
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[index]; // кладу в хэш-бакет
        table[index] = newNode;
        if (head == null) {
            head = newNode; // если это первый элемент во множестве
            tail = newNode;
        } else {
            tail.after = newNode; // линкую в хвост списка порядка добавления
            newNode.before = tail;
            tail = newNode;
        }
        size++;
        return true;
    }
    @Override
    public boolean remove(Object o) {
        int index = getIndex(o); // ищу хэш-бакет элемента
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                if (prev == null) table[index] = current.next; // вырезаю из хэш-цепочки
                else prev.next = current.next;
                if (current.before != null) current.before.after = current.after; else head = current.after; // вырезаю из списка порядка
                if (current.after != null) current.after.before = current.before; else tail = current.before;
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
        int index = getIndex(o); // ищу по хэш-коду бакет за O(1)
        Node<E> current = table[index];
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) return true; // нашли объект
            current = current.next;
        }
        return false;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного элемента коллекции нет то false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true; // вливаю все элементы коллекции
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) modified = true; // вычищаю все элементы из коллекции c
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after; // запоминаю следующий шаг заранее так как могу удалить текущий узел
            if (!c.contains(current.data)) {
                remove(current.data); // удаляю всё кроме элементов из коллекции c
                modified = true;
            }
            current = next;
        }
        return modified;
    }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}
