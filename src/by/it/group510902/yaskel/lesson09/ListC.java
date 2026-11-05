package by.it.group510902.yaskel.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private E[] elements;
    private int size; // реально добавленные элементы(количество)
    private static final int DEFAULT_CAPACITY = 5;

    public ListC() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
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

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            E element = elements[i];

            if (element == this) {
                sb.append("(this List)");
            } else {
                sb.append(element);
            }

            if (i != size - 1) {
                sb.append(", ");
            }
        }

        return sb.append(']').toString();
    }

    // расширяем массив ровно настолько, сколько нужно под minCapacity
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.min(elements.length * 2, minCapacity);
            E[] newElements = (E[]) new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void checkIndexForGet(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    private boolean elementsEqual(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndexForGet(index);

        E removed = elements[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[size - 1] = null;
        size--;

        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);

        ensureCapacity(size + 1);

        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        checkIndexForGet(index);
        E old = elements[index];
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
        for (int i = 0; i < size; i++) {
            if (elementsEqual(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndexForGet(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (elementsEqual(elements[i], o)) {
                return i;
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
        checkIndexForAdd(index);

        int i = index;
        boolean changed = false;
        for (E e : c) {
            add(i, e);
            i++;
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(i);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                changed = true;
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
