package by.it.group510901.gulchenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E element;
        Node<E> previous;
        Node<E> next;

        Node(E element, Node<E> previous, Node<E> next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    private E unlink(Node<E> node) {
        if (node.previous == null) {
            first = node.next;
        } else {
            node.previous.next = node.next;
        }

        if (node.next == null) {
            last = node.previous;
        } else {
            node.next.previous = node.previous;
        }

        E removed = node.element;
        node.element = null;
        node.previous = null;
        node.next = null;
        size--;

        return removed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        Node<E> current = first;

        while (current != null) {
            if (current != first) {
                result += ", ";
            }
            result += current.element == this
                    ? "(this Collection)"
                    : current.element;
            current = current.next;
        }

        return result + "]";
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> current;

        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
        }

        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = first;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> node = new Node<>(element, null, first);

        if (first == null) {
            last = node;
        } else {
            first.previous = node;
        }

        first = node;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(element, last, null);

        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }

        last = node;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.element;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.element;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return first == null ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return last == null ? null : unlink(last);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public boolean offer(E element) {
        return offerLast(element);
    }

    @Override
    public E removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return unlink(first);
    }

    @Override
    public E removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return unlink(last);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.element;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.element;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E element) {
        addFirst(element);
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
        while (first != null) {
            unlink(first);
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = last;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                unlink(current);
                return true;
            }
            current = current.previous;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = first;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

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