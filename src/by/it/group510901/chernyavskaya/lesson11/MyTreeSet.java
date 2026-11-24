package by.it.group510901.chernyavskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

//на основе обычного отсортированного массива, бинарный поиск

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements = new Object[16];
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Object midVal = elements[mid];

            int cmp;
            if (o == null) {
                if (midVal == null) return mid;
                cmp = -1;
            } else if (midVal == null) {
                cmp = 1;
            } else {
                @SuppressWarnings("unchecked")
                Comparable<Object> c = (Comparable<Object>) o;
                cmp = c.compareTo(midVal);
            }

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // Элемент найден
            }
        }
        return -(low + 1);
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        int res = binarySearch(e);
        if (res >= 0) {
            return false;
        }

        int idx = -res - 1;
        ensureCapacity();

        for (int i = size; i > idx; i--) {
            elements[i] = elements[i - 1];
        }

        elements[idx] = e;
        size++;
        return true;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    @Override
    public boolean remove(Object o) {
        int res = binarySearch(o);
        if (res < 0) {
            return false;
        }

        int idx = res;

        for (int i = idx; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
