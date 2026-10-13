package by.it.group551002.efimchikov.lesson09;

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
    private Object[] data= new Object[16];
    private int size=0;

    @Override
    public String toString() {
        StringBuilder output= new StringBuilder();
        output.append("[");
        for(int i=0;i<size;i++){
            if(data[i]!=null)
                output.append(data[i].toString());
            if(i!=size-1) output.append(", ");
        }
        output.append("]");
        return output.toString();
    }

    private void resize() {
        int newSize=data.length*2;
        Object[] newData =new Object[newSize];
        if (this.size >= 0) System.arraycopy(data, 0, newData, 0, this.size);
        this.data=newData;
    }

    @Override
    public boolean add(E e) {
        if (this.size + 1 == data.length)
            resize();
        data[this.size++]=e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if(index>=size || index<0)
            return null;
        E ret=(E)data[index];
        for(int i=index;i<this.size;i++){
            data[i]=data[i+1];
        }
        size--;
        return ret;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        if(index<0 || index>size) {
            throw new IndexOutOfBoundsException();
        }
        if(size+1==data.length)
            resize();
        for(int i=size;i>=index;i--)
            data[i+1]=data[i];
        data[index]=element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for(int i=0;i<size;i++){
            if(data[i].equals(o)) {
                this.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if(index<0 || index>=size)
            throw new IndexOutOfBoundsException();
        E old=(E)data[index];
        data[index]=element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        for(int i=0;i<size;i++)
            data[i]=null;
        size=0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i=0;i<size;i++)
            if(data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if(index<0 || index>=size)
            return null;
        return (E)data[index];
    }

    @Override
    public boolean contains(Object o) {
        for(int i=0;i<size;i++)
            if(data[i].equals(o)) return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i=size-1;i>=0;i--){
            if(data[i].equals(o)) return i;
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
