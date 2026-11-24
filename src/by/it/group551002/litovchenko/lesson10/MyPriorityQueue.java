package by.it.group551002.litovchenko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;
    private E[] array;
    private int size;

    public MyPriorityQueue() {
        this.array = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            initialCapacity = DEFAULT_CAPACITY;
        }
        this.array = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    // =========================================================================
    // Вспомогательные методы кучи (Min-Heap)
    // =========================================================================

    private void ensureCapacity() {
        if (size >= array.length) {
            int newCapacity = array.length + (array.length >> 1); // +50%
            E[] newArray = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
    }

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = array[parent];
            if (compare(x, e) >= 0) {
                break;
            }
            array[k] = e;
            k = parent;
        }
        array[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size >>> 1; // Узлы с индексами >= half являются листьями
        while (k < half) {
            int child = (k << 1) + 1; // Левый ребенок
            E c = array[child];
            int right = child + 1;

            if (right < size && compare(c, array[right]) > 0) {
                child = right;
                c = array[child];
            }

            if (compare(x, c) <= 0) {
                break;
            }

            array[k] = c;
            k = child;
        }
        array[k] = x;
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            array[i] = null;
        } else {
            E moved = array[s];
            array[s] = null;
            siftDown(i, moved);
            if (array[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    // =========================================================================
    // Обязательные методы интерфейса Queue
    // =========================================================================

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("PriorityQueue does not permit null elements");
        }
        ensureCapacity();
        int i = size++;
        if (i == 0) {
            array[0] = element;
        } else {
            siftUp(i, element);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = array[0];
        E x = array[s];
        array[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    @Override
    public E peek() {
        return (size == 0) ? null : array[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
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
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            }
        }
        return modified;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, array[i]);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                array[i] = null;
                modified = true;
            } else {
                array[newSize++] = array[i];
            }
        }
        if (modified) {
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                array[newSize++] = array[i];
            } else {
                array[i] = null;
                modified = true;
            }
        }
        if (modified) {
            size = newSize;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = array[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                result[i] = (T) array[i];
            }
            return result;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) array[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}