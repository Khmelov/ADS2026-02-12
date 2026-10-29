package by.it.group551004.pedko.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private Object[] array;
    private int size = 0;
    private int capacity = 0;
    private static final int DEFAULT_CAPACITY = 5;

    void init() {
        array = new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    void extend() {
        if (capacity == 0) {
            init();
        } else {
            int newCapacity = array.length * 2;
            Object[] newArray = new Object[newCapacity];
            for (int i = 0; i < size; ++i) {
                newArray[i] = array[i];
            }
            array = newArray;
            capacity = newCapacity;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String answer = "[";

        for (int i = 0; i < size; ++i) {
            answer = answer.concat(array[i].toString());
            if (i != size-1) {
                answer = answer.concat(", ");
            }
        }
        answer = answer.concat("]");

        return answer;
    }

    @Override
    public boolean add(E e) {
        if (capacity == 0 || size == capacity) {
            extend();
        }

        array[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        E removed = null;

        if (index < size) {
            removed = (E) array[index];
            for (int i = index + 1; i < size; ++i) {
                array[i-1] = array[i];
            }
            size--;
        }
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
        if (index < size) {
            if (size == capacity) {
                extend();
            }
            for (int i = size + 1; i > index; --i) {
                array[i] = array[i-1];
            }
            array[index] = element;
            size++;
        }
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
        return size == 0;
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
