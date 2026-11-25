package by.it.group510901.parkhomenko.lesson10;

import java.util.*;
//двусвязный список: каждый узел знает соседа слева и справа
//двусторонняя очередь но без массива

public class MyLinkedList<E> implements Deque<E> {


    // Узел списка: значение и ссылки на предыдущий и следующий узлы.
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    // Находит узел по номеру: идём с того конца, который ближе.
    private Node<E> node(int index) {
        if (index < size / 2) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        }
        Node<E> x = tail;
        for (int i = size - 1; i > index; i--) x = x.prev;
        return x;
    }

    // Выкидывает узел из цепочки: соседи начинают ссылаться друг на друга. Возвращает его значение.
    private E unlink(Node<E> x) {
        if (x.prev == null) head = x.next; else x.prev.next = x.next;
        if (x.next == null) tail = x.prev; else x.next.prev = x.prev;
        size--;
        return x.value;
    }

    // Превращает список в текст вида "[5, 7, 3]" - точно как у LinkedList.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = head; x != null; x = x.next) {
            if (x != head) sb.append(", ");
            sb.append(x.value);
        }
        return sb.append(']').toString();
    }

    // Добавляет элемент в конец списка.
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Удаляет элемент с номером index и возвращает его.
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return unlink(node(index));
    }

    // Удаляет первый элемент, равный o. Возвращает true, если удалил.
    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (Objects.equals(o, x.value)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    // Возвращает, сколько элементов сейчас в списке.
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в начало: новый узел становится головой.
    @Override
    public void addFirst(E e) {
        Node<E> x = new Node<>(e);
        x.next = head;
        if (head == null) tail = x; else head.prev = x;
        head = x;
        size++;
    }

    // Добавляет элемент в конец: новый узел становится хвостом.
    @Override
    public void addLast(E e) {
        Node<E> x = new Node<>(e);
        x.prev = tail;
        if (tail == null) head = x; else tail.next = x;
        tail = x;
        size++;
    }

    // Возвращает первый элемент, не удаляя его.
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент; если список пуст - ошибка.
    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.value;
    }

    // Возвращает последний элемент; если список пуст - ошибка.
    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.value;
    }

    // Забирает и удаляет первый элемент (null, если пусто).
    @Override
    public E poll() {
        return pollFirst();
    }

    // Забирает и удаляет первый элемент (null, если пусто).
    @Override
    public E pollFirst() {
        return head == null ? null : unlink(head);
    }

    // Забирает и удаляет последний элемент (null, если пусто).
    @Override
    public E pollLast() {
        return tail == null ? null : unlink(tail);
    }



    // добавить в начало (вернуть true/false вместо ошибки)
    @Override
    public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }

    // добавить в конец (вернуть true/false вместо ошибки)
    @Override
    public boolean offerLast(E e) { throw new UnsupportedOperationException(); }

    // удалить и вернуть первый (ошибка, если пусто)
    @Override
    public E removeFirst() { throw new UnsupportedOperationException(); }

    // удалить и вернуть последний (ошибка, если пусто)
    @Override
    public E removeLast() { throw new UnsupportedOperationException(); }

    // посмотреть первый (null, если пусто)
    @Override
    public E peekFirst() { throw new UnsupportedOperationException(); }

    // посмотреть последний (null, если пусто)
    @Override
    public E peekLast() { throw new UnsupportedOperationException(); }

    // удалить первое вхождение o
    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    // удалить последнее вхождение o
    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    // добавить в конец (вернуть true/false вместо ошибки)
    @Override
    public boolean offer(E e) { throw new UnsupportedOperationException(); }

    // удалить и вернуть первый (ошибка, если пусто)
    @Override
    public E remove() { throw new UnsupportedOperationException(); }

    // посмотреть первый (null, если пусто)
    @Override
    public E peek() { throw new UnsupportedOperationException(); }

    // добавить в конец все элементы из c
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    // положить в начало (как в стек)
    @Override
    public void push(E e) { throw new UnsupportedOperationException(); }

    // снять с начала (как со стека)
    @Override
    public E pop() { throw new UnsupportedOperationException(); }

    // есть ли o в списке
    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    // есть ли все элементы из c
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // удалить все элементы, которые есть в c
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // оставить только элементы, которые есть в c
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // удалить все элементы
    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    // пуст ли список
    @Override
    public boolean isEmpty() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив Object
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив типа T
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // «указка» для перебора от начала к концу
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    // «указка» для перебора от конца к началу
    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

}
