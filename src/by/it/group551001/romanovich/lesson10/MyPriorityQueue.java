package by.it.group551001.romanovich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] heap = (E[]) new Object[DEFAULT_CAPACITY];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean contains(Object element) {
        return indexOf(element) >= 0;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == heap.length) {
            grow();
        }
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        int n = --size;
        E last = heap[n];
        heap[n] = null;
        if (n > 0) {
            siftDown(0, last, n);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeIf(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeIf(c, false);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private boolean removeIf(Collection<?> c, boolean removeContained) {
        int kept = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) != removeContained) {
                heap[kept++] = heap[i];
            }
        }
        if (kept == size) {
            return false;
        }
        for (int i = kept; i < size; i++) {
            heap[i] = null;
        }
        size = kept;
        heapify();
        return true;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i], size);
        }
    }

    private void siftUp(int k, E key) {
        Comparable<? super E> comparableKey = (Comparable<? super E>) key;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];
            if (comparableKey.compareTo(e) >= 0) {
                break;
            }
            heap[k] = e;
            k = parent;
        }
        heap[k] = key;
    }

    private void siftDown(int k, E key, int n) {
        Comparable<? super E> comparableKey = (Comparable<? super E>) key;
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < n && ((Comparable<? super E>) c).compareTo(heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (comparableKey.compareTo(c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = key;
    }

    private void grow() {
        E[] newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    private int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return i;
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
            return;
        }
        E moved = heap[s];
        heap[s] = null;
        siftDown(i, moved, size);
        if (heap[i] == moved) {
            siftUp(i, moved);
        }
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) {
            return false;
        }
        removeAt(i);
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return heap[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) heap[i];
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }
}
