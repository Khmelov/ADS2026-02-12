package by.it.group510902.klimantovich.lesson01.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data; // храню сами данные узла
        Node<E> next; // ссылка на следующий узел
        Node<E> prev; // ссылка на предыдущий узел
        Node(E data) { this.data = data; }
    }
    private Node<E> first; // указатель на начало нашего списка
    private Node<E> last; // указатель на самый конец списка
    private int size = 0; // счетчик элементов
    //Задание на уровень B
    //    Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
    //    и работает на основе двунаправленного связного списка
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data); // бегу по узлам и собираю их значения
            current = current.next;
            if (current != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E e) {
        addLast(e); // обычный add просто кидает элемент в конец
        return true;
    }
    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e);
        if (first == null) {
            first = newNode; // если список пуст то первый узел становится и последним
            last = newNode;
        } else {
            newNode.next = first; // цепляю новый узел перед текущим началом
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }
    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);
        if (last == null) {
            first = newNode; // если список был пустой
            last = newNode;
        } else {
            last.next = newNode; // цепляю в самый хвост списка
            newNode.prev = last;
            last = newNode;
        }
        size++;
    }
    @Override
    public int size() {
        return size; // просто отдаю счетчик
    }
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(); // проверка выхода за границы
        Node<E> current = first;
        for (int i = 0; i < index; i++) current = current.next; // шагаю до нужного индекса
        E oldData = current.data;
        if (current.prev != null) current.prev.next = current.next; else first = current.next; // переставляю ссылки
        if (current.next != null) current.next.prev = current.prev; else last = current.prev;
        size--;
        return oldData;
    }
    @Override
    public boolean remove(Object o) {
        Node<E> current = first;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                if (current.prev != null) current.prev.next = current.next; else first = current.next; // отвязываю узел
                if (current.next != null) current.next.prev = current.prev; else last = current.prev;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }
    @Override
    public E element() { return getFirst(); } // метод возвращает голову списка
    @Override
    public E getFirst() {
        if (first == null) throw new java.util.NoSuchElementException(); // если пусто кидаю эксепшн
        return first.data;
    }
    @Override
    public E getLast() {
        if (last == null) throw new java.util.NoSuchElementException();
        return last.data;
    }
    @Override
    public E poll() { return pollFirst(); } // poll берет элемент с самого начала
    @Override
    public E pollFirst() {
        if (first == null) return null; // возвращаю null если список пустой
        E data = first.data;
        first = first.next; // двигаю указатель начала на следующий элемент
        if (first != null) first.prev = null; else last = null;
        size--;
        return data;
    }
    @Override
    public E pollLast() {
        if (last == null) return null;
        E data = last.data;
        last = last.prev; // сдвигаю указатель конца назад
        if (last != null) last.next = null; else first = null;
        size--;
        return data;
    }
    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override
    public boolean offerLast(E e) { addLast(e); return true; }
    @Override
    public E removeFirst() { return getFirst(); }
    @Override
    public E removeLast() { return getLast(); }
    @Override
    public E peekFirst() { return first == null ? null : first.data; }
    @Override
    public E peekLast() { return last == null ? null : last.data; }
    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override
    public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean offer(E e) { return offerLast(e); }
    @Override
    public E remove() { return removeFirst(); }
    @Override
    public E peek() { return peekFirst(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public void clear() { first = null; last = null; size = 0; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean contains(Object o) {
        Node<E> current = first;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) return true;
            current = current.next;
        }
        return false;
    }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Iterator<E> descendingIterator() { return null; }
    @Override
    public void push(E e) { addFirst(e); }
    @Override
    public E pop() { return removeFirst(); }
}
