package by.it.group551002.brutski.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {
    int start_capacity = 16;
    private E[] array;
    int size = 0;
    double k = 1.5;

    public MyPriorityQueue() {
        array = (E[]) new Object[start_capacity];
    }

    private void reallocate(int min_size) {
        if (min_size > array.length) {
            int newCapacity = (int) (array.length * k);
            E[] newElements = (E[]) new Object[newCapacity];
            System.arraycopy(array, 0, newElements, 0, size);
            array = newElements;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private int siftDown(int i) { //просеивание вниз
        int left = 2 * i + 1, right = 2 * i + 2;
        int size = this.size, min = i;

        if (left < size && compare(array[left], array[min]) < 0)
            min = left;
        if (right < size && compare(array[right], array[min]) < 0)
            min = right;
        if (min == i)
            return i;

        E temp = array[min];
        array[min] = array[i];
        array[i] = temp;

        return siftDown(min);
    }

    private int siftUp(int i) { //просеивание вверх
        while (i > 0 && compare(array[(i - 1) / 2], array[i]) > 0) {
            int swap = (i - 1) / 2;
            E temp = array[swap];
            array[swap] = array[i];
            array[i] = temp;

            i = swap;
        }
        return i;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size; ++i) {
            if (i > 0) {
                str.append(", ");
            }
            E e = array[i];
            str.append(e == this ? "(this Collection)" : String.valueOf(e));
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; ++i) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();

        reallocate(size + 1);
        array[size] = e;
        siftUp(size++);

        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            return false;

        for (int i = 0; i < size; i++) {
            if (compare(array[i], (E) o) == 0)
                return true;
        }
        return false;
    }

    @Override
    public E remove() {
        if (this.isEmpty())
            throw new NoSuchElementException("PriorityQueue is empty");

        E result = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;

        if (!this.isEmpty())
            siftDown(0);

        return result;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (this.isEmpty())
            return null;

        E result = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;

        if (!this.isEmpty())
            siftDown(0);

        return result;
    }

    @Override
    public E peek() {
        if (this.isEmpty())
            return null;

        return array[0];
    }

    @Override
    public E element() {
        if (this.isEmpty())
            throw new NoSuchElementException("PriorityQueue is empty");

        return array[0];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        for (Object o: c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }

        if (c.isEmpty())
            return false;

        for (E o: c) {
            add(o);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) {
                array[j++] = array[i];
            }
        }
        boolean changed = j != size;
        for (int i = j; i < size; i++) {
            array[i] = null;
        }
        size = j;
        if (changed) {
            heapify();
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                array[j++] = array[i];
            }
        }
        boolean changed = j != size;
        for (int i = j; i < size; i++) {
            array[i] = null;
        }
        size = j;
        if (changed) {
            heapify();
        }
        return changed;
    }
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////

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
