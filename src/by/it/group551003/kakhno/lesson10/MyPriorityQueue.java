package by.it.group551003.kakhno.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void resize() {
        E[] newHeap = (E[]) new Object[heap.length * 2];

        for (int i = 0; i < size; i++)
            newHeap[i] = heap[i];

        heap = newHeap;
    }

    private int compare(E first, E second) {
        return ((Comparable<E>) first).compareTo(second);
    }

    private void siftUp(int index) {
        int parent;
        E element;

        element = heap[index];

        while (index > 0) {
            parent = (index - 1) / 2;

            if (compare(element, heap[parent]) >= 0)
                break;

            heap[index] = heap[parent];
            index = parent;
        }

        heap[index] = element;
    }

    private void siftDown(int index) {
        int left;
        int right;
        int smallest;
        E element;

        element = heap[index];

        while (true) {
            left = index * 2 + 1;
            right = index * 2 + 2;

            if (left >= size)
                break;

            smallest = left;

            if (right < size &&
                    compare(heap[right], heap[left]) < 0)
                smallest = right;

            if (compare(element, heap[smallest]) <= 0)
                break;

            heap[index] = heap[smallest];
            index = smallest;
        }

        heap[index] = element;
    }

    private void heapify() {
        int i;

        i = size / 2 - 1;

        while (i >= 0) {
            siftDown(i);
            i--;
        }
    }

    private void removeAt(int index) {
        int lastIndex;
        E element;

        lastIndex = size - 1;
        element = heap[lastIndex];

        heap[lastIndex] = null;
        size--;

        if (index < size) {
            heap[index] = element;

            if (index > 0 &&
                    compare(heap[index], heap[(index - 1) / 2]) < 0) {
                siftUp(index);
            } else {
                siftDown(index);
            }
        }
    }

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            if (i > 0)
                result += ", ";

            result += heap[i];
        }

        result += "]";

        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            heap[i] = null;

        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();

        if (size == heap.length)
            resize();

        heap[size] = element;
        size++;

        siftUp(size - 1);

        return true;
    }

    @Override
    public E remove() {
        E element;

        if (size == 0)
            throw new java.util.NoSuchElementException();

        element = heap[0];

        size--;

        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null;

            siftDown(0);
        } else {
            heap[0] = null;
        }

        return element;
    }

    @Override
    public boolean contains(Object element) {
        int i;

        for (i = 0; i < size; i++)
            if (element == null && heap[i] == null ||
                    element != null && element.equals(heap[i]))
                return true;

        return false;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        if (size == 0)
            return null;

        return remove();
    }

    @Override
    public E peek() {
        if (size == 0)
            return null;

        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection)
            if (!contains(element))
                return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;

        for (E element : collection) {
            add(element);
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        int oldSize;
        int newSize;
        int j;

        oldSize = size;
        j = 0;

        for (int i = 0; i < oldSize; i++) {
            if (!collection.contains(heap[i])) {
                heap[j] = heap[i];
                j++;
            } else {
                changed = true;
            }
        }

        newSize = j;

        for (int i = newSize; i < oldSize; i++)
            heap[i] = null;

        size = newSize;

        if (changed)
            heapify();

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;
        int oldSize;
        int newSize;
        int j;

        oldSize = size;
        j = 0;

        for (int i = 0; i < oldSize; i++) {
            if (collection.contains(heap[i])) {
                heap[j] = heap[i];
                j++;
            } else {
                changed = true;
            }
        }

        newSize = j;

        for (int i = newSize; i < oldSize; i++)
            heap[i] = null;

        size = newSize;

        if (changed)
            heapify();

        return changed;
    }

    @Override
    public boolean remove(Object element) {
        for (int i = 0; i < size; i++)
            if (element == null && heap[i] == null ||
                    element != null && element.equals(heap[i])) {

                removeAt(i);

                return true;
            }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                return heap[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];

        for (int i = 0; i < size; i++)
            result[i] = heap[i];

        return result;
    }

    @Override
    public <T> T[] toArray(T[] result) {
        for (int i = 0; i < size; i++)
            result[i] = (T) heap[i];

        return result;
    }
}