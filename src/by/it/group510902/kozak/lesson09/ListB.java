package by.it.group510902.kozak.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements = new Object[10];
    private int size = 0;
    private void ensure() {
        if (size >= elements.length) {
            int newCapacity = elements.length + elements.length / 2;
            Object[] newElements = new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i<size; i++){
            if (i>0) {
                result += ", ";
            }
            result += elements[i];
        }
        result+= "]";
        return result ;
    }

    @Override
    public boolean add(E e) {
        ensure();
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index<0 || index >=size){
            throw new IndexOutOfBoundsException();
        }
        E removed = (E) elements[index];
        for (int i=index; i <size-1; i++){
            elements[i] = elements[i+1];
        }
        elements[size-1] = null;
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;

    }

    @Override
    public void add(int index, E element) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        for (int i = size; i>index; i--){
            elements[i]=elements[i-1];
        }
        ensure();
        elements[index]=element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index== -1) return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return size ==0;
    }


    @Override
    public void clear() {
        for (int i=0; i<size; i++){
            elements[i]=null;
        }
        size=0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i<size; i++){
            if (elements[i]==null && o == null) return i;
            if (elements[i]!=null && elements[i].equals(o)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) !=-1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i>=0; i--){
            if (elements[i]==null && o == null) return i;
            if (elements[i]!=null && elements[i].equals(o)) return i;
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
