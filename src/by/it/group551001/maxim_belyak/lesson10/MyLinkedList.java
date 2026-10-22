package by.it.a_khmelev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Дек на основе двунаправленного связного списка БЕЗ использования других классов
    // СТАНДАРТНОЙ БИБЛИОТЕКИ. Каждый узел знает предыдущий и следующий узлы.

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E> head; // первый узел (или null, если список пуст)
    private Node<E> tail; // последний узел
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                       ///////
    /////////////////////////////////////////////////////////////////////////

    // вставка узла в начало
    private void linkFirst(E element) {
        Node<E> node = new Node<>(element);
        node.next = head;
        if (head == null) {
            tail = node;      // список был пуст: новый узел и первый, и последний
        } else {
            head.prev = node;
        }
        head = node;
        size++;
    }

    // вставка узла в конец
    private void linkLast(E element) {
        Node<E> node = new Node<>(element);
        node.prev = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    // "выкидываем" узел из цепочки: соседи начинают ссылаться друг на друга
    private E unlink(Node<E> node) {
        E item = node.item;
        if (node.prev == null) {
            head = node.next;  // удаляем первый узел
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            tail = node.prev;  // удаляем последний узел
        } else {
            node.next.prev = node.prev;
        }
        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return item;
    }

    // поиск узла по номеру, идём с ближайшего конца
    private Node<E> nodeAt(int index) {
        Node<E> node;
        if (index < size / 2) {
            node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = tail;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }
        return node;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = head; node != null; node = node.next) {
            sb.append(node.item);
            if (node.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        linkLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(Object element) {
        return removeFirstOccurrence(element);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        linkFirst(element);
    }

    @Override
    public void addLast(E element) {
        linkLast(element);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return head == null ? null : unlink(head);
    }

    @Override
    public E pollLast() {
        return tail == null ? null : unlink(tail);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)        ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return unlink(tail);
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> node = head; node != null; node = node.next) {
            if (same(o, node.item)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> node = tail; node != null; node = node.prev) {
            if (same(o, node.item)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
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
        for (Node<E> node = head; node != null; node = node.next) {
            if (same(o, node.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E item = current.item;
                current = current.prev;
                return item;
            }
        };
    }

}