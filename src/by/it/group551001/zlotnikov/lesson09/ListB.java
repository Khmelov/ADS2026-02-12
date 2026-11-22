package by.it.group551001.zlotnikov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private Node<E> node(int index) {
        Node<E> x = head;
        for (int i = 0; i < index; i++) {
            x = x.next;
        }
        return x;
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

    private void unlink(Node<E> x) {
        if (x.prev != null) {
            x.prev.next = x.next;
        } else {
            head = x.next;
        }
        if (x.next != null) {
            x.next.prev = x.prev;
        } else {
            tail = x.prev;
        }
        size--;
    }

    private void linkBefore(E e, Node<E> succ) {
        Node<E> pred = (succ == null) ? tail : succ.prev;
        Node<E> newNode = new Node<>(e, pred, succ);
        if (pred != null) {
            pred.next = newNode;
        } else {
            head = newNode;
        }
        if (succ != null) {
            succ.prev = newNode;
        } else {
            tail = newNode;
        }
        size++;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "[";
        for (Node<E> x = head; x != null; x = x.next) {
            result += x.data;
            if (x.next != null) {
                result += ", ";
            }
        }
        return result + "]";
    }

    @Override
    public boolean add(E e) {
        linkBefore(e, null);
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> x = node(index);
        E old = x.data;
        unlink(x);
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        linkBefore(element, index == size ? null : node(index));
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> x = node(index);
        E old = x.data;
        x.data = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            x.data = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return node(index).data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                return index;
            }
            index--;
        }
        return -1;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        for (E e : c) {
            add(index++, e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            if (c.contains(x.data)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            if (!c.contains(x.data)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListB<E> result = new ListB<>();
        Node<E> x = (fromIndex == size) ? null : node(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            result.add(x.data);
            x = x.next;
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
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < size) {
            throw new UnsupportedOperationException("Array is too small");
        }
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            a[i++] = (T) x.data;
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
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.data;
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
        return listIterator();
    }

    private class ListItr implements ListIterator<E> {
        private Node<E> lastReturned;
        private int lastRetIndex;
        private Node<E> next;
        private int nextIndex;

        ListItr(int index) {
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException();
            }
            lastReturned = next;
            lastRetIndex = nextIndex;
            next = next.next;
            nextIndex++;
            return lastReturned.data;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new IndexOutOfBoundsException();
            }
            next = (next == null) ? tail : next.prev;
            nextIndex--;
            lastReturned = next;
            lastRetIndex = nextIndex;
            return lastReturned.data;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            boolean wasNext = (next != lastReturned);
            unlink(lastReturned);
            if (!wasNext) {
                next = lastReturned.next;
            }
            nextIndex = lastRetIndex;
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.data = e;
        }

        @Override
        public void add(E e) {
            Node<E> newNode = new Node<>(e, null, null);
            if (next == null) {
                newNode.prev = tail;
                if (tail != null) {
                    tail.next = newNode;
                } else {
                    head = newNode;
                }
                tail = newNode;
            } else {
                newNode.next = next;
                newNode.prev = next.prev;
                if (next.prev != null) {
                    next.prev.next = newNode;
                } else {
                    head = newNode;
                }
                next.prev = newNode;
            }
            size++;
            nextIndex++;
            lastReturned = null;
        }
    }
}