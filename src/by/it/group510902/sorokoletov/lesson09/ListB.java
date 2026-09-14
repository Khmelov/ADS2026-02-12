package by.it.group510902.sorokoletov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {
    private Object[] elements = new Object[10];
    private int size = 0;

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
            if (i < size - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            int newCapacity = elements.length == 0 ? 10 : elements.length * 2;
            Object[] newElements = new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }

        elements[size] = e;
        size++;

        return true;
    }

    @Override
    public E remove(int index) {
        E oldValue = (E) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return oldValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        Object[] newElements = new Object[elements.length + 1];

        int ptr1 = 0;
        int ptr2 = 0;

        while (ptr1< newElements.length || ptr2< elements.length){
            if (ptr1 == index){
                newElements[ptr1] = element;
                ptr1++;
            }
            else{
                newElements[ptr1] = elements[ptr2];
                ptr1++;
                ptr2++;
            }
        }
        elements = newElements;
        size++;

    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {


                for (int j = i; j < size - 1; j++) {
                    elements[j] = elements[j + 1];
                }

                size--;
                elements[size] = null;
                return true;
            }

        }


        return false;
    }

    @Override
    public E set(int index, E element) {
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }


    @Override
    public boolean isEmpty() {
        return this.size==0;
    }



    @Override
    public void clear() {
        for(int i = 0; i<size; i++){
            elements[i]=null;

        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;}

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
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
