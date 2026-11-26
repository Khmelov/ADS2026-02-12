package by.it.group551002.kubarko.lesson10;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elementData = (E[]) new Object[16];
    private int size = 0;

    private void siftUp(int idx, E element) {
        Comparable<? super E> key = (Comparable<? super E>) element;
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            E parent = elementData[parentIdx];
            if (key.compareTo(parent) >= 0) {
                break;
            }
            elementData[idx] = parent;
            idx = parentIdx;
        }
        elementData[idx] = element;
    }

    private void siftDown(int idx, E element) {
        Comparable<? super E> key = (Comparable<? super E>) element;
        int half = size / 2;
        while (idx < half) {
            int childIdx = idx * 2 + 1;
            int rightIdx = childIdx + 1;
            E child = elementData[childIdx];
            if (rightIdx < size && ((Comparable<? super E>) child).compareTo(elementData[rightIdx]) > 0) {
                child = elementData[rightIdx];
                childIdx = rightIdx;
            }
            if (key.compareTo(child) <= 0) {
                break;
            }
            elementData[idx] = child;
            idx = childIdx;
        }
        elementData[idx] = element;
    }

    private void grow() {
        int newCapacity = elementData.length * 2;
        E[] temp = (E[]) new Object[newCapacity];
        System.arraycopy(elementData, 0, temp, 0, size);
        elementData = temp;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elementData[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
            elementData[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        if (size >= elementData.length) {
            grow();
        }
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException("Queue is empty");
        E el = elementData[0];
        int lastIndex = size - 1;
        E lastElement = elementData[lastIndex];
        elementData[lastIndex] = null;
        size--;
        if (size > 0) {
            siftDown(0, lastElement);
        }
        return el;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) return false;
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(element)) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        return remove();
    }

    @Override
    public E peek() {
        return (size > 0) ? elementData[0] : null;
    }

    @Override
    public E element() {
        if (size > 0) return elementData[0];
        throw new NoSuchElementException();
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
        if (c.isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (E x : c) {
            if (add(x)) {
                modified = true;
            }
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(elementData[readIndex])) {
                elementData[writeIndex++] = elementData[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elementData[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elementData[readIndex])) {
                elementData[writeIndex++] = elementData[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elementData[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    private void heapify() {
        int half = size / 2;
        for (int i = half - 1; i >= 0; i--) {
            siftDown(i, elementData[i]);
        }
    }

    private void removeAt(int i) {
        int lastIndex = size - 1;
        if (lastIndex == i) {
            elementData[lastIndex] = null;
            size--;
        } else {
            E moved = elementData[lastIndex];
            elementData[lastIndex] = null;
            size--;
            siftDown(i, moved);
            if (elementData[i] == moved) {
                siftUp(i, moved);
            }
        }
    }


    //Boilerplate functions
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
