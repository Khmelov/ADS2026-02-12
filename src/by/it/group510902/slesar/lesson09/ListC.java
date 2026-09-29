package by.it.group510902.slesar.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements = new Object[DEFAULT_CAPACITY];
    private int size;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }
    }
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        String result = "[";

        for (int i = 0; i < size; i++) {
            result += elements[i];

            if (i < size - 1) {
                result += ", ";
            }
        }

        result += "]";

        return result;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();

        elements[size] = e;
        size++;

        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E removed = (E) elements[index];

        for(int i =index; i< size-1;i++){
            elements[i] = elements[i+1];
        }
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
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity();

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;

    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E oldElement = (E) elements[index];
        elements[index] = element;

        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
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
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }

        return -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;

        for (E element : c) {
            add(element);
            modified = true;
        }

        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        boolean modified = false;

        for (E element : c) {
            add(index, element);
            index++;
            modified = true;
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListB<E> result = new ListB<>();

        for (int i = fromIndex; i < toIndex; i++) {
            result.add((E) elements[i]);
        }

        return result;
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
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),
                    size
            );
        }

        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];

        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }

        return result;
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
