package by.it.group510901.parkhomenko.lesson09;

import java.util.*;

// Список на массиве. Массив data хранит элементы, size - сколько их сейчас.
public class ListA<E> implements List<E> {

    private Object[] data = new Object[10];
    private int size = 0;

    // Если массив заполнен - создаём вдвое больший и переписываем в него элементы.
    private void grow() {
        if (size == data.length) {
            Object[] bigger = new Object[size * 2];
            for (int i = 0; i < size; i++) bigger[i] = data[i];
            data = bigger;
        }
    }

    // ---------- Реализовано ----------

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        String s = "";
        for (int i = 0; i < size; i++) {
            if (i > 0) s += ", ";
            s += data[i];
        }
        return "[" + s + "]";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        grow();
        data[size++] = e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        E old = (E) data[index];
        for (int i = index; i < size - 1; i++) data[i] = data[i + 1];   // сдвиг влево
        data[--size] = null;
        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int size() {
        return size;
    }

    // ---------- Не нужно для этого задания ----------

    @Override
    public void add(int index, E element) { throw new UnsupportedOperationException(); }

    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public E set(int index, E element) { throw new UnsupportedOperationException(); }

    @Override
    public boolean isEmpty() { throw new UnsupportedOperationException(); }

    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    @Override
    public int indexOf(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public E get(int index) { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public int lastIndexOf(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }

    @Override
    public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }

    @Override
    public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

}
