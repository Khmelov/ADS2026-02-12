package by.it.group551004.frantskevich.lesson10;

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

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    private Node<E> node(int index) {
        if (index < size / 2) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        }
        Node<E> x = last;
        for (int i = size - 1; i > index; i--) {
            x = x.prev;
        }
        return x;
    }

    private E unlink(Node<E> x) {
        if (x.prev == null) {
            first = x.next;
        } else {
            x.prev.next = x.next;
        }
        if (x.next == null) {
            last = x.prev;
        } else {
            x.next.prev = x.prev;
        }
        size--;
        return x.value;
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = first; x != null; x = x.next) {
            if (x != first) {
                sb.append(", ");
            }
            sb.append(x.value);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        Node<E> x = new Node<>(e);
        x.next = first;
        if (first == null) {
            last = x;
        } else {
            first.prev = x;
        }
        first = x;
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> x = new Node<>(e);
        x.prev = last;
        if (last == null) {
            first = x;
        } else {
            last.next = x;
        }
        last = x;
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
        return first.value;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
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
    //////               Опциональные к реализации методы             ///////
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
    public boolean offer(E e) {
        return offerLast(e);
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
        return first == null ? null : first.value;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.value;
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
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (same(o, x.value)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = last; x != null; x = x.prev) {
            if (same(o, x.value)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (same(o, x.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E item : c) {
            addLast(item);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Node<E> x = first; x != null; x = x.next) {
            if (c.contains(x.value)) {
                unlink(x);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Node<E> x = first; x != null; x = x.next) {
            if (!c.contains(x.value)) {
                unlink(x);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.value;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            a[i++] = (T) x.value;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = last;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.prev;
                return value;
            }
        };
    }
}
