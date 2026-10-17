package by.it.group551004.zubko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] heap = (E[]) new Object[11];
    private int size = 0;

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCap = heap.length;
        int newCap = oldCap < 64 ? oldCap + 2 : oldCap + (oldCap >> 1);
        E[] newHeap = (E[]) new Object[newCap];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = heap[parent];
            if (key.compareTo((E) e) >= 0) {
                break;
            }
            heap[k] = (E) e;
            k = parent;
        }
        heap[k] = (E) key;
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) heap[right]) > 0) {
                c = heap[child = right];
            }
            if (key.compareTo((E) c) <= 0) {
                break;
            }
            heap[k] = (E) c;
            k = child;
        }
        heap[k] = (E) key;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDownComparable(i, heap[i]);
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

    @SuppressWarnings("unchecked")
    private E removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            siftDownComparable(i, moved);
            if (heap[i] == moved) {
                siftUpComparable(i, moved);
                if (heap[i] != moved) {
                    return moved;
                }
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
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
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size >= heap.length) {
            grow();
        }
        siftUpComparable(size, element);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        int s = --size;
        E moved = heap[s];
        heap[s] = null;
        if (s != 0) {
            siftDownComparable(0, moved);
        }
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int end = size;
        int i;
        for (i = 0; i < end && !c.contains(heap[i]); i++) {
            // skip initial non-matching elements
        }
        if (i >= end) {
            return false;
        }
        int w = i;
        for (; i < end; i++) {
            if (!c.contains(heap[i])) {
                heap[w++] = heap[i];
            }
        }
        for (i = w; i < end; i++) {
            heap[i] = null;
        }
        size = w;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int end = size;
        int i;
        for (i = 0; i < end && c.contains(heap[i]); i++) {
            // skip initial matching elements
        }
        if (i >= end) {
            return false;
        }
        int w = i;
        for (; i < end; i++) {
            if (c.contains(heap[i])) {
                heap[w++] = heap[i];
            }
        }
        for (i = w; i < end; i++) {
            heap[i] = null;
        }
        size = w;
        heapify();
        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса Queue            ///////
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
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        System.arraycopy(heap, 0, a, 0, size);
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
