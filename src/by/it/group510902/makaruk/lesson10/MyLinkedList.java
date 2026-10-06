package by.it.group510902.makaruk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Класс узла списка
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

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data);
            current = current.next;
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
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

    @Override
    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    // Аннотацию @Override здесь ставить нельзя, так как в Deque нет удаления по числовому индексу
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return unlink(current);
    }

    // Служебный вспомогательный метод удаления ноды из связей
    private E unlink(Node<E> x) {
        E element = x.data;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
        return element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        E data = first.data;
        Node<E> next = first.next;
        first.data = null;
        first.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return data;
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        E data = last.data;
        Node<E> prev = last.prev;
        last.data = null;
        last.prev = null;
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;
        return data;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.data)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean offerFirst(E e) { return false; }
    public boolean offerLast(E e) { return false; }
    public E removeFirst() { return null; }
    public E removeLast() { return null; }
    public E peekFirst() { return null; }
    public E peekLast() { return null; }
    public boolean removeFirstOccurrence(Object o) { return false; }
    public boolean removeLastOccurrence(Object o) { return false; }
    public boolean offer(E e) { return false; }
    public E remove() { return null; }
    public E peek() { return null; }
    public void push(E e) { addFirst(e); }
    public E pop() { return pollFirst(); }
    public boolean contains(Object o) { return false; }
    public boolean isEmpty() { return size == 0; }
    public Iterator<E> iterator() { return null; }
    public Iterator<E> descendingIterator() { return null; }
    public void clear() {}
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}