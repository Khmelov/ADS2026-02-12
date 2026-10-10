package by.it.group510902.ryzhkov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ
    // БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////// Обязательные к реализации методы ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    private void increaseCapacity() {
        Object[] newElements = new Object[size * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    public ListA() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder result = new StringBuilder();
        result.append("[");

        for (int i = 0; i < size; i++) {
            result.append(elements[i]);
            if (i < size - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length)
            increaseCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index out of range");
        E removed = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        elements[size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////// Опциональные к реализации методы ///////
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
    //////// Эти методы имплементировать необязательно ////////////
    //////// но они будут нужны для корректной отладки ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
