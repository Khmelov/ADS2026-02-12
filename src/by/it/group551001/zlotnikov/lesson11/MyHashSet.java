package by.it.group551001.zlotnikov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;

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
        for (int i = 0; i < oldTable.length; i++) {
            Node<E> x = oldTable[i];
            while (x != null) {
                Node<E> next = x.next;
                int idx = hash(x.data);
                x.next = table[idx];
                table[idx] = x;
                x = next;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> x = table[i]; x != null; x = x.next) {
                if (!first) {
                    result += ", ";
                }
                result += x.data;
                first = false;
            }
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            Node<E> x = table[i];
            while (x != null) {
                Node<E> next = x.next;
                x.data = null;
                x.next = null;
                x = next;
            }
            table[i] = null;
        }
        size = 0;
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
        table[idx] = new Node<>(element, table[idx]);
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
                x.data = null;
                x.next = null;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Set / Collection             ///////
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
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> x = table[i];
            while (x != null) {
                Node<E> next = x.next;
                if (c.contains(x.data)) {
                    if (prev == null) {
                        table[i] = next;
                    } else {
                        prev.next = next;
                    }
                    x.data = null;
                    x.next = null;
                    size--;
                    changed = true;
                } else {
                    prev = x;
                }
                x = next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> x = table[i];
            while (x != null) {
                Node<E> next = x.next;
                if (!c.contains(x.data)) {
                    if (prev == null) {
                        table[i] = next;
                    } else {
                        prev.next = next;
                    }
                    x.data = null;
                    x.next = null;
                    size--;
                    changed = true;
                } else {
                    prev = x;
                }
                x = next;
            }
        }
        return changed;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (int b = 0; b < table.length; b++) {
            for (Node<E> x = table[b]; x != null; x = x.next) {
                result[i++] = x.data;
            }
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
        for (int b = 0; b < table.length; b++) {
            for (Node<E> x = table[b]; x != null; x = x.next) {
                a[i++] = (T) x.data;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
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
        for (int b = 0; b < table.length; b++) {
            for (Node<E> x = table[b]; x != null; x = x.next) {
                if (!s.contains(x.data)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int b = 0; b < table.length; b++) {
            for (Node<E> x = table[b]; x != null; x = x.next) {
                if (x.data != null) {
                    h += x.data.hashCode();
                }
            }
        }
        return h;
    }

    private class Itr implements Iterator<E> {
        private int nextBucket = 0;
        private Node<E> nextNode = null;

        Itr() {
            skipToNext();
        }

        private void skipToNext() {
            while (nextBucket < table.length) {
                if (nextNode != null) {
                    return;
                }
                nextNode = table[nextBucket];
                if (nextNode == null) {
                    nextBucket++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return nextBucket < table.length && nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("No more elements");
            }
            E data = nextNode.data;
            nextNode = nextNode.next;
            if (nextNode == null) {
                nextBucket++;
                skipToNext();
            }
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}