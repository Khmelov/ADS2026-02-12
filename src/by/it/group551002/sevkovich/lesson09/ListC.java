package by.it.group551002.sevkovich.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private E[] elements;
    public int size;
    public int capacity;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public ListC(int defaultCapacity) {
        elements = (E[]) new Object[defaultCapacity];
        size = 0;
        capacity = defaultCapacity;
    }

    public ListC() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void growList() {
        capacity += (capacity >> 3) + 6;
        E[] newList = (E[]) new Object[capacity];
        System.arraycopy(elements, 0, newList, 0, size);
        elements = newList;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < size; i++) {
            result.append(elements[i]).append(", ");
        }

        result.setLength(result.length() - 2);
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == capacity) {
            growList();
        }

        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E result = (E) elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == capacity) {
            growList();
        }

        for (int i = size - 1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int found = -1;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) {
                found = i;
                break;
            }
        }

        if (found == -1) {
            return false;
        }

        remove(found);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }

        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int found = -1;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) {
                found = i;
                break;
            }
        }

        return found;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int found = -1;
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, elements[i])) {
                found = i;
                break;
            }
        }

        return found;
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
        if (c.isEmpty()) {
            return false;
        }

        for (E e : c) {
            add(e);
        }

        return true;
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
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        E[] newList = (E[]) new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newList[newSize] = elements[i];
                newSize++;
            }
        }

        boolean changed = newSize != size;
        elements = newList;
        capacity = elements.length;
        size = newSize;
        return changed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        E[] newList = (E[]) new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newList[newSize] = elements[i];
                newSize++;
            }
        }

        boolean changed = newSize != size;
        elements = newList;
        capacity = elements.length;
        size = newSize;
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
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаем новый массив нужного типа и размера, если переданный слишком мал
            return (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // По спецификации Java, элемент после size должен быть null
        }
        return a;
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
