package by.it.group510901.pshanichny.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private int count;
    private Object[] array;

    public ListC() {
        array = new Object[10];
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                result += ", " + array[i];
            } else {
                result += array[i];
            }
        }
        result += "]";
        return result;
    }

    @Override
    public boolean add(E e) {
        if (count == array.length) {
            Object[] newArray = new Object[count*2];
            for(int i = 0; i < count; i++){
                newArray[i] = array[i];
            }
            array = newArray;
        }

        array[count] = e;
        count++;

        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне границ списка");
        }

        E removedElement = (E) array[index];

        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }

        count--;
        array[count] = null;

        return removedElement;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне границ списка");
        }
        if (count == array.length) {
            Object[] newArray = new Object[count*2];
            for(int i = 0; i < count; i++){
                newArray[i] = array[i];
            }
            array = newArray;
        }
        for (int i = count; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        count++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < count; i++) {
            if (array[i] == null ? o == null : array[i].equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне границ списка");
        }
        E oldElement = (E) array[index];
        array[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return count == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < count; i++) {
            array[i] = null;
        }
        count = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < count; i++) {
            if (array[i] == null ? o == null : array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне границ списка");
        }
        return (E) array[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = count - 1; i >= 0; i--) {
            if (array[i] == null ? o == null : array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c){
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (!c.isEmpty()) {
            for (E o : c) {
                add(o);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (!c.isEmpty()) {
            for (E o : c) {
                add(index, o);
                index++;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object o : c) {
            while (remove(o)){
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        for (int i = 0; i < count;) {
            if (!c.contains(array[i])) {
                remove(i);
                result = true;
            }
            else {
                i++;
            }
        }
        return result;
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
