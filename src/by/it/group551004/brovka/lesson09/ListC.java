package by.it.group551004.brovka.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    private int size;
    private E[] arr;

    public ListC() {
        arr = (E[]) new Object[10];  // начальный размер 10
        size = 0;}

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String str;
        str = "";
        str = str +"[";
        for (int i = 0; i < size; i++) {
            str = str + arr[i];
            if (i + 1 != size)
                str = str + ", ";
        }
        str = str +"]";
        return str;
    }


    @Override
    public boolean add(E e) {
        if (size == arr.length) {
            E[] arrnew = (E[]) new Object[arr.length * 2 + 1];
            for (int i =0; i < size; i++)
                arrnew[i] = arr[i];
            arrnew[size] = e;
            arr = arrnew;
        } else arr[size] = e;
        size ++;
        return true;
    }

    @Override
    public E remove(int index) {
        E rem;
        rem = arr[index];
        for (int i = index; i < size - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[size - 1] = null;
        size = size - 1;
        return rem;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == arr.length) {
            E[] arrnew = (E[]) new Object[arr.length * 2 + 1];
            for (int i = 0; i < index; i ++)
                arrnew[i] = arr[i];
            for (int i = size; i  > index; i--) {
                arrnew[i] = arr[i -1];
            }
            arrnew[index] = element;
            arr = arrnew;
        } else {
            for (int i = size; i  > index; i--)
                arr[i] = arr[i - 1];
            arr[index] = element;
        }
        size ++;

    }

    @Override
    public boolean remove(Object o) {
        int a;
        boolean is;
        is = true;
        a = indexOf(o);
        if (a == -1) {
            is = false    ;
        } else {
            for (int i = a; i + 1 < size; i++)
                arr[i] = arr[i + 1];
            arr[size - 1] = null;
            size--;
        }
        return is;
    }

    @Override
    public E set(int index, E element) {
        E oldarr;
        oldarr = arr[index];
        arr[index] = element;
        return oldarr;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            arr[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int a;
        a = -1;
        for ( int i = size - 1 ; i >= 0; i --)
            if (o.equals(arr[i])) a = i;
        return a;
    }

    @Override
    public E get(int index) {
        return arr[index];
    }

    @Override
    public boolean contains(Object o) {
        boolean isbe;
        isbe = true;
        if (indexOf(o) == -1)
            isbe = false;
        return isbe;
    }

    @Override
    public int lastIndexOf(Object o) {
        int a;
        a = -1;
        for ( int i = 0; i < size; i ++)
            if (o.equals(arr[i])) a = i;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean is, are;

        are = true;
        for (Object e : c){
            is = false;
            for (int i = 0; i < size ; i++)
                if (e.equals(arr[i])) is = true;
            if (!is)  are = false;
        }
        return are;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean is;
        is = false;
        for (E e : c) {
            add(e);
            is = true;
        }
        return is;
    }


    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int i = index;
        boolean is;
        is = false;
        for (E e : c) {
            add(i, e);
            i ++;
            is = true;
        }
        return is;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ischanged = false;
        for (Object e : c) {
            ischanged = ischanged || remove(e);
            while (remove(e));
        }
        return ischanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ischanged = false;

        int i = 0;
        while (i < size) {
            boolean isin = false;
            for (Object e : c)
                if (e.equals(arr[i])) isin = true;
            if (!isin) {
                remove(i);
                ischanged = true;
            } else i++;
        }
        return ischanged;
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
