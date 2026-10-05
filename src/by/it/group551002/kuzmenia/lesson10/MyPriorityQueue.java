package by.it.group551002.kuzmenia.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] heap = (E[]) new Object[16];
    private int size = 0;

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = heap[i];
        }
        heap = bigger;
    }

    // Просеивание вверх
    private void siftUp(int i, E element) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(element, heap[parent]) >= 0) {
                break;
            }
            heap[i] = heap[parent];
            i = parent;
        }
        heap[i] = element;
    }

    // Просеивание вниз
    private void siftDown(int i, E element) {
        int half = size / 2;
        while (i < half) {
            int child = 2 * i + 1;
            if (child + 1 < size && compare(heap[child], heap[child + 1]) > 0) {
                child++;
            }
            if (compare(element, heap[child]) <= 0) {
                break;
            }
            heap[i] = heap[child];
            i = child;
        }
        heap[i] = element;
    }

    // Выстраивает кучу из произвольно лежащих эл-тов
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    // Оставляет только те элементы для которых (c.contains(e) != removeIfContained)
    private boolean filter(Collection<?> c, boolean removeIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }
        int kept = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) != removeIfContained) {
                heap[kept++] = heap[i];
            }
        }
        boolean changed = kept != size;
        for (int i = kept; i < size; i++) {
            heap[i] = null;
        }
        size = kept;
        if (changed) {
            heapify();
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        if (size != 0) {
            result += heap[0];
        }
        for (int i = 1; i < size; i++) {
            result += ", ";
            result += heap[i];
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
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (size == heap.length) {
            grow();
        }
        siftUp(size, e);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        E last = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0, last);
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
        if (c == this) {
            throw new IllegalArgumentException();
        }
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
        return filter(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, false);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
