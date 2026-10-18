package by.it.group551003.dobrolovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private Object[] elements = new Object[10];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx < 0) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        E e = (E) elements[index];
        return e;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) return i;
        }
        return -1;
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
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) return false;
        int i = index;
        for (E e : c) {
            add(i++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (c.contains(elements[i])) {
                remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные — заглушки                      ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override public ListIterator<E> listIterator(int index) { return null; }

    @Override public ListIterator<E> listIterator() { return null; }

    @Override public <T> T[] toArray(T[] a) { return null; }

    @Override public Object[] toArray() { return new Object[0]; }

    @Override public Iterator<E> iterator() { return null; }

    /////////////////////////////////////////////////////////////////////////
    // Вспомогательные методы
    /////////////////////////////////////////////////////////////////////////

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCap = elements.length + (elements.length >> 1) + 1;
            if (newCap < minCapacity) newCap = minCapacity;
            Object[] bigger = new Object[newCap];
            System.arraycopy(elements, 0, bigger, 0, size);
            elements = bigger;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}