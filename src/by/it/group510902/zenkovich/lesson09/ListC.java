package by.it.group510902.zenkovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    Object[] elements;
    int size;

    public ListC(){
        this.elements = new Object[100000];
        this.size = 0;
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        checkCapacity(size + 1);
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) elements[index];
        int num = size-index-1;
        if (num > 0) {
            System.arraycopy(elements, index + 1, elements, index, num);
        }
        elements[--size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }


    void checkCapacity (int minCapacity){
        if (minCapacity > elements.length){


            int newCapacity = elements.length * 2;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(elements, 0, newArray, 0, size);
            elements = newArray;

        }
    }

    void checkIndex (int index){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }


    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        checkCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index );
        elements[index] = element;
        size++;
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0){
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++){
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (o.equals((elements[i]))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        int i = index;
        for (E e : c) {
            add(i, e);
            i++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (c.contains(elements[i])) {
                remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
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
