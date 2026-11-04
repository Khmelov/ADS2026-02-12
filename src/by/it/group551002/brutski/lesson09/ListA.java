package by.it.group551002.brutski.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private int size = 0;
    private Object[] elems = new Object[15];

    private void realloc_elems(int min_size) {
        double k = 1.5;
        if (min_size > elems.length) {
            int newCapacity = elems.length == 0 ? 15 : (int) (elems.length * k);
            if (newCapacity < min_size) {
                newCapacity = min_size;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elems, 0, newElements, 0, size);
            elems = newElements;
        }
    }

    private void check_ind(int ind, boolean non_strict) {
        if (non_strict) {
            if (ind < 0 || ind > size) {
                throw new IndexOutOfBoundsException();
            }
        }
        else {
            if (ind < 0 || ind >= size) {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                str.append(", ");
            }
            Object e = elems[i];
            str.append(e == this ? "(this Collection)" : String.valueOf(e));
        }
        str.append("]");

        return str.toString();
    }

    @Override
    public boolean add(E e) {
        realloc_elems(size + 1);
        elems[size++] = e;
        return true;
    }

    @Override
    public E remove(int ind) {
        check_ind(ind, false);
        E removed = (E) elems[ind];
        System.arraycopy(elems, ind + 1, elems, ind, size - ind - 1);
        elems[size--] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
