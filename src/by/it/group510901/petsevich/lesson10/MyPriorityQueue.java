package by.it.group510901.petsevich.lesson10;

import javax.swing.text.StyledEditorKit;
import java.io.Console;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/*
toString()
size()
clear()
add(E element)
remove()
contains(E element)

offer(E element)
poll()
peek()
element()
isEmpty()

containsAll(Collection<E> c)
addAll(Collection<E> c)
removeAll(Collection<E> c)
retainAll(Collection<E> c)
*/

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] objects;
    private int size;

    MyPriorityQueue() {
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
    public boolean add(E e) {
        if(size == objects.length)
            ensureCapacity();

        objects[size] = e;
        siftUp(e, size);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object e : c) {
            if(!contains(e))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isChanged = false;
        for(Object e : c) {
            if (add((E) e))
                isChanged = true;
        }
        return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Object[] newArray = new Object[objects.length];
        int it = 0;
        boolean isChanged = false;
        for(int i = 0; i < size; i++) {
            if(!c.contains(objects[i])) {
                newArray[it] = objects[i];
                it++;
                continue;
            }
            isChanged = true;
        }

        if(!isChanged)
            return false;

        size = it;
        objects = newArray;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Object[] newArray = new Object[objects.length];
        int it = 0;
        boolean isChanged = false;
        for(int i = 0; i < size; i++) {
            if(c.contains(objects[i])) {
                newArray[it] = objects[i];
                it++;
                continue;
            }
            isChanged = true;
        }

        if(!isChanged)
            return false;

        size = it;
        objects = newArray;
        heapify();
        return true;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            objects[i] = null;
        size = 0;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E remove() {
        if(size == 0)
            throw new NoSuchElementException();

        E e = (E)objects[0];
        objects[0] = objects[size-1];
        siftDown(0);
        size--;
        return e;
    }

    @Override
    public E poll() {
        if(size == 0)
            return null;
        return remove();
    }

    @Override
    public E element() {
        if(isEmpty())
            throw new NoSuchElementException();
        return peek();
    }

    @Override
    public E peek() {
        if(size == 0)
            return null;

        return (E)objects[0];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if(o.equals(objects[i]))
                return true;
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(int i = 0; i < size; i++) {
            builder.append(objects[i]);
            if(i != size - 1) builder.append(", ");
        }
        builder.append(']');
        return builder.toString();
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

    private void ensureCapacity() {
        int capacity = objects.length;
        int newCapacity = 2 * capacity;
        Object[] bigger = new Object[newCapacity];
        for(int i = 0; i < size; i++)
            bigger[i] = objects[i];
        objects = bigger;
    }

    private void heapify() {
        for(int i = size/2-1; i >= 0; i--)
            siftDown(i);
    }

    private void siftDown(int i) {
        while (i < size / 2) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;
            if(right < size && ((Comparable<E>)objects[left]).compareTo((E)objects[right]) > 0)
                smallest = right;
            if(((Comparable<E>)objects[smallest]).compareTo((E)objects[i]) > 0)
                break;

            Object tmp = objects[i];
            objects[i] = objects[smallest];
            objects[smallest] = tmp;
            i = smallest;
        }
    }

    private void siftUp(E e, int i) {
        int j = (i-1) / 2;
        while(j >= 0) {
            Comparable<E> parent = (Comparable<E>)objects[j];
            if(parent.compareTo(e) > 0) {
                objects[i] = objects[j];
                objects[j] = e;
                i = j;
                j = (i-1) / 2;
                continue;
            }
            break;
        }
    }

}
