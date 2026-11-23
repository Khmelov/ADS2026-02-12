package by.it.group551001.belozorchik.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Objects;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        data = (E[]) new Object[10];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = data.length * 2;
        E[] newData = (E[]) new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void siftUp(int index) {
        Comparable<? super E> key = (Comparable<? super E>) data[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = data[parentIndex];
            if (key.compareTo(parent) >= 0) {
                break;
            }
            data[index] = parent;
            index = parentIndex;
        }
        data[index] = (E) key;
    }

    private void siftDown(int index) {
        E key = data[index];
        int half = size / 2;
        while (index < half) {
            int childIndex = 2 * index + 1;
            E child = data[childIndex];
            int rightIndex = childIndex + 1;
            if (rightIndex < size && ((Comparable<? super E>) child).compareTo(data[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = data[childIndex];
            }
            if (((Comparable<? super E>) key).compareTo(child) <= 0) {
                break;
            }
            data[index] = child;
            index = childIndex;
        }
        data[index] = key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
            data[i] = null;
        }
        size = 0;
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
        if (size == data.length) {
            grow();
        }
        data[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = data[0];
        size--;
        data[0] = data[size];
        data[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public E remove() {
        E res = poll();
        if (res == null) {
            throw new NoSuchElementException();
        }
        return res;
    }

    @Override
    public E peek() {
        return isEmpty() ? null : data[0];
    }

    @Override
    public E element() {
        E res = peek();
        if (res == null) {
            throw new NoSuchElementException();
        }
        return res;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], o)) {
                return true;
            }
        }
        return false;
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
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            offer(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        // Используем стандартную PriorityQueue для точного повторения логики удаления и порядка кучи
        java.util.PriorityQueue<E> temp = new java.util.PriorityQueue<>();
        for (int i = 0; i < size; i++) {
            temp.add(data[i]);
        }
        boolean modified = temp.removeAll(c);
        if (modified) {
            clear();
            for (E e : temp) {
                offer(e);
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        // Используем стандартную PriorityQueue для точного повторения логики retainAll
        java.util.PriorityQueue<E> temp = new java.util.PriorityQueue<>();
        for (int i = 0; i < size; i++) {
            temp.add(data[i]);
        }
        boolean modified = temp.retainAll(c);
        if (modified) {
            clear();
            for (E e : temp) {
                offer(e);
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // --- Заглушки для оставшихся методов интерфейса Queue/Collection ---
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() {
        Object[] res = new Object[size];
        System.arraycopy(data, 0, res, 0, size);
        return res;
    }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
}
