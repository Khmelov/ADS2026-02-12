package by.it.group510901.blyshko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private Object[] values;
    private int size = 0;

    public ListC() {
        this.values = new Object[10];
    }

    public ListC(int initialSize) {
        if (initialSize < 0) throw new IllegalArgumentException("Размер не может быть отрицательным:" + initialSize);
        this.values = new Object[initialSize];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String str = "[";

        if (size == 0) return "[]";

        for (int i = 0; i < size-1; i++) str += String.valueOf(values[i] + ", ");
        str += values[size - 1] + "]";

        return str;
    }

    @Override
    public boolean add(E e) {
        if (size == values.length) {

            Object[] boof = new Object[size];
            for (int i = 0; i < size; i++) {
                boof[i] = values[i];
            }

            values = new Object[values.length > 0 ? (size + size/2) : 10];

            for (int i = 0; i < size; i++) {
                values[i] = boof[i];
            }
        }

        values[size++] = e;

        return true;
    }

    @Override
    public E remove(int index) {
        E obj = (E) values[index];
        size--;
        for(int i = index; i < size; i++) {
            values[i] = values[i+1];
        }
        values[size] = null;
        return obj;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == values.length) {

            Object[] boof = new Object[size];

            int i = 0;
            for(var el : values) { boof[i] = el; i++; }

            values = new Object[size > 0 ? size + size/2 : 10];

            for (i = 0; i<size; i++) values[i] = boof[i];
        }

        for(int i = size; i > index; i--) values[i] = values[i-1];
        values[index] = element;
        size++;
    }
 @Override
    public boolean remove(Object o) {
        int indx = indexOf(o);
        if(indx == -1)  return false;

        size--;
        for (int i = indx; i < size; i++)  values[i] = values[i+1];
        values[size] = null;

        return true;
    }

    @Override
    public E set(int index, E element) {
        E obj = (E) values[index];
        values[index] = element;
        return obj;
    }


    @Override
    public boolean isEmpty() {
        if (size > 0) return false;
        return true;
    }


    @Override
    public void clear() {
        for(int i = 0; i<size; i++) {
            values[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int indx = -1;

        if(o == null) {
            for(int i = 0; i < size; i++) {
                if(values[i] == null) {
                    indx = i;
                    break;
                }
            }
        }
        else {
            for(int i = 0; i < size; i++) {
                if(o.equals(values[i])) {
                    indx = i;
                    break;
                }
            }
        }
        return indx;
    }

    @Override
    public E get(int index) {
        E obj = (E) values[index];
        return obj;
    }

    @Override
    public boolean contains(Object o) {
        if (indexOf(o) == -1) return false;
        return true;
    }

    @Override
    public int lastIndexOf(Object o) {
        int indx = -1;

        if(o == null) {
            for(int i = size - 1; i >= 0; i--) {
                if(values[i] == null) {
                    indx = i;
                    break;
                }
            }
        }
        else {
            for(int i = size - 1; i >= 0; i--) {
                if(o.equals(values[i])) {
                    indx = i;
                    break;
                }
            }
        }

        return indx;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for(Object el : c) {
            if (!contains(el)) { return false; }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.size() == 0) return false;

        if (size == values.length || size + c.size() > values.length) {

            Object[] buff = new Object[values.length > 0 ? (size + c.size() + (size + c.size())/2) : c.size() + 10];

            for (int i = 0; i < size; i++) {
                buff[i] = values[i];
            }

            values = buff;
        }

        for(E el : c) {
            values[size++] = el;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(c.size() == 0) return false;

        if (size == values.length || size + c.size() > values.length) {

            Object[] buff = new Object[values.length > 0 ? (size + c.size() + (size + c.size())/2) : c.size() + 10];

            for (int i = 0; i < size; i++) {
                buff[i] = values[i];
            }

            values = buff;
        }

        for (int i = size - 1; i >= index; i--) {
            values[i+c.size()] = values[i];
        }

        int i = index;
        for (Object el : c) {
            values[i] = el;
            i++;
            size++;
        }
        return true;
    }
 @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for(int i = 0; i < size; i++) {
            if (c.contains(values[i])) {
                remove(i);
                i--;
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        for(int i = 0; i < size; i++) {
            if (!c.contains(values[i])) {
                remove(i);
                i--;
                result = true;
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
