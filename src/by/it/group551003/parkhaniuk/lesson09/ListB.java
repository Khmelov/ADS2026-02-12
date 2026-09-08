package by.it.group551003.parkhaniuk.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {


    private int size;
    private int capacity;
    private E[] data;

    public ListB() {
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
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        if (size == capacity)
            ensureCapacity();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;
        for(int i = 0; i < size && !found; i++) {
            if (o.equals(data[i])) {
                remove(i);
                found = true;
            }
        }
        return found;
    }

    @Override
    public E set(int index, E element) {
        E removed = data[index];
        data[index] = element;
        return removed;
    }


    @Override
    public boolean isEmpty() {
        return (size == 0);
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            data[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null)
            for (int i = 0; i < size; i++)
                if (o.equals(data[i]))
                    return i;
        return -1;
    }

    @Override
    public E get(int index) {
        if (index >= 0 && index < size)
            return data[index];
        return null;
    }

    @Override
    public boolean contains(Object o) {
        if (o != null)
            for (int i = 0; i < size; i++)
                if (o.equals(data[i]))
                    return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null)
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(data[i]))
                    return i;
        return -1;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


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
