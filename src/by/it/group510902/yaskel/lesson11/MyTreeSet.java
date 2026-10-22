package by.it.group510902.yaskel.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 8;

    private E[] data;  // отсортированный массив, занято первых size ячеек
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    // вспомогательные методы

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int search(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(data[mid], o);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
        }
        data = bigger;
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
    }

    // обязательные методы

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += data[i];
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int index = search(element);
        if (index >= 0) {
            return false; // уже есть
        }
        int pos = -(index + 1);

        if (size == data.length) {
            grow();
        }
        for (int i = size; i > pos; i--) {
            data[i] = data[i - 1];
        }
        data[pos] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = search(o);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return search(o) >= 0;
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
        if (c == this) {
            boolean changed = size > 0;
            clear();
            return changed;
        }
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            return false;
        }
        // фильтруем на месте за один проход, порядок сохраняется
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[j++] = data[i];
            }
        }
        boolean changed = j != size;
        for (int i = j; i < size; i++) {
            data[i] = null;
        }
        size = j;
        return changed;
    }

    // остальные методы интерфейса

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                canRemove = true;
                return data[index++];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                removeAt(--index);
                canRemove = false;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}