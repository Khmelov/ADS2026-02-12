package by.it.group551002.voroshko.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private E[] elements = (E[]) new Object[10];

    private int size = 0;

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
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 3 / 2 + 1];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }

        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        E removedElement = elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        size--;
        elements[size] = null;

        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 3 / 2 + 1];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
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
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elements[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E item : c) {
            add(item);
            isModified = true;
        }
        return isModified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int cSize = c.size();
        if (cSize == 0) return false;

        int minCapacity = size + cSize;

        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 3 / 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        for (int i = size - 1; i >= index; i--) {
            elements[i + cSize] = elements[i];
        }

        for (E item : c) {
            elements[index++] = item;
        }

        size += cSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int writeIndex = 0;
        boolean isModified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                isModified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int writeIndex = 0;
        boolean isModified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                isModified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }

        size = writeIndex;
        return isModified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(elements[i]);
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            private int cursor = index;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                return elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                return elements[--cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                ListC.this.remove(--cursor);
            }

            @Override
            public void set(E e) {
                ListC.this.set(cursor - 1, e);
            }

            @Override
            public void add(E e) {
                ListC.this.add(cursor++, e);
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }

        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] newArray = new Object[size];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        return newArray;
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
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                return elements[cursor++];
            }
        };
    }

}
