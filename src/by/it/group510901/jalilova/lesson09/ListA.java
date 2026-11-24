package by.it.group510901.jalilova.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements = new Object[10]; // массив-хранилище
    private int size = 0;          // сколько элементов реально лежит

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
        StringBuilder sb = new StringBuilder("[");
        sb.append(elements[0]);
        for (int i = 1; i < size; i++ ){
            sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length){
            int newCapacity = elements.length == 0 ? 10 : elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size]=e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        E temp = (E)elements[index];
        int newCapacity = elements.length == 0 ? 10 : elements.length - 1;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < index; i++) {
            newElements[i] = elements[i];
        }
        for (int i = index+1; i < size; i++) {
            newElements[i-1] = elements[i];
        }
        elements = newElements;
        size--;
        return temp;
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
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                // Если курсор меньше реального размера списка, значит элементы еще есть
                return cursor < size;
            }

            @Override
            public E next() {
                // Если пользователь вызвал next(), а элементы кончились — бросаем стандартную ошибку
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                // Запоминаем текущий элемент, двигаем курсор вперед и возвращаем элемент
                E nextElement = (E) elements[cursor];
                cursor++;
                return nextElement;
            }
        };
    }

}
