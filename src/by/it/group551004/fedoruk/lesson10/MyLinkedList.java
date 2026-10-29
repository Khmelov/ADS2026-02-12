package by.it.group551004.fedoruk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public MyLinkedList() {
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        Node<E> h = head;
        Node<E> newNode = new Node<>(null, element, h);
        head = newNode;
        if (h == null) {
            tail = newNode;
        } else {
            h.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> t = tail;
        Node<E> newNode = new Node<>(t, element, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
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
    public E element() {
        return getFirst();
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
        E element = head.item;
        Node<E> next = head.next;
        head.item = null;
        head.next = null;
        head = next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E element = tail.item;
        Node<E> prev = tail.prev;
        tail.item = null;
        tail.prev = null;
        tail = prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return element;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = node(index);
        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = head; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // --- Дополнительные методы интерфейса Deque для корректной компиляции ---

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
        E x = pollFirst();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E removeLast() {
        E x = pollLast();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peekFirst() {
        return (head == null) ? null : head.item;
    }

    @Override
    public E peekLast() {
        return (tail == null) ? null : tail.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> x = tail; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = tail; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
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
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = current;
                current = current.next;
                return lastReturned.item;
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
            private Node<E> current = tail;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = current;
                current = current.prev;
                return lastReturned.item;
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
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    private int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.next, index++) {
                if (x.item == null) return index;
            }
        } else {
            for (Node<E> x = head; x != null; x = x.next, index++) {
                if (o.equals(x.item)) return index;
            }
        }
        return -1;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}