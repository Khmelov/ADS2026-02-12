package by.it.group510902.zenkovich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    E[] heap;
    int size;
    int DEFAULT_CAPACITY = 11;

    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }


    int parent(int i) { return (i - 1) >>> 1; }
    int left(int i)   { return 2 * i + 1; }
    int right(int i)  { return 2 * i + 2; }

    void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCap = heap.length + (heap.length >> 1) + 1;
            if (newCap < minCapacity) newCap = minCapacity;
            E[] newHeap = (E[]) new Object[newCap];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    void siftUp(int i) {
        E x = heap[i];
        while (i > 0) {
            int p = parent(i);
            if (((Comparable<? super E>) x).compareTo(heap[p]) >= 0) break;
            heap[i] = heap[p];
            i = p;
        }
        heap[i] = x;
    }

    void siftDown(int i) {
        E x = heap[i];
        int half = size >>> 1;
        while (i < half) {
            int child = left(i);
            int r = child + 1;
            if (r < size && ((Comparable<? super E>) heap[r]).compareTo(heap[child]) < 0) {
                child = r;
            }
            if (((Comparable<? super E>) x).compareTo(heap[child]) <= 0) break;
            heap[i] = heap[child];
            i = child;
        }
        heap[i] = x;
    }

    void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return i;
        }
        return -1;
    }



    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i + 1 < size) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E result = heap[0];
        int last = --size;
        E x = heap[last];
        heap[last] = null;
        if (last != 0) {
            heap[0] = x;
            siftDown(0);
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;

        int last = --size;
        E x = heap[last];
        heap[last] = null;

        if (i != last) {
            heap[i] = x;
            siftDown(i);
            siftUp(i);
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        int last = --size;
        E x = heap[last];
        heap[last] = null;
        if (last != 0) {
            heap[0] = x;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            offer(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (size == 0 || c.isEmpty()) return false;
        boolean modified = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (!c.contains(heap[r])) {
                heap[w++] = heap[r];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) heap[i] = null;
            size = w;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (size == 0) return false;
        boolean modified = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(heap[r])) {
                heap[w++] = heap[r];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) heap[i] = null;
            size = w;
            heapify();
        }
        return modified;
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(heap, 0, arr, 0, size);
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
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
                if (index >= size) throw new NoSuchElementException();
                return heap[index++];
            }
        };
    }
}