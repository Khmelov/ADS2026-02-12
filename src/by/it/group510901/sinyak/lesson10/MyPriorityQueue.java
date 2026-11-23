package by.it.group510901.sinyak.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] elements;
    private int size;


    public MyPriorityQueue() {
        this.elements = new Object[11];
    }

    public int compare(E a, E b) {
        Comparable<E> comparableA = (Comparable<E>) a;
        return comparableA.compareTo(b);
    }

    public void growEls(Object[] els) {

        int k = 2;
        int newSize = size == 0 ? 11 : size * k;
        Object[] newEls = new Object[newSize];

        for (int i = 0; i <  size; i++) {
            newEls[i] = els[i];
        }

        this.elements = newEls;
    }

    //     Методы    //

    private void siftUp(int k, E el) {

        while (k > 0) {

            int parentIndex = (k-1) / 2;
            E parent = (E) elements[parentIndex];

            if (compare(el, parent) >= 0) break;

            elements[k] = parent;
            k = parentIndex;
        }

        elements[k] = el;
    }

    private void siftDown(int k, E el) {

         while (2 * k + 1 < size) {

            int leftChild = 2 * k + 1;
            int rightChild = 2 * k + 2;

            int priorityChild = leftChild;
            if( rightChild < size && compare((E) elements[leftChild], (E) elements[rightChild]) > 0) {
                priorityChild = rightChild;
            }

            if (compare(el, (E) elements[priorityChild]) <= 0) {
                break;
            }

            elements[k] = elements[priorityChild];
            k = priorityChild;
        }

        elements[k] = el;
    }

    @Override
    public String toString() {
        String result = "[]";

        if (size > 0) {
            result = "[";
            for (int i = 0; i < size - 1; i++) {
                result += elements[i] + ", ";
            }
            result += elements[size - 1] + "]";
        }

        return result;
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

        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return true;
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (compare((E) o, (E) elements[i]) == 0) return true;
            }
        }

        return false;
    }


    @Override
    public boolean add(E e) {

        if(e == null) throw new NullPointerException();

        if(size >= elements.length) { growEls(elements); }

        siftUp(size, e);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {

        if(o == null) throw new NullPointerException();

        for (int i = 0; i < size; i++) {
            if (compare((E) elements[i], (E) o) == 0) {

                elements[i] = elements [size - 1];
                elements[size - 1] = null;
                size--;

                int parentIndx = i / 2;

                if (compare((E) elements[i], (E) elements[parentIndx]) < 0) {
                    siftUp(i, (E) elements[i]);
                }

                if (compare((E) elements[i], (E) elements[parentIndx]) > 0) {
                    siftDown(i, (E) elements[i]);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for(var el : c) {
            if(!contains(el)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        if(c == null) throw new NullPointerException();

        if(c.isEmpty()) return false;

        for (var el : c) {
            add(el);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        if(c == null) throw new NullPointerException();

        if(c.isEmpty()) return false;

        boolean isRemoved = false;

        int newSize = 0;
        for (int x = 0; x < size; x++) {
            if (!c.contains(elements[x])) {
                elements[newSize] = elements[x];
                newSize++;
            }
            else {
                isRemoved = true;
            }
        }


        if (isRemoved)  {
            for(int i = newSize; i < size; i++) {
                elements[i] = null;
            }

            size = newSize;
            heapify();
        }
        else return false;

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        if(c == null) throw new NullPointerException();
        if(c.isEmpty()) {
            clear();
            return true;
        }

        boolean isRetained = false;

        Object[] temp = new Object[size];

        int newSize = 0;
        for (int x = 0; x < size; x++) {
            if (c.contains(elements[x])) {
                temp[newSize] = elements[x];
                newSize++;
            }
            else {
                isRetained = true;
            }
        }

        elements = temp;
        size = newSize;

        heapify();

        if (!isRetained) return false;

        return true;
    }

    @Override
    public void clear() {

        for(int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {

        E result = poll();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E poll() {

        if(size == 0) return null;

        E result = (E) elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;

        if(size > 0) {
            siftDown(0, (E) elements[0]);
        }

        return result;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[0];
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return (E) elements[0];
    }

    private void heapify() {

        for (int i = size / 2 - 1; i >= 0; i-- ) {
            siftDown(i, (E) elements[i]);
        }
    }

    //////////////////////////////////

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
}
