package by.it.group551001.artobolevskiy.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    E[] elements = (E[]) new Object[10];
    int size = 0;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            s.append(elements[i]);
            if (i < size - 1) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
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
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null) {
                if (elements[i] == null) {
                    return true;
                }
            } else {
                if (element.equals(elements[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (size == elements.length) {
            E[] NewElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, NewElements, 0, size);
            elements = NewElements;
        }

        elements[size] = element;
        siftUp(size);
        size++;

        return true;
    }

    void siftUp(int index) {
        E target = elements[index];
        Comparable<? super E> comp = (Comparable<? super E>) target;

        while (index > 0) {
            int ParentIdx = (index - 1) >>> 1; //
            E parent = elements[ParentIdx];

            if (comp.compareTo(parent) >= 0) {
                break;
            }

            elements[index] = parent;
            index = ParentIdx;
        }
        elements[index] = target;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        int lastIndex = size - 1;
        E result = elements[0];
        E lastElement = elements[lastIndex];
        elements[lastIndex] = null;
        size--;

        if (size > 0) {
            elements[0] = lastElement;
            siftDown(0);
        }

        return result;
    }

    void siftDown(int index) {
        int half = size >>> 1; // до size / 2
        E target = elements[index];
        Comparable<? super E> comp = (Comparable<? super E>) target;

        while (index < half) {
            int leftChildIndex = (index << 1) + 1; // левый потомок: index * 2 + 1
            int rightChildIndex = leftChildIndex + 1; // правый потомок: index * 2 + 2

            E child = elements[leftChildIndex];
            int childIndex = leftChildIndex;

            if (rightChildIndex < size && ((Comparable<? super E>) child).compareTo(elements[rightChildIndex]) > 0) {
                childIndex = rightChildIndex;
                child = elements[rightChildIndex];
            }

            if (comp.compareTo(child) <= 0) {
                break;
            }

            elements[index] = child;
            index = childIndex;
        }
        elements[index] = target;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
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
        for (E element : c) {
            add(element);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        E[] KeepElements = (E[]) new Object[elements.length];
        int KeepSize = 0;

        // фильтруем массив, сохраняя исходный порядок элементов
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                modified = true;
            } else {
                KeepElements[KeepSize++] = elements[i];
            }
        }

        if (modified) {
            elements = KeepElements;
            size = KeepSize;
            MakeAHip();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        E[] KeepElements = (E[]) new Object[elements.length];
        int KeepSize = 0;

        // фильтруем массив, сохраняя исходный порядок элементов
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                modified = true;
            } else {
                KeepElements[KeepSize++] = elements[i];
            }
        }

        if (modified) {
            elements = KeepElements;
            size = KeepSize;
            MakeAHip(); // восстанавливаем свойства кучи
        }
        return modified;
    }

    void MakeAHip() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean remove(Object o) { return false; }
}
