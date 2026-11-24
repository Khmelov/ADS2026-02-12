package by.it.group510901.jalilova.lesson10;

/*  Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
    и работает на основе двунаправленного связного списка
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

                toString()
                add(E element)
                remove(int)
                remove(E element)
                size()

                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()           */

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {


    private int size = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data); // Берем данные из текущего узла
            // Если за текущим узлом есть следующий, добавляем запятую с пробелом
            if (current.next != null) {
                sb.append(", ");
            }
            // 3. Переходим к следующему узлу
            current = current.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(last, element, null);
        if (size != 0) {
            last.next = newNode;
        }
        else {
            first = newNode;
        }
        last = newNode;
        size++;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(null, element, first);
        if (size != 0) {
            first.prev = newNode;
        }
        else {
            last = newNode;
        }
        first = newNode;
        size++;
    }

    @Override
    public E element() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public E poll() {
        if (size == 0){
            return null;
        }
        E temp = first.data;
        first = first.next;
        if (first != null){
            first.prev = null;
        }
        else{
            last = null;
        }
        size--;
        return temp;
    }

    @Override
    public E pollFirst() {
        return poll();
    }

    @Override
    public E pollLast() {
        if (size == 0){
            return null;
        }
        E temp = last.data;
        last = last.prev;
        if (last != null){
            last.next = null;
        }
        else{
            first = null;
        }
        size--;
        return temp;
    }

    public E remove(int index){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) return poll();
        if (index == size - 1) return pollLast();

        Node<E> current;
        if (index >= size / 2) {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        } else {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        }

        E removed = current.data;
        current.prev.next = current.next;
        current.next.prev = current.prev;
        current.prev = null;
        current.next = null;
        size--;
        return removed;
    }

    @Override
    public boolean remove(Object o){
        if (size == 0){
            return false;
        }
        if (o == null ? first.data == null : o.equals(first.data)){
            poll();
            return true;
        }
        else if (o == null ? last.data == null : o.equals(last.data)){
            pollLast();
            return true;
        }
        Node<E> current = first.next;
        while (current != null){
            if (o == null ? current.data == null : o.equals(current.data)){
                current.prev.next = current.next;
                current.next.prev = current.prev;
                current.prev = null;
                current.next = null;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // ЗАГЛУШКИ

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { if (size == 0) throw new NoSuchElementException(); return pollFirst(); }
    @Override public E removeLast() { if (size == 0) throw new NoSuchElementException(); return pollLast(); }
    @Override public E peekFirst() { return size == 0 ? null : first.data; }
    @Override public E peekLast() { return size == 0 ? null : last.data; }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); } // Исправлено на boolean/void в зависимости от вашей JDK, если будет ошибка — поменяйте на void
    @Override public E pop() { return removeFirst(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
