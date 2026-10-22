package by.it.group551004.sharkevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 495;
    private E[] elements;
    private int size;

    public MyPriorityQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void ensureCapasity() {
        if  (size == elements.length) {
            int newCapacity = size * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(elements[i], elements[parent]) < 0) {
                E temp = elements[i];
                elements[i] = elements[parent];
                elements[parent] = temp;
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < size && compare(elements[left], elements[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && compare(elements[right], elements[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == i) {
                break;
            }
            E temp = elements[i];
            elements[i] = elements[smallest];
            elements[smallest] = temp;
            i = smallest;
        }
    }

    private void helpify() {
        for (int i = size / 2 - 1; i > -1; i--) {
            siftDown(i);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapasity();
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    public boolean offer(E element) {
        return add(element);
    }

    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    public E peek() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

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

    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NoSuchElementException();
        }
        boolean isChanged = false;
        for (E e : c) {
            add(e);
            isChanged = true;
        }
        return isChanged;
    }

    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[w++] = elements[i];
            }
        }
        if (w == size) {
            return false;
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        size = w;
        helpify();
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[w++] = elements[i];
            }
        }
        if (w == size) {
            return false;
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        size = w;
        helpify();
        return true;
    }
}
