package by.it.group551001.bogush.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head; // первый узел
    private Node<E> tail; // последний узел
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // ------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------

    // Отцепляет узел от списка и возвращает его значение
    private E unlink(Node<E> node) {
        E value = node.value;

        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        node.prev = null;
        node.next = null;
        node.value = null;
        size--;
        return value;
    }

    // Узел по индексу: идём с ближнего конца
    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private boolean sameValue(Object o, E value) {
        if (o == null) {
            return value == null;
        } else {
            return o.equals(value);
        }
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // remove(int) в Deque нет, это отдельный публичный метод (как в List)
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        return unlink(getNode(index));
    }

    // remove(E element): в Deque сигнатура remove(Object)
    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (sameValue(o, current.value)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void addFirst(E element) {
        Node<E> node = new Node<>(element);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(element);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }

    // ------------------------------------------------------------------
    // Простые бонусные методы
    // ------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (head != null) {
            unlink(head);
        }
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (sameValue(o, current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(tail);
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (head == null) {
            return null;
        }
        return head.value;
    }

    @Override
    public E peekLast() {
        if (tail == null) {
            return null;
        }
        return tail.value;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (sameValue(o, current.value)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Остальное из интерфейса -- не обязательно, заглушки
    // ------------------------------------------------------------------

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}