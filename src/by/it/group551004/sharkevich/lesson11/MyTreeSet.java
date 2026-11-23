package by.it.group551004.sharkevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 32;

    private Object[] elements;
    private int size;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean add(E e) {
        int index = binarySearch(e);
        if (index >= 0) {
            return false;
        }
        int pos = -(index + 1);
        ensureCapacity(size + 1);
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[pos] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elements[i])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    private int binarySearch(Object key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = ((Comparable<Object>) key).compareTo(elements[mid]);
            if (cmp == 0) {
                return mid;
            }
            if (cmp < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -(lo + 1);
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    private void ensureCapacity(int needed) {
        if (needed <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        if (newCapacity < needed) {
            newCapacity = needed;
        }
        Object[] newArr = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }
}
