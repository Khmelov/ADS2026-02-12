package by.it.group551004.bulavin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private int size;
    private int capacity;
    private E[] data;

    public ListC() {
        size = 0;
        capacity = 10;
        data = (E[]) new Object[capacity];
    }

    public ListC(int capacity) {
        size = 0;
        this.capacity = capacity;
        data = (E[]) new Object[capacity];
    }
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        String res = "";

        res += ("[" + data[0]);
        for(int i = 1; i < size; i++)
            res += (", " + data[i].toString());
        res += "]";
        return res;
    }

    private void ensureCapacity()
    {
        capacity *= 2;
        E[] newData = (E[]) new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);

        data = newData;
    }

    @Override
    public boolean add(E e) {
        if (size == capacity)
            ensureCapacity();

        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index >= 0 && index < size) {
            E deleteVal = data[index];
            System.arraycopy(data, index + 1, data, index, size - index - 1);
            data[size--] = null;
            return deleteVal;
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        if (size == capacity)
            ensureCapacity();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;
        for(int i = 0; i < size && !found; i++) {
            if (o.equals(data[i])) {
                remove(i);
                found = true;
            }
        }
        return found;
    }

    @Override
    public E set(int index, E element) {
        E removed = data[index];
        data[index] = element;
        return removed;
    }


    @Override
    public boolean isEmpty() {
        return (size == 0);
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            data[i] = null;

        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null)
            for (int i = 0; i < size; i++)
                if (o.equals(data[i]))
                    return i;
        return -1;
    }

    @Override
    public E get(int index) {
        if (index >= 0 && index < size)
            return data[index];
        return null;
    }

    @Override
    public boolean contains(Object o) {
        if (o != null)
            for (int i = 0; i < size; i++)
                if (o.equals(data[i]))
                    return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null)
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(data[i]))
                    return i;
        return -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object obj : c)
            if (!contains(obj))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for(E obj : c)
            add(obj);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for(E obj : c)
            add(index++, obj);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for(int i = 0; i < size; i++)
            if (c.contains(data[i])) {
                remove(i);
                i--;
                changed = true;
            }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for(int i = 0; i < size; i++)
            if (!c.contains(data[i]))
            {
                remove(i);
                i--;
                changed = true;
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
        if (toIndex < fromIndex || toIndex > size || fromIndex < 0)
            return null;

        int len = toIndex - fromIndex;
        List<E> subData = new ListC<>(len);
        for(int i = fromIndex; i < toIndex; i++)
            subData.add(data[i]);
        return subData;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            private int cursor = index;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E next() {
                lastRet = cursor;
                return data[cursor++];
            }

            @Override
            public E previous() {
                lastRet = cursor;
                return data[cursor--];
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
                if (lastRet < 0)
                    throw new IllegalStateException();
                ListC.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0)
                    throw new IllegalStateException();
                ListC.this.set(lastRet, e);
            }

            @Override
            public void add(E e) {
                ListC.this.add(cursor, e);
                cursor++;
                lastRet = -1; // после add нельзя делать remove/set
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(data, 0, arr, 0, size);
        return arr;
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
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                lastRet = cursor;
                return data[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0)
                    throw new IllegalStateException();
                ListC.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            }
        };
    }

}
