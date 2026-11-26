package by.it.group510901.kostykovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class MyTreeSet<E> implements Set<E> {

    private Object[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        table = new Object[DEFAULT_CAPACITY];
        size = 0;
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
    public void clear() {
        table = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        if (size == 0) {
            table[0] = e;
            size++;
            return true;
        }

        int low = 0;
        int high = size - 1;
        int insertIndex = 0;

        Comparable compElement = (Comparable) e;

        while (low <= high) {
            int mid = (low + high)/2;
            int cmp = compElement.compareTo(table[mid]);

            if (cmp == 0) {
                return false;
            } else if (cmp > 0) {
                low = mid + 1;
                insertIndex = low;
            } else {
                high = mid - 1;
                insertIndex = mid;
            }
        }

        if (size == table.length) {
            resize();
        }

        for (int i = size; i > insertIndex; i--) {
            table[i] = table[i - 1];
        }

        table[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null || size == 0) {
            return false;
        }

        int low = 0;
        int high = size - 1;
        Comparable compElement = (Comparable) o;

        while (low <= high) {
            int mid = (low + high)/2;
            int cmp = compElement.compareTo(table[mid]);

            if (cmp == 0) {
                return true;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || size == 0) {
            return false;
        }

        int low = 0;
        int high = size - 1;
        int targetIndex = -1;
        Comparable compElement = (Comparable) o;

        while (low <= high) {
            int mid = (low + high)/2;
            int cmp = compElement.compareTo(table[mid]);

            if (cmp == 0) {
                targetIndex = mid;
                break;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (targetIndex == -1) {
            return false;
        }

        for (int i = targetIndex; i < size - 1; i++) {
            table[i] = table[i + 1];
        }

        table[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += table[i];
        }
        result += "]";
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (!c.contains(element)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private void resize() {
        Object[] newTable = new Object[table.length * 2];
        for (int i = 0; i < size; i++) {
            newTable[i] = table[i];
        }
        table = newTable;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyTreeSetIterator();
    }

    private class MyTreeSetIterator implements Iterator<E> {
        private int cursor = 0;
        private int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = cursor;
            cursor++;
            return (E) table[lastReturned];
        }

        @Override
        public void remove() {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            MyTreeSet.this.remove(table[lastReturned]);
            cursor = lastReturned;
            lastReturned = -1;
        }
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}

