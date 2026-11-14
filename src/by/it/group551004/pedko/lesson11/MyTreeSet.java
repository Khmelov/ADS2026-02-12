package by.it.group551004.pedko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTreeSet<E> implements Set<E> {

    private final int STD_SIZE = 10;
    private E[] array = (E[]) new Comparable[STD_SIZE];
    private int size = 0;

    // первый индекс, где array[i] >= e  (нижняя граница)
    private int lowerBound(E e) {
        int lo = 0, hi = size;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (((Comparable<E>)array[mid]).compareTo(e) < 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // то же самое, но для произвольного Object (contains/remove)
    private int lowerBoundObject(Object o) {
        int lo = 0, hi = size;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (((Comparable<E>) o).compareTo(array[mid]) > 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    private void grow() {
        E[] bigger = (E[]) new Comparable[array.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = array[i];
        }
        array = bigger;
    }

    private void removeAt(int pos) {
        for (int i = pos; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null;   // отпускаем ссылку
        size--;
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
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int pos = lowerBound(e);
        if (pos < size && ((Comparable<E>)array[pos]).compareTo(e) == 0) {
            return false;                 // уже есть
        }
        if (size == array.length) {
            grow();
        }
        for (int i = size; i > pos; i--) { // сдвиг вправо
            array[i] = array[i - 1];
        }
        array[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int pos = lowerBoundObject(o);
        if (pos >= size || ((Comparable<E>) o).compareTo(array[pos]) != 0) {
            return false;                 // не найден
        }
        removeAt(pos);                    // сдвиг влево
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int pos = lowerBoundObject(o);
        return pos < size && ((Comparable<E>) o).compareTo(array[pos]) == 0;
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
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {   // с конца, чтобы сдвиг не сбивал индекс
            if (c.contains(array[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(array[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return array[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = array[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) array[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}