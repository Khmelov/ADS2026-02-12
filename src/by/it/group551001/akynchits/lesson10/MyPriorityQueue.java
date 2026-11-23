package by.it.group551001.akynchits.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<? super E>> implements java.util.Queue<E> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += heap[i];
            if (i < size - 1) {
                result += ", ";
            }
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
    public boolean add(E element) {
        if (offer(element)) {
            return true;
        }
        throw new IllegalStateException("Queue full");
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
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == heap.length) {
            grow();
        }
        heap[size] = element;
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = (E) heap[0];
        E x = (E) heap[s];
        heap[s] = null;

        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
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
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
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
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        size = newSize;
        if (modified) {
            heapify(); // Перестраиваем кучу
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        size = newSize;
        if (modified) {
            heapify(); // Перестраиваем кучу
        }
        return modified;
    }

    // Метод для полного перестроения кучи (heapify)
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, (E) heap[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = heap.length * 2;
        Object[] newHeap = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            E e = (E) heap[parent];
            if (x.compareTo(e) >= 0) {
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
            int child = (k * 2) + 1;
            E c = (E) heap[child];
            int right = child + 1;

            if (right < size) {
                E rightChild = (E) heap[right];
                if (rightChild.compareTo(c) < 0) {
                    c = rightChild;
                    child = right;
                }
            }

            if (x.compareTo(c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return i;
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        int s = --size;
        if (i == s) {
            heap[s] = null;
        } else {
            E moved = (E) heap[s];
            heap[s] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
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
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) heap[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private class MyIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) heap[currentIndex++];
        }
    }
}