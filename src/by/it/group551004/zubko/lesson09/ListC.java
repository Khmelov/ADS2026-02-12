package by.it.group551004.zubko.lesson09;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private Object[] elements = new Object[10];
    private int size = 0;

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 3 / 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldValue = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return oldValue;
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
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
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
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
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
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) {
            return false;
        }
        ensureCapacity(size + numNew);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elements, index, numNew);
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                modified = true;
            } else {
                elements[writeIndex++] = elements[readIndex];
            }
        }
        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }
        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListC<E> sub = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add((E) elements[i]);
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return new ListIterator<E>() {
            private int cursor = index;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (cursor <= 0) {
                    throw new NoSuchElementException();
                }
                cursor--;
                lastRet = cursor;
                return (E) elements[cursor];
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
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                ListC.this.set(lastRet, e);
            }

            @Override
            public void add(E e) {
                ListC.this.add(cursor, e);
                cursor++;
                lastRet = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(elements, 0, copy, 0, size);
        return copy;
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
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

}
