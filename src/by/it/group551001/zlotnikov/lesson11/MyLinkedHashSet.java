package by.it.group551001.zlotnikov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;      // цепочка коллизий в бакете
        Node<E> before;    // порядок добавления
        Node<E> after;     // порядок добавления

        Node(E data) {
            this.data = data;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;
    private Node<E> head;
    private Node<E> tail;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int hash(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        for (Node<E> x = head; x != null; x = x.after) {
            int idx = hash(x.data);
            x.next = table[idx];
            table[idx] = x;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        for (Node<E> x = head; x != null; x = x.after) {
            if (x != head) {
                result += ", ";
            }
            result += x.data;
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.after;
            x.data = null;
            x.next = null;
            x.before = null;
            x.after = null;
            x = next;
        }
        head = null;
        tail = null;
        size = 0;
        table = (Node<E>[]) new Node[16];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int idx = hash(element);
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (element == null ? x.data == null : element.equals(x.data)) {
                return false;
            }
        }
        Node<E> newNode = new Node<>(element);
        newNode.next = table[idx];
        table[idx] = newNode;
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.before = tail;
            tail.after = newNode;
            tail = newNode;
        }
        size++;
        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = hash(o);
        Node<E> prev = null;
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                if (prev == null) {
                    table[idx] = x.next;
                } else {
                    prev.next = x.next;
                }
                if (x.before != null) {
                    x.before.after = x.after;
                } else {
                    head = x.after;
                }
                if (x.after != null) {
                    x.after.before = x.before;
                } else {
                    tail = x.before;
                }
                x.data = null;
                x.next = null;
                x.before = null;
                x.after = null;
                size--;
                return true;
            }
            prev = x;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = hash(o);
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                return true;
            }
        }
        return false;
    }

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
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
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
            Node<E> next = x.after;
            if (c.contains(x.data)) {
                remove(x.data);
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
            Node<E> next = x.after;
            if (!c.contains(x.data)) {
                remove(x.data);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Set / Collection             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            result[i++] = x.data;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < size) {
            throw new UnsupportedOperationException("Array too small");
        }
        int i = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            a[i++] = (T) x.data;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> s = (Set<?>) o;
        if (s.size() != size) {
            return false;
        }
        for (Node<E> x = head; x != null; x = x.after) {
            if (!s.contains(x.data)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Node<E> x = head; x != null; x = x.after) {
            if (x.data != null) {
                h += x.data.hashCode();
            }
        }
        return h;
    }

    private class Itr implements Iterator<E> {
        private Node<E> nextNode = head;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (nextNode == null) {
                throw new IndexOutOfBoundsException();
            }
            E data = nextNode.data;
            nextNode = nextNode.after;
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}