package by.it.group551002.rybik.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 15;
    private int size = 0;
    private Object[] array = new Object[capacity];


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < size-1; i++){
            sb.append(array[i]);
            sb.append(", ");
        }
        if ((size != 0)) {
            sb.append(array[size - 1]);
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if(size >= capacity){
            Object[] newArray = new Object[capacity * 2];
            if (newArray == null) return false;

            for(int i = 0; i < size; i++){
                newArray[i] = array[i];
            }
            newArray[size] = e;
            capacity = capacity * 2;
            array = newArray;
        } else{
            array[size] = e;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if(index < size){
            E element = (E) array[index];
            for(int i = index; i < size-1; i++) {
                array[i] = array[i+1];
            }
            array[size - 1] = null;
            size--;
            return element;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if(size >= capacity) {
            Object[] newArray = new Object[capacity * 2];
            for(int i = 0; i < index; i++){
                newArray[i] = array[i];
            }
            newArray[index] = element;
            for(int i = index; i < size; i++){
                newArray[i + 1] = array[i];
            }
            capacity = capacity * 2;
            array = newArray;
        } else {
            for(int i = size; i > index; i-- ) {
                array[i] = array[i-1];
            }
            array[index] = element;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index > -1) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index >= size) return null;
        else {
            E o = (E) array[index];
            array[index] = element;
            return o;
        }

    }


    @Override
    public boolean isEmpty() {
       return size == 0;
    }


    @Override
    public void clear() {
        Object[] newArray = new Object[15];
        array = newArray;
        size = 0;
        capacity = 15;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (index < size) ? (E) array[index] : null;
    }

    @Override
    public boolean contains(Object o) {
        if(o == null){
            for (int i = size-1; i >= 0; i--) {
                if (array[i] == null) return true;
            }
        } else {
            for (int i = size-1; i >= 0; i--) {
                if (o.equals(array[i])) return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == null){
            for (int i = size-1; i >= 0; i--) {
                if (array[i] == null) return i;
            }
        } else {
            for (int i = size-1; i >= 0; i--) {
                if (o.equals(array[i])) return i;
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
