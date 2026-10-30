package by.it.group510902.shimul.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] array = (E[]) new Object[10];
    private int size = 0;

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        array[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0);
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
        if (size == 0) {
            return null;
        }
        return array[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
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
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
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
        boolean modified = false;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) {
                array[j++] = array[i];
            } else {
                modified = true;
            }
        }
        // Очищаем оставшуюся хвостовую часть
        for (int i = j; i < size; i++) {
            array[i] = null;
        }
        if (modified) {
            size = j;
            heapify(); // Восстанавливаем структуру кучи за O(n)
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                array[j++] = array[i];
            } else {
                modified = true;
            }
        }
        // Очищаем оставшуюся хвостовую часть для Garbage Collector
        for (int i = j; i < size; i++) {
            array[i] = null;
        }
        if (modified) {
            size = j;
            heapify(); // Восстанавливаем структуру кучи за O(n)
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return array[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = array[i];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size
            );
            for (int i = 0; i < size; i++) {
                result[i] = (T) array[i];
            }
            return result;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) array[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // --- Вспомогательные приватные методы для кучи ---

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == array.length) {
            E[] newArray = (E[]) new Object[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E key = array[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E parentVal = array[parent];
            if (((Comparable<? super E>) key).compareTo(parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parent;
        }
        array[index] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E key = array[index];
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E childVal = array[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) childVal).compareTo(array[right]) > 0) {
                child = right;
                childVal = array[child];
            }
            if (((Comparable<? super E>) key).compareTo(childVal) <= 0) {
                break;
            }
            array[index] = childVal;
            index = child;
        }
        array[index] = key;
    }

    private void removeAt(int index) {
        if (index == size - 1) {
            array[index] = null;
            size--;
            return;
        }
        E moved = array[size - 1];
        array[size - 1] = null;
        size--;
        array[index] = moved;
        siftDown(index);
        if (array[index] == moved) {
            siftUp(index);
        }
    }
}