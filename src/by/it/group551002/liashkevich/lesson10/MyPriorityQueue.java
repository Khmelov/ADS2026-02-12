package by.it.group551002.liashkevich.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private int currentCapacity = 16;
    private E[] template = (E[]) new Object[currentCapacity];
    private int length = 0;
    private final double loadFactor = 0.75;

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        E temp = template[i];
        template[i] = template[j];
        template[j] = temp;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(template[i], template[parent]) < 0) {
                this.swap(i, parent);
                i = parent;
            }
            else
                break;
        }
    }

    private void siftDown(int i) {
        int smallest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (leftChild < length && this.compare(template[leftChild], template[smallest]) < 0)
            smallest = leftChild;
        if (rightChild < length && this.compare(template[rightChild], template[smallest]) < 0)
            smallest = rightChild;
        if (smallest != i) {
            this.swap(i, smallest);
            siftDown(smallest);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            result.append(template[i]);
            result.append(", ");
        }
        if (length != 0)
            result.setLength(result.length()-2);
        return result.append("]").toString();
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
        currentCapacity = 16;
        template = (E[]) new Object[currentCapacity];
        length = 0;
    }

    @Override
    public boolean add(E e) {
        if (((double) length / currentCapacity) >= loadFactor) {
            currentCapacity *= 2;
            E[] newTemplate = (E[]) new Object[currentCapacity];
            System.arraycopy(template, 0, newTemplate, 0, length);
            template = newTemplate;
        }
        template[length] = e;
        int i = length;
        ++length;
        this.siftUp(i);
        return true;
    }

    @Override
    public E remove() {
        if (length == 0)
            throw new NoSuchElementException("");
        E toReturn = template[0];
        --length;
        template[0] = template[length];
        template[length] = null;
        int i = 0;
        if (length > 0)
            this.siftDown(i);
        return toReturn;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < length; i++)
            if (o.equals(template[i]))
                return true;
        return false;
    }

    @Override
    public boolean offer(E e) {
        return this.add(e);
    }

    @Override
    public E poll() {
        if (length == 0)
            return null;
        E toReturn = template[0];
        --length;
        template[0] = template[length];
        template[length] = null;
        int i = 0;
        if (length > 0)
            this.siftDown(i);
        return toReturn;
    }

    @Override
    public E peek() {
        return (length != 0) ? template[0] : null;
    }

    @Override
    public E element() {
        if (length == 0)
            throw new NoSuchElementException("");
        return template[0];
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (!this.contains(elem))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E elem : c) {
            this.add(elem);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = length - 1; i >= 0; i--) {
            if (c.contains(template[i])) {
                System.arraycopy(template, i + 1, template, i, length - i - 1);
                template[--length] = null;
                modified = true;
            }
        }
        if (modified) {
            for (int i = length / 2 - 1; i >= 0; i--)
                this.siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = length - 1; i >= 0; i--) {
            if (!c.contains(template[i])) {
                System.arraycopy(template, i + 1, template, i, length - i - 1);
                template[--length] = null;
                modified = true;
            }
        }
        if (modified) {
            for (int i = length / 2 - 1; i >= 0; i--)
                this.siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
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

}
