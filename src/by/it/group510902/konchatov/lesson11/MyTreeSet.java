package by.it.group510902.konchatov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements; 
    private int size;

    public MyTreeSet() {
        elements = (E[]) new Object[10];
        size = 0;
    }
    private void eq() {
        if (size == elements.length) {
            E[] newArr = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) newArr[i] = elements[i];
            elements = newArr;
        }
    }

    private int binarySearch(Object o) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = ((Comparable<Object>) elements[mid]).compareTo(o);
            if (cmp == 0) {
                return mid;          
            } else if (cmp < 0) {
                left = mid + 1;         
            } else {
                right = mid - 1;      
            }
        }
        return -(left + 1);         
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(Object o) {
        int id= binarySearch(o);
        if (id < 0) return false;     
        for (int i = id; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elements[i])) {
                remove(elements[i]);   
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }
    @Override
    public boolean add(E element) {
        int id = binarySearch(element);
        if (id >= 0) return false;       
        int sdv = -(id + 1); 
        eq();
        for (int i = size; i > sdv; i--) {
            elements[i] = elements[i - 1];
        }
        elements[sdv] = element;
        size++;
        return true;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}