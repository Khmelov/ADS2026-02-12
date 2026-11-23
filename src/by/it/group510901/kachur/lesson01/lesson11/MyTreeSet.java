package by.it.group510901.kachur.lesson01.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] data;
    private int size;

    public MyTreeSet() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // Возвращает:
    //   >= 0  — индекс найденного элемента
    //   <  0  — (-(точка_вставки) - 1)
    @SuppressWarnings("unchecked")
    private int find(Object key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = ((Comparable<Object>) key).compareTo(data[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) hi = mid - 1;
            else lo = mid + 1;
        }
        return -(lo + 1);
    }

    @Override
    public boolean add(E e) {
        int found = find(e);
        if (found >= 0) return false;

        int pos = -(found + 1);
        if (size == data.length) grow();

        for (int i = size; i > pos; i--) {
            data[i] = data[i - 1];
        }
        data[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return find(o) >= 0;
    }

    @Override
    public boolean remove(Object o) {
        int found = find(o);
        if (found < 0) return false;

        for (int i = found; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
        return true;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) result = result + ", ";
            result = result + data[i];
        }
        return result + "]";
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
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (!c.contains(data[read])) {
                data[write++] = data[read];
            }
        }
        boolean changed = (write != size);
        for (int i = write; i < size; i++) data[i] = null;
        size = write;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (c.contains(data[read])) {
                data[write++] = data[read];
            }
        }
        boolean changed = (write != size);
        for (int i = write; i < size; i++) data[i] = null;
        size = write;
        return changed;
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = data[i];
        data = bigger;
    }

    // ===== Заглушки для оставшихся методов Set =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}