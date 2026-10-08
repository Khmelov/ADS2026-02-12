package by.it.group551001.vinogradov.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] heap = (E[]) new Object[10];
    private int size = 0;
    

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newLength = heap.length * 2;
        E[] newHeap = (E[]) new Object[newLength];

        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private int compare(E first, E second) {
        return ((Comparable<? super E>) first).compareTo(second);
    }

    private void siftUp(int idx) {
        while(idx > 0) {
            int parent = (idx-1)/2;
            if (compare(heap[idx], heap[parent]) < 0) {
                E temp = heap[idx];
                heap[idx] = heap[parent];
                heap[parent] = temp;
                idx = parent;
            }
            else return;
        }
    }

    private void siftDown(int idx) {
        while (idx*2 + 1 < size) {
            int left = idx*2 + 1;
            int right = idx*2 + 2;

            int sm = left;
            if (right < size && compare(heap[right], heap[left]) < 0) sm = right;
            if (compare(heap[idx], heap[sm]) <= 0) break;

            E temp = heap[idx];
            heap[idx] = heap[sm];
            heap[sm] = temp;
            idx = sm;
        }
    }

    private void rebuildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) s += ", ";
            s += heap[i];
        }
        return s + "]";
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
        if (size == heap.length) grow();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw  new NoSuchElementException();
        E res = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (size == heap.length) grow();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return heap[0];
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
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean modified = false;
        for (E element : collection) {
            if (add(element)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int oldSize = size;
        int wr = 0;
        for (int rd = 0; rd < oldSize; rd++) {
            if (!collection.contains(heap[rd])) {
                heap[wr] = heap[rd];
                wr++;
            }
        }
        for (int i = wr; i < oldSize; i++) {
            heap[i] = null;
        }
        size = wr;
        if (size != oldSize) {
            rebuildHeap();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int oldSize = size;
        int wr = 0;
        for (int rd = 0; rd < oldSize; rd++) {
            if (collection.contains(heap[rd])) {
                heap[wr] = heap[rd];
                wr++;
            }
        }
        for (int i = wr; i < oldSize; i++) {
            heap[i] = null;
        }
        size = wr;
        if (size != oldSize) {
            rebuildHeap();
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object object) {
        throw notImplemented();
    }

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }
}
