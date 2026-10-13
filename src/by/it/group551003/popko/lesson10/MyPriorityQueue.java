package by.it.group551003.popko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
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
        E value = poll();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        int i = size;
        size = i + 1;
        if (i == 0) {
            elements[0] = element;
        } else {
            siftUp(i, element);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elements[0];
        int last = --size;
        E moved = elements[last];
        elements[last] = null;
        if (last != 0) {
            siftDown(0, moved);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        boolean changed = false;
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            E e = elements[r];
            if (!c.contains(e)) {
                elements[w++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        size = w;
        if (changed) {
            heapify();
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            E e = elements[r];
            if (c.contains(e)) {
                elements[w++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) {
            elements[i] = null;
        }
        size = w;
        if (changed) {
            heapify();
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[i++];
            }
        };
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
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                result[i] = (T) elements[i];
            }
            return result;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Queue)) return false;
        Queue<?> other = (Queue<?>) o;
        if (other.size() != size) return false;
        Iterator<?> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object a = it1.next();
            Object b = it2.next();
            if (a == null ? b != null : !a.equals(b)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < size; i++) {
            hash = 31 * hash + (elements[i] == null ? 0 : elements[i].hashCode());
        }
        return hash;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2 + 1];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = elements[parent];
            if (((Comparable<? super E>) x).compareTo(e) >= 0) {
                break;
            }
            elements[k] = e;
            k = parent;
        }
        elements[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = elements[child];
            int right = child + 1;
            if (right < size &&
                    ((Comparable<? super E>) c).compareTo(elements[right]) > 0) {
                child = right;
                c = elements[child];
            }
            if (((Comparable<? super E>) x).compareTo(c) <= 0) {
                break;
            }
            elements[k] = c;
            k = child;
        }
        elements[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, elements[i]);
        }
    }

    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    private void removeAt(int index) {
        int last = --size;
        E moved = elements[last];
        elements[last] = null;
        if (index != last) {
            elements[index] = moved;
            siftDown(index, moved);
            siftUp(index, elements[index]);
        }
    }
}