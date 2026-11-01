package by.it.group510901.petsevich.lesson11;

import java.util.*;

/*
toString()
size()
clear()
isEmpty()
add(Object)
remove(Object)
contains(Object)

containsAll(Collection)
addAll(Collection)
removeAll(Collection)
retainAll(Collection)
 */

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Object[] objects;
    private int size;

    MyTreeSet() {
        objects = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    private int binarySearch(Object o) {
        int left = 0, right = size-1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if (((Comparable<? super E>) o).compareTo((E) objects[mid]) > 0)
                left = mid + 1;
            else if (((Comparable<? super E>) o).compareTo((E) objects[mid]) < 0)
                right = mid - 1;
            else return mid;
        }
        return -(left + 1);
    }


    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            return false;

        int idx = binarySearch(e);
        if(idx >= 0)
            return false;

        idx = -(idx + 1);
        if(size == objects.length)
            resize();

        for(int i = size; i > idx; i--)
            objects[i] = objects[i-1];
        objects[idx] = e;
        size++;
        return true;
    }

    private void resize() {
        int newCap = 2 * objects.length;
        Object[] newObjects = new Object[newCap];
        for(int i = 0; i < objects.length; i++)
            newObjects[i] = objects[i];
        objects = newObjects;
    }

    @Override
    public boolean remove(Object o) {
        if(o == null)
            return false;

        int idx = binarySearch(o);
        if(idx < 0)
            return false;

        for(int i = idx; i < size-1; i++)
            objects[i] = objects[i+1];
        size--;
        objects[size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(var o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isChanged = false;
        for(var o : c) {
            if(add(o))
                isChanged = true;
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        int newSize = 0;
        for(int i = 0; i < size; i++) {
            if(c.contains(objects[i]))
                objects[newSize++] = objects[i];
            else isChanged = true;
        }
        for(int i = newSize; i < size; i++)
            objects[i] = null;
        size = newSize;
        return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for(var o : c) {
            if(remove(o))
                isChanged = true;
        }
        return isChanged;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            objects[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(int i = 0; i < size; i++) {
            builder.append(objects[i]);
            if(i != size-1)
                builder.append(", ");
        }
        builder.append(']');
        return builder.toString();
    }
}
