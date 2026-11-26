package by.it.group551001.bochilo.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    //Создайте аналог двунаправленного связного списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

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
    private int size = 0;

    private boolean equalsElement(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    private E unlink(Node<E> node) {
        E item = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return item;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = first; node != null; node = node.next) {
            sb.append(node.item);
            if (node.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return unlink(node);
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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void addFirst(E element) {
        Node<E> node = new Node<>(null, element, first);
        if (first == null) {
            last = node;
        } else {
            first.prev = node;
        }
        first = node;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(last, element, null);
        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        size++;
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
    public boolean offer(E e) {
        return offerLast(e);
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
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.item;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.item;
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

    @Override
    public E remove() {
        return removeFirst();
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
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> node = first; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> node = last; node != null; node = node.prev) {
            if (equalsElement(o, node.item)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> node = first; node != null; node = node.next) {
            if (equalsElement(o, node.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        while (first != null) {
            unlink(first);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            addLast(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            if (c.contains(node.item)) {
                unlink(node);
                modified = true;
            }
            node = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            if (!c.contains(node.item)) {
                unlink(node);
                modified = true;
            }
            node = next;
        }
        return modified;
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
                E item = current.item;
                current = current.next;
                return item;
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
                E item = current.item;
                current = current.prev;
                return item;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = first; node != null; node = node.next) {
            result[i++] = node.item;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> node = first; node != null; node = node.next) {
            a[i++] = (T) node.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
