package by.it.group551003.kalach.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap;          // бинарная мин-куча: родитель всегда <= потомков
    private int size = 0;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[11];
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[11];
        this.comparator = comparator;
    }

    // ---------- вспомогательные методы ----------

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private static boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = heap[i];
        heap = bigger;
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E p = heap[parent];
            if (compare(x, p) >= 0) break;
            heap[k] = p;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = 2 * k + 1;
            E c = heap[child];
            int right = child + 1;
            if (right < size && compare(c, heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (compare(x, c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
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
            if (heap[i] == moved) siftUp(i, moved);
        }
    }

    // общий метод для removeAll / retainAll
    private boolean bulkRemove(Collection<?> c, boolean removeIfContained) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            E e = heap[i];
            if (c.contains(e) != removeIfContained) {
                heap[j++] = e;
            }
        }
        boolean changed = j != size;
        for (int i = j; i < size; i++) heap[i] = null;
        size = j;
        if (changed) heapify();
        return changed;
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
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
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (same(element, heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        if (size >= heap.length) grow();
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        int n = --size;
        E x = heap[n];
        heap[n] = null;
        if (n > 0) siftDown(0, x);
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
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkRemove(c, false);
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (same(o, heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
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
                if (i >= size) throw new NoSuchElementException();
                return heap[i++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) result[i] = heap[i];
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) heap[i];
        if (a.length > size) a[size] = null;
        return a;
    }
}
