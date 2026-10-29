package by.it.group510901.kostykovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
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

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > currentSize){
            throw new IndexOutOfBoundsException();
        }
        if(list.length <= 0){
            Object[] notNullList = new Object[10];
            list = notNullList;
        }
        if(currentSize < list.length){
            for(int i = currentSize-1; i >= index; i--){
                list[i+1] = list[i];
            }
            list[index] = element;
            currentSize++;
        }else{
            Object[] newList = new Object[list.length*2];

            for(int i = 0; i < index; i++){
                newList[i] = list[i];
            }
            newList[index] = element;
            for(int i = index; i < currentSize; i++){
                newList[i+1] = list[i];
            }
            list = newList;
            currentSize++;
        }

    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= currentSize){
            throw new IndexOutOfBoundsException();
        }
        E memo = (E) list[index];
        list[index] = element;
        return memo;
    }


    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }


    @Override
    public void clear() {
        list = new Object[0];
        currentSize = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i<currentSize; i++){
            if (o == null) {
                if (list[i] == null) return i;
            } else {
                if (o.equals(list[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= currentSize){
            throw new IndexOutOfBoundsException();
        }
        return (E) list[index];
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i<currentSize; i++){
            if(list[i].equals(o)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = currentSize - 1; i >= 0; i--){
            if (o == null) {
                if (list[i] == null) return i;
            } else {
                if (o.equals(list[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
       for(Object o: c) {
           if (this.contains(o)) {
               continue;
           } else {
               return false;
           }
       }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = false;
            for(E o: c){
                this.add(o);
                flag = true;
            }
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean flag = false;
        for(E o: c){
            this.add(index++, o);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for(int i = currentSize - 1; i >= 0; i--){
            if(c.contains(list[i])){
                this.remove(i);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean flag = false;
        for(int i = currentSize - 1; i >= 0; i--){
            if(!c.contains(list[i])){
                this.remove(i);
                flag = true;
            }
        }
        return flag;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
