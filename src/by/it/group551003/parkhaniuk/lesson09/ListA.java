package by.it.group551003.parkhaniuk.lesson09;

import java.io.ObjectInput;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    private int size;
    private int capacity;
    private E[] data;

    public ListA() {
        size = 0;
        capacity = 10;
        data = (E[]) new Object[capacity];
    }
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        String res = "";

        res += ("[" + data[0]);
        for(int i = 1; i < size; i++)
            res += (", " + data[i].toString());
        res += "]";
        return res;
    }

    private void ensureCapacity()
    {
        capacity *= 2;
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);

        data = newData;
    }


    @Override
    public boolean add(E e) {
        if (size == capacity)
            ensureCapacity();

        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index >= 0 && index < size) {
            E deleteVal = data[index];
            System.arraycopy(data, index + 1, data, index, size - index - 1);
            data[size--] = null;
            return deleteVal;
        }
        return null;
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
