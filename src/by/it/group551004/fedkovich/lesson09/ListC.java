package by.it.group551004.fedkovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    private static final int INITIAL_CAPACITY = 2;
    private Object[] data = new Object[INITIAL_CAPACITY];
    private int cap = INITIAL_CAPACITY;
    private int len = 0;

    private int getNewCapacity(int minCapacity) {
        int newCap = cap + (cap >> 1);

        if (newCap < minCapacity) {
            newCap = minCapacity;
        }

        return newCap;
    }

    private void increaseCapacity(int minCapacity) {
        int newCap = getNewCapacity(minCapacity);
        Object[] newData = new Object[newCap];

        System.arraycopy(data, 0, newData, 0, len);

        cap = newCap;
        data = newData;
    }

    private void increaseCapacityWithGap(int minCapacity, int gapStartIdx, int gapLen) {
        int newCap = getNewCapacity(minCapacity);
        Object[] newData = new Object[newCap];

        System.arraycopy(
                data, 0,
                newData, 0,
                gapStartIdx);

        System.arraycopy(
                data, gapStartIdx,
                newData, gapStartIdx + gapLen,
                len - gapStartIdx);

        cap = newCap;
        data = newData;
    }

    private void shiftLeft(int index, int amount) {
        int elementsToShift = len - index - amount;

        if (elementsToShift > 0) {
            System.arraycopy(
                    data, index + amount,
                    data, index,
                    elementsToShift);
        }

        for (int i = len - amount; i < len; i++) {
            data[i] = null;
        }
    }

    private void shiftRight(int index, int amount) {
        if (len + amount > cap) {
            throw new IllegalStateException("insufficient capacity to shift elements");
        }

        int elementsToShift = len - index;

        if (elementsToShift > 0) {
            System.arraycopy(
                    data, index,
                    data, index + amount,
                    elementsToShift);
        }
    }

    // Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ
    // БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("[");
        if (len > 0) {
            res.append(data[0]);

            for (int i = 1; i < len; ++i) {
                res.append(", ").append(data[i]);
            }
        }
        res.append("]");

        return res.toString();
    }

    @Override
    public boolean add(E e) {
        if (len == cap)
            increaseCapacity(len + 1);

        data[len++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= len)
            throw new IndexOutOfBoundsException(index);

        Object e = data[index];
        shiftLeft(index, 1);
        --len;

        return (E) e;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public void add(int index, E element) {
        final int GAP_LEN = 1;

        if (index < 0 || index > len)
            throw new IndexOutOfBoundsException(index);

        if (len == cap) {
            increaseCapacityWithGap(len + GAP_LEN, index, GAP_LEN);
        } else {
            shiftRight(index, 1);
        }

        data[index] = element;
        ++len;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;

        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= len)
            throw new IndexOutOfBoundsException(index);

        Object e = data[index];
        data[index] = element;

        return (E) e;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < len; ++i) {
            data[i] = null;
        }

        len = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < len; ++i) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= len)
            throw new IndexOutOfBoundsException(index);

        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = len - 1; i >= 0; --i) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int lenAdded = c.size();

        if (lenAdded == 0)
            return false;

        Object[] arr = c.toArray();

        if (len + lenAdded > cap)
            increaseCapacity(len + lenAdded);

        int i = len;
        for (Object o : arr) {
            data[i++] = o;
        }
        len += lenAdded;

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > len)
            throw new IndexOutOfBoundsException(index);

        int lenAdded = c.size();
        if (lenAdded == 0)
            return false;

        Object[] arr = c.toArray();

        if (len + lenAdded > cap) {
            increaseCapacityWithGap(len + lenAdded, index, lenAdded);
        } else {
            shiftRight(index, lenAdded);
        }

        int i = index;
        for (Object o : arr) {
            data[i++] = o;
        }
        len += lenAdded;

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int r = 0;
        int w = 0;

        while (r < len) {
            if (!c.contains(data[r])) {
                data[w] = data[r];
                ++w;
            }
            ++r;
        }

        if (w == len)
            return false;

        for (int i = w; i < len; ++i) {
            data[i] = null;
        }
        len = w;

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int r = 0;
        int w = 0;

        while (r < len) {
            if (c.contains(data[r])) {
                data[w] = data[r];
                ++w;
            }
            ++r;
        }

        if (w == len)
            return false;

        for (int i = w; i < len; ++i) {
            data[i] = null;
        }
        len = w;

        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > len)
            throw new IndexOutOfBoundsException(fromIndex);
        if (toIndex < 0 || toIndex > len)
            throw new IndexOutOfBoundsException(toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("inverted from and to indices");

        int listLen = toIndex - fromIndex;

        ListC<E> list = new ListC<E>();
        list.data = new Object[listLen];
        list.cap = listLen;
        list.len = listLen;

        System.arraycopy(data, fromIndex, list.data, 0, listLen);

        return list;
    }

    private class DataIterator implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;

        public DataIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < len;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new RuntimeException("no such element");

            lastRet = cursor;
            return (E) data[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious())
                throw new RuntimeException("no such element");

            --cursor;
            lastRet = cursor;
            return (E) data[cursor];
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
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            data[lastRet] = e;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > len)
            throw new IndexOutOfBoundsException(index);

        return new DataIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DataIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[len];
        System.arraycopy(data, 0, arr, 0, len);
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
        return new DataIterator(0);
    }
}
