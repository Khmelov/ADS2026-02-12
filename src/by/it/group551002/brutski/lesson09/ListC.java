package by.it.group551002.brutski.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int size = 0;
    private Object[] elems = new Object[15];

    private void realloc_elems(int min_size) {
        double k = 1.5;
        if (min_size > elems.length) {
            int newCapacity = elems.length == 0 ? 15 : (int) (elems.length * k);
            if (newCapacity < min_size) {
                newCapacity = min_size;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elems, 0, newElements, 0, size);
            elems = newElements;
        }
    }

    private void check_ind(int ind, boolean non_strict) {
        if (non_strict) {
            if (ind < 0 || ind > size) {
                throw new IndexOutOfBoundsException();
            }
        }
        else {
            if (ind < 0 || ind >= size) {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                str.append(", ");
            }
            Object e = elems[i];
            str.append(e == this ? "(this Collection)" : String.valueOf(e));
        }
        str.append("]");

        return str.toString();
    }

    @Override
    public boolean add(E e) {
        realloc_elems(size + 1);
        elems[size++] = e;
        return true;
    }

    @Override
    public E remove(int ind) {
        check_ind(ind, false);
        E removed = (E) elems[ind];
        System.arraycopy(elems, ind + 1, elems, ind, size - ind - 1);
        elems[size--] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        check_ind(index, true);
        realloc_elems(size + 1);
        System.arraycopy(elems, index, elems, index + 1, size - index);
        elems[index] = element;
        ++size;
    }

    @Override
    public boolean remove(Object o) {
        int ind = indexOf(o);
        if (ind != -1) {
            remove(ind);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        check_ind(index, false);
        E old_elem = (E) elems[index];
        elems[index] = element;
        return old_elem;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; ++i)
            elems[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; ++i) {
            if (elems[i] == null && o == null) {
                return i;
            }
            if (elems[i] != null && elems[i].equals(o))
                return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        check_ind(index, false);
        return (E) elems[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i > -1; --i) {
            if (elems[i] == null && o == null) {
                return i;
            } else if (elems[i] != null && elems[i].equals(o))
                return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        int j = 0;
        for (Object obj: c) {
            if (!contains(obj))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty())
            return false;

        for (E el: c) {
            add(el);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty())
            return false;

        int last_ind = index;
        for (E el: c) {
            add(last_ind++, el);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty())
            return false;

        boolean is_changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elems[i])) {
                remove(elems[i]);
                is_changed = true;
            }
            else
                ++i;
        }
        return is_changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean is_changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elems[i])) {
                remove(i);
                is_changed = true;
            }
            else
                ++i;
        }
        return is_changed;
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
