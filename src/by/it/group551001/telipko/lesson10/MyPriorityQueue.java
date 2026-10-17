package by.it.group551001.telipko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    public static void main(String[] args) {
        MyPriorityQueue<Integer> q = new MyPriorityQueue<>();
        q.add(5);
        q.add(1);
        q.add(3);
        q.add(2);
        System.out.println(q);
        System.out.println(q.poll());
        System.out.println(q);
    }

    private Object[] heap = new Object[11];
    private int size;


    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    private void grow() {
        Object[] bigger = new Object[heap.length * 2];
        System.arraycopy(heap, 0, bigger, 0, size);
        heap = bigger;
    }

    private void siftUp(int k, Object x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object p = heap[parent];
            if (compare(x, p) >= 0) {
                break;
            }
            heap[k] = p;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, Object x, int n) {
        int half = n >>> 1;
        while (k < half) {
            int child = 2 * k + 1;
            Object c = heap[child];
            int right = child + 1;
            if (right < n && compare(c, heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (compare(x, c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i], size);
        }
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
        } else {
            Object moved = heap[s];
            heap[s] = null;
            siftDown(i, moved, size);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
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


    private boolean bulkRemove(Collection<?> c, boolean removeIfContained) {
        int end = size;
        boolean[] dead = new boolean[end];
        boolean any = false;
        for (int i = 0; i < end; i++) {
            if (c.contains(heap[i]) == removeIfContained) {
                dead[i] = true;
                any = true;
            }
        }
        if (!any) {
            return false;
        }
        int w = 0;
        for (int i = 0; i < end; i++) {
            if (!dead[i]) {
                heap[w++] = heap[i];
            }
        }
        for (int i = w; i < end; i++) {
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
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
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
            heap[i] = null;
        }
        size = 0;
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
    public boolean add(E e) {
        return offer(e);
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


    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        return size == 0 ? null : (E) heap[0];
    }

    @Override
    public E element() {
        E e = peek();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = (E) heap[0];
        int n = --size;
        Object last = heap[n];
        heap[n] = null;
        if (n > 0) {
            siftDown(0, last, n);
        }
        return result;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

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
    public boolean contains(Object o) {
        return indexOf(o) != -1;
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

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return (E) heap[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @SuppressWarnings("unchecked")
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
}
