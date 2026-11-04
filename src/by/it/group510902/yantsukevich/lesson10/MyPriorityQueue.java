package by.it.group510902.yantsukevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] elements;
    private int size;

    public MyPriorityQueue() {
        elements = (E[]) new Comparable[10];
    }

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            result += elements[i];

            if (i < size - 1) {
                result += ", ";
            }
        }

        return result + "]";
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
    public boolean add(E element) {
        if (size == elements.length) {
            resize();
        }

        elements[size] = element;
        size++;

        siftUp(size - 1);

        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        E result = elements[0];

        size--;

        elements[0] = elements[size];
        elements[size] = null;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        return remove();
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }

        return elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;

        for (E element : c) {
            add(element);
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize] = elements[i];
                newSize++;
            }
        }

        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }

        size = newSize;

        heapify();

        return oldSize != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int oldSize = size;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize] = elements[i];
                newSize++;
            }
        }

        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }

        size = newSize;

        heapify();

        return oldSize != size;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (elements[index].compareTo(elements[parent]) >= 0) {
                break;
            }

            E temp = elements[index];
            elements[index] = elements[parent];
            elements[parent] = temp;

            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left >= size) {
                break;
            }

            int smallest = left;

            if (right < size &&
                    elements[right].compareTo(elements[left]) < 0) {
                smallest = right;
            }

            if (elements[index].compareTo(elements[smallest]) <= 0) {
                break;
            }

            E temp = elements[index];
            elements[index] = elements[smallest];
            elements[smallest] = temp;

            index = smallest;
        }
    }

    private void resize() {
        E[] newElements = (E[]) new Comparable[elements.length * 2];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    private E removeAt(int index) {
        E result = elements[index];

        int lastIndex = --size;

        if (lastIndex == index) {
            elements[index] = null;
            return result;
        }

        E moved = elements[lastIndex];
        elements[lastIndex] = null;
        elements[index] = moved;

        siftDown(index);

        if (elements[index] == moved) {
            siftUp(index);
        }

        return result;
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
    public boolean remove(Object o) {
        return false;
    }




}
