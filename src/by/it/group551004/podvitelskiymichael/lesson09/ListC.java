package by.it.group551004.podvitelskiymichael.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public ListC() {
        head = null;
        tail = null;
        size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Singly-linked: no prev pointers, so every by-index lookup walks from head.
    private Node<E> nodeAt(int index) {
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private void linkLast(E e) {
        Node<E> newNode = new Node<>(e, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    private void linkFirst(E e) {
        Node<E> newNode = new Node<>(e, head);
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    // Inserts e right after pred; pred == null means insert at head.
    private void linkAfter(E e, Node<E> pred) {
        if (pred == null) {
            linkFirst(e);
            return;
        }
        Node<E> newNode = new Node<>(e, pred.next);
        pred.next = newNode;
        if (newNode.next == null) {
            tail = newNode;
        }
        size++;
    }

    // Removes the node right after pred; pred == null means remove head.
    private E unlinkAfter(Node<E> pred) {
        Node<E> target = (pred == null) ? head : pred.next;
        E value = target.value;
        Node<E> next = target.next;

        if (pred == null) {
            head = next;
        } else {
            pred.next = next;
        }
        if (next == null) {
            tail = pred;
        }

        target.value = null;
        target.next = null;
        size--;
        return value;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> pred = (index == 0) ? null : nodeAt(index - 1);
        return unlinkAfter(pred);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if (index == size) {
            linkLast(element);
        } else {
            Node<E> pred = (index == 0) ? null : nodeAt(index - 1);
            linkAfter(element, pred);
        }
    }

    @Override
    public boolean remove(Object o) {
        Node<E> pred = null;
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                unlinkAfter(pred);
                return true;
            }
            pred = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.value = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                index = i;
            }
            current = current.next;
            i++;
        }
        return index;
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
        boolean changed = false;
        for (E e : c) {
            linkLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        boolean changed = false;
        int insertPos = index;
        for (E e : c) {
            add(insertPos, e);
            insertPos++;
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            while (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> pred = null;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.value)) {
                unlinkAfter(pred);
                changed = true;
            } else {
                pred = current;
            }
            current = next;
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        }
        ListC<E> result = new ListC<>();
        Node<E> current = nodeAt(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            result.linkLast(current.value);
            current = current.next;
        }
        return result;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            a[i++] = (T) current.value;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            result[i++] = current.value;
            current = current.next;
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new ListItr(0);
    }

    // Singly-linked: no prev pointers, so previous() re-walks from head.
    // O(1) next(), O(n) previous() — the classic trade-off vs a doubly-linked list.
    private class ListItr implements ListIterator<E> {
        private int cursor;
        private int lastReturned = -1;

        ListItr(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E value = nodeAt(cursor).value;
            lastReturned = cursor;
            cursor++;
            return value;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            cursor--;
            lastReturned = cursor;
            return nodeAt(cursor).value;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastReturned);
            if (lastReturned < cursor) {
                cursor--;
            }
            lastReturned = -1;
        }

        @Override
        public void set(E e) {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            ListC.this.set(lastReturned, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(cursor, e);
            cursor++;
            lastReturned = -1;
        }
    }

}