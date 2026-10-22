package by.it.group510901.kostykovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {
    private Object[] list = new Object[0];
    private int currentSize;
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String response = "[";
        if(this.size() == 0){
            return "[]";
        }else {
            for (int i=0; i < this.size(); i++) {
                response += list[i];
                if(i < this.size()-1){
                    response += ", ";
                }
            }
            response += "]";
        }
        return response;
    }

    @Override
    public boolean add(E e) {
        if(list.length <= 0){
            Object[] notNullList = new Object[10];
            list = notNullList;
        }
        if(currentSize < list.length) {
            list[this.size()] = e;
            currentSize++;
        }else{
            Object[] newList = new Object[currentSize *2];
            for(int i = 0; i < list.length; i++){
                newList[i] = list[i];
            }
            newList[this.size()] = e;
            list = newList;
            currentSize++;
        }
        return true;
    }

    @Override
    public E remove(int index) {
        E removed = (E) list[index];
        Object[] newList = new Object[currentSize-1];
        if(index < 0 || index >= currentSize){
            throw new IndexOutOfBoundsException();
        }
        for(int i = index; i < currentSize - 1 ; i++){
            list[i] = list[i+1];
        }
        list[currentSize - 1] = null;
        currentSize--;
        return removed;
    }

    @Override
    public int size() {
        return currentSize;
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
        return null;
    }

}
