package by.it.group551001.vinogradov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    @SuppressWarnings("unchecked")
    private E[] elements = (E[]) new Object[10];
    private int size;

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }


    @SuppressWarnings("unchecked")
    private void grow() {
        int newLength = elements.length * 2;
        E[] newElements = (E[]) new Object[newLength];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) s += ", ";
            s += elements[i];
        }
        return s + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int l = 0, r = size;
        while (l < r) {
            int m = l + (r-l)/2;
            if (compare(elements[m], element) == 0) return false;
            else if (compare(elements[m], element) > 0) {
                r = m;
            }
            else l = m + 1;
        }
        if (size == elements.length) grow();
        for (int i = size; i > l; i--) {
            elements[i] = elements[i-1];
        }
        elements[l] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int l = 0, r = size;
        int m = 0;
        boolean f = false;
        while (l < r) {
            m = l + (r-l)/2;
            if (compare(elements[m], (E)element) == 0) {
                f = true;
                break;
            }
            else if (compare(elements[m], (E)element) > 0) {
                r = m;
            }
            else l = m + 1;
        }
        if (!f) return f;
        for (int i = m; i < size-1; i++) {
            elements[i] = elements[i+1];
        }
        size--;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        int l = 0, r = size;
        while (l < r) {
            int m = l + (r-l)/2;
            if (compare(elements[m], (E)element) == 0) return true;
            else if (compare(elements[m], (E) element) > 0) {
                r = m;
            }
            else l = m + 1;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        boolean f = true;
        for (Object el : collection) {
            f &= contains(el);
        }
        return f;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean f = false;
        for (E el : collection) {
            f |= add(el);
        }
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean f = false;
        for (Object el : collection) {
            f |= remove(el);
        }
        return f;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int wr = 0;
        for (int i = 0; i < size; i++) {
            if (collection.contains(elements[i])) {
                elements[wr] = elements[i];
                wr++;
            }
        }
        if (size == wr) return false;
        size = wr;
        return true;

    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }
}
