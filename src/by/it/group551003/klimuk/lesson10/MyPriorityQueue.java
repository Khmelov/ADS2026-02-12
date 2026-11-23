package by.it.group551003.klimuk.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] elements;
    private int size;

    public MyPriorityQueue() {
        this.elements = (E[]) new Comparable[11];
        this.size = 0;
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
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException("Element cannot be null");
        if (size >= elements.length) {
            grow();
        }
        int i = size;
        size++;
        if (i == 0) {
            elements[0] = element;
        } else {
            siftUp(i, element);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = elements[0];
        E x = elements[s];
        elements[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(E element) {
        return contains((Object) element);
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
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                elements[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                elements[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, elements[i]);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity < 64 ? oldCapacity + 2 : oldCapacity + (oldCapacity >> 1);
        E[] newArray = (E[]) new Comparable[newCapacity];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = elements[parent];
            if (x.compareTo(e) >= 0) {
                break;
            }
            elements[k] = e;
            k = parent;
        }
        elements[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = elements[child];
            int right = child + 1;
            if (right < size && c.compareTo(elements[right]) > 0) {
                child = right;
                c = elements[child];
            }
            if (x.compareTo(c) <= 0) {
                break;
            }
            elements[k] = c;
            k = child;
        }
        elements[k] = x;
    }

    private E removeAt(int i) {
        int s = --size;
        if (s == i) {
            elements[i] = null;
        } else {
            E moved = elements[s];
            elements[s] = null;
            siftDown(i, moved);
            if (elements[i] == moved) {
                siftUp(i, moved);
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
