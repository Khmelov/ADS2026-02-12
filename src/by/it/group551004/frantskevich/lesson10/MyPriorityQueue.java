package by.it.group551004.frantskevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    // min-куча: потомки элемента i лежат в 2i+1 и 2i+2
    private E[] heap = (E[]) new Object[10];
    private int size = 0;

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            E e = heap[parent];
            if (compare(x, e) >= 0) {
                break;
            }
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size / 2;
        while (k < half) {
            int child = 2 * k + 1;
            int right = child + 1;
            if (right < size && compare(heap[child], heap[right]) > 0) {
                child = right;
            }
            if (compare(x, heap[child]) <= 0) {
                break;
            }
            heap[k] = heap[child];
            k = child;
        }
        heap[k] = x;
    }

    // восстановление кучи из произвольного массива за O(n)
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        size--;
        if (size == i) {
            heap[i] = null;
        } else {
            E moved = heap[size];
            heap[size] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    // удаляет все элементы, для которых contains(c) == remove, за один проход + heapify
    private boolean bulkRemove(Collection<?> c, boolean remove) {
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) != remove) {
                heap[w++] = heap[i];
            }
        }
        if (w == size) {
            return false;
        }
        for (int i = w; i < size; i++) {
            heap[i] = null;
        }
        size = w;
        heapify();
        return true;
    }

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
        sb.append("]");
        return sb.toString();
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
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (size == heap.length) {
            E[] heap_new = (E[]) new Object[heap.length * 2];
            for (int i = 0; i < size; i++) {
                heap_new[i] = heap[i];
            }
            heap = heap_new;
        }
        size++;
        siftUp(size - 1, e);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        E x = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        removeAt(i);
        return true;
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
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) heap[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (i >= size) {
                    throw new NoSuchElementException();
                }
                return heap[i++];
            }
        };
    }
}
