package by.it.group510901.kachur.lesson01.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    // ============== Вспомогательные методы ==============

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCap = heap.length + (heap.length >> 1) + 1;
            if (newCap < minCapacity) newCap = minCapacity;
            if (newCap > Integer.MAX_VALUE - 8) newCap = Integer.MAX_VALUE - 8;
            E[] newHeap = (E[]) new Object[newCap];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    // "siftUp"
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];
            if (compare(x, e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    // "siftDown"
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && compare(c, heap[right]) > 0) {
                c = heap[child = right];
            }
            if (compare(x, c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    //  восстанавливаем кучу снизу вверх
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    // ============== Обязательные методы ==============

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
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
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        int i = size;
        size = i + 1;
        if (i == 0) {
            heap[0] = e;
        } else {
            siftUp(i, e);
        }
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = heap[0];
        E x = heap[s];
        heap[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ============== Коллекционные методы ==============

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    // Точная копия алгоритма java.util.PriorityQueue.removeAll
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) return false;
        int i, n = size;
        for (i = 0; i < n; i++) {
            if (c.contains(heap[i])) break;
        }
        if (i == n) return false;
        for (int j = i + 1; j < n; j++) {
            E element = heap[j];
            if (!c.contains(element)) heap[i++] = element;
        }
        for (int k = i; k < n; k++) heap[k] = null;
        size = i;
        heapify();
        return true;
    }

    // Точная копия алгоритма java.util.PriorityQueue.retainAll
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) return false;
        int i, n = size;
        for (i = 0; i < n; i++) {
            if (!c.contains(heap[i])) break;
        }
        if (i == n) return false;
        for (int j = i + 1; j < n; j++) {
            E element = heap[j];
            if (c.contains(element)) heap[i++] = element;
        }
        for (int k = i; k < n; k++) heap[k] = null;
        size = i;
        heapify();
        return true;
    }

    // ============== Остальное (для компиляции Queue/Collection) ==============

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(heap, 0, arr, 0, size);
        return arr;
    }

    @SuppressWarnings("unchecked")
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
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    private int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) return i;
        }
        return -1;
    }

    private E removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            heap[i] = moved;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return heap[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }
}
