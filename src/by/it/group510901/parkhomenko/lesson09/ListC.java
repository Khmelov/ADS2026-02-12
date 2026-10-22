package by.it.group510901.parkhomenko.lesson09;

import java.util.*;

// Список на массиве. Массив data хранит элементы, size - сколько их сейчас.
public class ListC<E> implements List<E> {

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

    @Override
    @SuppressWarnings("unchecked")
    public void add(int index, E element) {
        grow();
        for (int i = size; i > index; i--) data[i] = data[i - 1];       // сдвиг вправо
        data[index] = element;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        data = new Object[10];
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) data[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) add(index++, e);
        return !c.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--)
            if (c.contains(data[i])) { remove(i); changed = true; }
        return changed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--)
            if (!c.contains(data[i])) { remove(i); changed = true; }
        return changed;
    }

    // ---------- Не нужно для этого задания ----------

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
