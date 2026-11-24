package by.it.group551003.yakushev.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTreeSet<E> implements Set<E> {
    private Object[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        array = new Object[DEFAULT_CAPACITY];
    }

    // Бинарный поиск. Возвращает индекс, если найден, иначе -(точка вставки + 1)
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("unchecked")
            Comparable<Object> midVal = (Comparable<Object>) array[mid];
            int cmp = midVal.compareTo(key);

            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("TreeSet does not support null");

        int pos = binarySearch(e);
        if (pos >= 0) return false; // Элемент уже есть

        int insertPos = -(pos + 1);

        if (size == array.length) {
            resize();
        }

        // Сдвигаем элементы вправо
        for (int i = size; i > insertPos; i--) {
            array[i] = array[i - 1];
        }
        array[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int pos = binarySearch(o);
        if (pos < 0) return false;

        // Сдвигаем элементы влево
        for (int i = pos; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return binarySearch(o) >= 0;
    }

    private void resize() {
        int newCapacity = array.length * 2;
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { array = new Object[DEFAULT_CAPACITY]; size = 0; }

    @Override
    public Iterator<E> iterator() {
        return new TreeIterator();
    }

    private class TreeIterator implements Iterator<E> {
        int index = 0;
        int lastReturnedIndex = -1;

        public boolean hasNext() { return index < size; }
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturnedIndex = index;
            @SuppressWarnings("unchecked")
            E item = (E) array[index++];
            return item;
        }
        public void remove() {
            if (lastReturnedIndex == -1) throw new IllegalStateException();
            MyTreeSet.this.remove(array[lastReturnedIndex]);
            index = lastReturnedIndex; // Индекс сместился, корректируем
            lastReturnedIndex = -1;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) if (remove(e)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] currentElements = new Object[size];
        for (int i = 0; i < size; i++) currentElements[i] = array[i];

        for (Object e : currentElements) {
            if (!c.contains(e)) {
                if (remove(e)) modified = true;
            }
        }
        return modified;
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;
        try { return containsAll(c); } catch (Exception unused) { return false; }
    }
    @Override public int hashCode() {
        int h = 0;
        for (E e : this) if (e != null) h += e.hashCode();
        return h;
    }
}