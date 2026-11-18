package by.it.group551003.kravchenkovikk.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private int size;
    private Object[] array;

    ListC(){
        this.array = new Object[100];
        this.size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String str = "[";
        for (int i = 0; i < size; i++) {
            str += array[i];
            if (i == size - 1) {
                str += "]";
            } else {
                str += ", ";
            }
        }
        return str;
    }

    @Override
    public boolean add(E e) {
        if (size == array.length) {
            Object[] newArray = new Object[array.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index >= size) {
            return null;
        }
        E removedEl = (E)array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null;
        size--;
        return removedEl;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == array.length) {
            Object[] newArray = new Object[array.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index >= size) {
            return null;
        }
        E res = (E)array[index];
        array[index] = element;
        return res;
    }


    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (size == 0) {
            return -1;
        }
        for (int i = 0; i < size; i++){
            if (o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index >= size) {
            return null;
        }
        return (E) array[index];
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (size == 0) {
            return - 1;
        }
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj: c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E obj: c) {
            add(obj);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E obj: c) {
            add(index, obj);
            index++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isOk = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(array[i])) {
                remove(i);
                isOk = true;
            }
        }
        return isOk;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(array[i])) {
                remove(array[i]);
            }
        }
        return true;
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
