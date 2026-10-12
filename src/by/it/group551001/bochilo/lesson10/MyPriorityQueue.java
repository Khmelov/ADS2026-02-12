package by.it.group551001.bochilo.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    //Создайте аналог очереди с приоритетом на основе кучи БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    @SuppressWarnings("unchecked")
    private E[] heap = (E[]) new Object[11];
    private int size = 0;

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        if (size < heap.length) {
            return;
        }
        E[] newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    private void siftUp(int k, E key) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];
            if (compare(key, e) >= 0) {
                break;
            }
            heap[k] = e;
            k = parent;
        }
        heap[k] = key;
    }

    private void siftDown(int k, E key) {
        int half = size >>> 1;
        while (k < half) {
            int child = 2 * k + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && compare(c, heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (compare(key, c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    // удаление за O(n): уплотняем массив с сохранением порядка, затем строим кучу заново
    private boolean bulkRemove(Collection<?> c, boolean removeIfContains) {
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) != removeIfContains) {
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
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        grow();
        siftUp(size++, element);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        int n = --size;
        E x = heap[n];
        heap[n] = null;
        if (n > 0) {
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
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
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
    @SuppressWarnings("unchecked")
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
}
