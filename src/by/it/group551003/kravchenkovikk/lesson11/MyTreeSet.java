package by.it.group551003.kravchenkovikk.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size;

    public MyTreeSet(){
        this.elements = (E[]) new Object[100];
        this.size = 0;
    }

    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private int binarySearch(E e) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (((Comparable<E>) elements[mid]).compareTo(e) < 0) {
                left = mid + 1;
            } else if (((Comparable<E>) elements[mid]).compareTo(e) > 0) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -(left + 1);
    }

    @Override
    public String toString () {
        if (size == 0) {
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            str.append(elements[i]);
            if (i < size - 1) {
                str.append(", ");
            }
        }
        return str.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object e) {
        int index = binarySearch((E)e);
        if (index >= 0) {
            return false;
        }
        if (size == elements.length) {
            grow();
        }
        int insert = -(index + 1);
        for (int i = size; i > insert; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insert] = (E) e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch((E) o);
        if (index < 0) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = binarySearch((E) o);
        if (index < 0) {
            return false;
        }
        return true;
    }

    @Override public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        for (Object obj: c) {
            add(obj);
        }
        return true;
    }

    @Override public boolean retainAll(Collection<?> c) {
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
            }
        }
        return true;
    }

    @Override public boolean removeAll(Collection<?> c) {
        boolean isRemoved = false;
        for (Object obj : c) {
            if (c.contains(obj)) {
                remove(obj);
                isRemoved = true;
            }
        }
        return isRemoved;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
