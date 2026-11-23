package by.it.group510901.kachur.lesson01.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Двунаправленный связный список, реализующий интерфейс Deque<E>.
 *
 *
 */
public class MyLinkedList<E> implements Deque<E> {

    /** Узел списка. */
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    // ==================== Обязательные методы задания ====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = first; x != null; x = x.next) {
            sb.append(x.item);
            if (x.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Удаление по индексу.
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     * Удаление по значению
     */
    @Override
    public boolean remove(Object element) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (element == null ? x.item == null : element.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> oldFirst = first;
        Node<E> newNode = new Node<>(null, element, oldFirst);
        first = newNode;
        if (oldFirst == null) last = newNode;
        else oldFirst.prev = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> oldLast = last;
        Node<E> newNode = new Node<>(oldLast, element, null);
        last = newNode;
        if (oldLast == null) first = newNode;
        else oldLast.next = newNode;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlink(first);
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlink(last);
    }

    // ==================== Вспомогательные ====================

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null) first = next;
        else { prev.next = next; x.prev = null; }

        if (next == null) last = prev;
        else { next.prev = prev; x.next = null; }

        x.item = null;
        size--;
        return element;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
    }

    // ==================== Остальные методы Deque ====================

    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E remove() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return pollLast();
    }

    @Override public E peek() { return first == null ? null : first.item; }
    @Override public E peekFirst() { return first == null ? null : first.item; }
    @Override public E peekLast() { return last == null ? null : last.item; }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = first; x != null; x = x.next)
            if (o == null ? x.item == null : o.equals(x.item)) return true;
        return false;
    }

    @Override public void push(E e) { addFirst(e); }

    @Override
    public E pop() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = last; x != null; x = x.prev)
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        return false;
    }

    @Override
    public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null; x.next = null; x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) result[i++] = x.item;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) a[i++] = (T) x.item;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            addLast(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            if (c.contains(x.item)) { unlink(x); modified = true; }
            x = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            if (!c.contains(x.item)) { unlink(x); modified = true; }
            x = next;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = first;
            private Node<E> lastReturned = null;

            @Override public boolean hasNext() { return next != null; }

            @Override
            public E next() {
                if (next == null) throw new NoSuchElementException();
                lastReturned = next;
                E item = next.item;
                next = next.next;
                return item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> next = last;
            private Node<E> lastReturned = null;

            @Override public boolean hasNext() { return next != null; }

            @Override
            public E next() {
                if (next == null) throw new NoSuchElementException();
                lastReturned = next;
                E item = next.item;
                next = next.prev;
                return item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }
}