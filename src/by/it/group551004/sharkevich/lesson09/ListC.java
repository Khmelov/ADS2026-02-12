package by.it.group551004.sharkevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private Object[] elements;
    private int size;

    public ListC() {
        elements = new Object[1];
        size = 0;
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1)
                result += ", ";
        }
        return result + "]";
    }

    @Override
    public boolean add(E e) {
        ensureEnoughSize(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        E oldVal = (E)elements[index];
        for (int i = index; i < size - 1; i++)
            elements[i] = elements[i + 1];
        elements[--size] = null;
        return oldVal;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        ensureEnoughSize(size + 1);
        for (int i = size; i > index; i--)
            elements[i] = elements[i - 1];
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0)
            return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        E aim = (E)elements[index];
        elements[index] = element;
        return aim;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (o.equals(elements[i]))
                return i;
        return -1;
    }

    @Override
    public E get(int index) {
        return (E)elements[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (o.equals(elements[i]))
                return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i > -1; i--)
            if (o.equals(elements[i]))
                return i;
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E o : c)
            add(size, o);
            isModified = true;
        return isModified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean isModified = false;
        for (E o : c) {
            add(index++, o);
            isModified = true;
        }
        return isModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        for (int i = size - 1; i > -1; i--)
            if (c.contains(elements[i])) {
                remove(i);
                isModified = true;
            }
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        for (int i = size - 1; i > -1; i--)
            if (!c.contains(elements[i])) {
                remove(i);
                isModified = true;
            }
        return isModified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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

    public void ensureEnoughSize(int requiredSize) {
        if (requiredSize > elements.length) {
            int newSize = elements.length * 2;
            Object[] newElements = new Object[newSize];
            for (int i = 0; i < size; i++)
                newElements[i] = elements[i];
            elements = newElements;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
