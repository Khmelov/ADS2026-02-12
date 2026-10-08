package by.it.group551002.puteev.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;

    private static final int INITIAL_CAPACITY = 16;

    public MyPriorityQueue() {
        elements = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity << 1;
        E[] newElements = (E[]) new Comparable[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Просеивание вверх (Sift-Up) для сохранения свойств кучи при добавлении
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = elements[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            elements[k] = e;
            k = parent;
        }
        elements[k] = (E) key;
    }

    // Просеивание вниз (Sift-Down) для сохранения свойств кучи при удалении
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1; // Листья начинаются с половины массива
        while (k < half) {
            int child = (k << 1) + 1; // Левый потомок
            E c = elements[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo(elements[right]) > 0) {
                child = right;
                c = elements[child];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            elements[k] = c;
            k = child;
        }
        elements[k] = (E) key;
    }

    // Построение кучи за O(n) по алгоритму Floyd's Heap Construction
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, elements[i]);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size >= elements.length) {
            grow();
        }
        size++;
        siftUp(size - 1, element);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
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
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
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

    // O(n) удаление элементов: фильтрация за за 1 проход и восстановление кучи с помощью heapify()
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        boolean modified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    // O(n) сохранение элементов: фильтрация за 1 проход и восстановление кучи с помощью heapify()
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        boolean modified = false;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = writeIndex; i < size; i++) {
                elements[i] = null;
            }
            size = writeIndex;
            heapify();
        }
        return modified;
    }

    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}