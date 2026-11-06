package by.it.group510902.omelkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E>{

    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int find(Object o) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = compare(elements[mid], o);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1); // вставка
    }

    private void resize() {
        if (size < elements.length) return;
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
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
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return find(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int pos = find(e);
        if (pos >= 0) return false; // unique
        pos = -pos - 1;
        resize();
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int pos = find(o);
        if (pos < 0) return false;
        for (int i = pos; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return true;
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

    private boolean shrinkTo(int newSize) {
        if (newSize == size) return false;
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[w++] = elements[i];
            }
        }
        return shrinkTo(w);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[w++] = elements[i];
            }
        }
        return shrinkTo(w);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
