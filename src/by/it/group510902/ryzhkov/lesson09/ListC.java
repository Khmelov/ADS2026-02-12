package by.it.group510902.ryzhkov.lesson09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

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

    public ListC() {
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

    @Override
    public void add(int index, E element) {
        if (size == elements.length)
            increaseCapacity();
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index out of range");
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        elements[size] = null;
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index out of range");
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index out of range");
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;

        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    lastIndex = i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    lastIndex = i;
                }
            }
        }

        return lastIndex;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] newElements = c.toArray();
        int newSize = newElements.length;

        while (size + newSize > elements.length) {
            increaseCapacity();
        }

        System.arraycopy(newElements, 0, elements, size, newSize);
        size += newSize;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index out of range");
        Object[] newElements = c.toArray();
        int newSize = newElements.length;

        while (size + newSize > elements.length) {
            increaseCapacity();
        }

        System.arraycopy(elements, index, elements, index + newSize, size - index);
        System.arraycopy(newElements, 0, elements, index, newSize);
        size += newSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int writeIndex = 0;
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[writeIndex++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
            }
            size = writeIndex;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int writeIndex = 0;
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[writeIndex++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
            }
            size = writeIndex;
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////// Опциональные к реализации методы ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Invalid subList indexes");
        }

        ListC<E> subList = new ListC<>();
        for (int i = fromIndex; i <= toIndex; i++) {
            subList.add((E) elements[i]);
        }
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index out of range");

        List<E> tempList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tempList.add((E) elements[i]);
        }

        return tempList.listIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }

        System.arraycopy(elements, 0, a, 0, size);
        return a;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
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
