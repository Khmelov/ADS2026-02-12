package by.it.group510901.jalilova.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private Object[] elements = new Object[10]; // массив-хранилище
    private int size = 0;          // сколько элементов реально лежит

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        sb.append(elements[0]);
        for (int i = 1; i < size; i++ ){
            sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length){
            int newCapacity = elements.length == 0 ? 10 : elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size]=e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        E temp = (E)elements[index];
        int newCapacity = elements.length == 0 ? 10 : elements.length - 1;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < index; i++) {
            newElements[i] = elements[i];
        }
        for (int i = index+1; i < size; i++) {
            newElements[i-1] = elements[i];
        }
        elements = newElements;
        size--;
        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс должен быть от нуля, до size");
        }
        if (size == elements.length){
            int newCapacity = elements.length == 0 ? 10 : elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1]; // Переносим элемент в соседнюю правую ячейку
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for(int i = 0; i < size; i++){
            if((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))){
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E temp = (E)elements[index];
        elements[index]=element;
        return temp;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        Object[] emptyArray = new Object[10];
        elements = emptyArray;
        size=0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++){
            if((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))){
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E)elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size-1; i >= 0; i--){
            if((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))){
                return i;
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
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                // Если курсор меньше реального размера списка, значит элементы еще есть
                return cursor < size;
            }

            @Override
            public E next() {
                // Если пользователь вызвал next(), а элементы кончились — бросаем стандартную ошибку
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                // Запоминаем текущий элемент, двигаем курсор вперед и возвращаем элемент
                E nextElement = (E) elements[cursor];
                cursor++;
                return nextElement;
            }
        };
    }

}
