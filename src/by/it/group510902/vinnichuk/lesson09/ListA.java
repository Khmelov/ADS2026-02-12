package by.it.group510902.vinnichuk.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Object[] elements;

    private int size;

    public ListA(){
        this.elements = new Object[10];
        this.size=0;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        for (int i=0; i<size; i++){
            sb.append(elements[i]);
            if (i<size-1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if(size==elements.length){
            Object[] newElements = new Object[elements.length*3/2+1];
            for (int i=0; i<elements.length; i++){
                newElements[i]=elements[i];
            }
            elements=newElements;
        }

        elements[size++]=e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        E removedElement = (E) elements[index];

        for (int i=index; i<size-1; i++){
            elements[i]=elements[i+1];
        }

        size--;
        elements[size]=null;
        return removedElement;
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
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 3 / 2 + 1];
            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
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
        // Если ищем null, сравниваем элементы через null
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            // Если объект не null, сравниваем через .equals()
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        // Если элемент не найден, то возвращаем -1
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >=0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
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
        Object[] exactArray = new Object[size];
        for (int i=0; i<size; i++){
            exactArray[i]=elements[i];
        }
        return exactArray;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>(){
            private int cursor = 0;
            @Override
            public boolean hasNext(){
                return cursor<size;
            }
            @Override
            @SuppressWarnings("unchecked")
            public E next(){
                if (cursor >=size){
                    throw new NoSuchElementException();
                }
                return (E) elements[cursor++];
            }
        };
    }

}
