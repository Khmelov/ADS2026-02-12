package by.it.group551003.kalvinkovskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {
    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public ListA() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder str = new StringBuilder();
        str.append("[");
        str.append(elements[0]);
        for (int i = 1; i < size; i++) {
            str.append(", ");
            str.append(elements[i]);
        }
        str.append("]");
        return str.toString();
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        int newCapacity = elements.length * 2;
        if (newCapacity == 0) {
            newCapacity = DEFAULT_CAPACITY;
        }
        E[] new_elements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, new_elements, 0, size);
        elements = new_elements;
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            ensureCapacity();
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
        E to_delete_e = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        size--;
        elements[size] = null;
        return to_delete_e;
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
        if (size == elements.length) {
            ensureCapacity();
        }
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = -1;

        for (int i = 0; i < size; i++) {
            boolean match;
            if (o == null)
                match = (elements[i] == null);
            else
                match = o.equals(elements[i]);

            if (match) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
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
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            boolean match;
            if (o == null)
                match = (elements[i] == null);
            else
                match = o.equals(elements[i]);

            if (match) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int last_ind = -1;
        for (int i = size - 1; i >= 0; i--) {
            boolean match;
            if (o == null)
                match = (elements[i] == null);
            else
                match = o.equals(elements[i]);
            if (match) {
                last_ind = i;
                break;
            }
        }
        return last_ind;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        Object[] cArray = c.toArray();
        int to_add_count = cArray.length;
        if (to_add_count == 0)
            return false;

        int new_size = size + to_add_count;
        if (new_size > elements.length) {
            ensureCapacity();
            if (new_size > elements.length) {
                @SuppressWarnings("unchecked")
                E[] new_elements = (E[]) new Object[new_size];
                System.arraycopy(elements, 0, new_elements, 0, size);
                elements = new_elements;
            }
        }

        int num_moved = size - index;
        if (num_moved > 0) {
            System.arraycopy(elements, index, elements, index + to_add_count, num_moved);
        }

        for (int i = 0; i < to_add_count; i++) {
            @SuppressWarnings("unchecked")
            E e = (E) cArray[i];
            elements[index + i] = e;
        }

        size = new_size;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListA<E> subList = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(elements[i]);
        }
        return subList;
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
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListAIterator(0);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListAIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListAIterator(index);
    }

    private class ListAIterator implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;

        public ListAIterator(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastRet = cursor;
            return elements[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new java.util.NoSuchElementException();
            }
            lastRet = cursor - 1;
            cursor = lastRet;
            return elements[lastRet];
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
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListA.this.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListA.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListA.this.add(cursor, e);
            cursor++;
            lastRet = -1;
        }
    }
}