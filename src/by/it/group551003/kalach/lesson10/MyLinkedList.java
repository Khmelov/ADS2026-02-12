package by.it.group551003.kalach.lesson10;

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

    // ---------- вспомогательные методы ----------

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void unlink(Node<E> node) {
        if (node.prev == null) first = node.next;
        else node.prev.next = node.next;

        if (node.next == null) last = node.prev;
        else node.next.prev = node.prev;

        node.prev = null;
        node.next = null;
        size--;
    }

    private Node<E> nodeAt(int index) {
        Node<E> cur;
        if (index < size / 2) {
            cur = first;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = last;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // удаление по индексу (метод интерфейса List, в Deque его нет)
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> node = nodeAt(index);
        E value = node.value;
        unlink(node);
        return value;
    }

    // удаление по значению (первое вхождение)
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
        Node<E> node = new Node<>(element);
        node.next = first;
        if (first != null) first.prev = node;
        else last = node;
        first = node;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(element);
        node.prev = last;
        if (last != null) last.next = node;
        else first = node;
        last = node;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return first.value;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return last.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = first.value;
        unlink(first);
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E value = last.value;
        unlink(last);
        return value;
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

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
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : first.value;
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : last.value;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            if (same(o, cur.value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> cur = last; cur != null; cur = cur.prev) {
            if (same(o, cur.value)) {
                unlink(cur);
                return true;
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
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
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
    public boolean contains(Object o) {
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            if (same(o, cur.value)) return true;
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
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = first;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.value)) {
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = first;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.value)) {
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = first;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E value = cur.value;
                cur = cur.next;
                return value;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> cur = last;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E value = cur.value;
                cur = cur.prev;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            result[i++] = cur.value;
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
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            a[i++] = (T) cur.value;
        }
        if (a.length > size) a[size] = null;
        return a;
    }
}
