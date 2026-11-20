package by.it.group551002.savitsky.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] data;
    private int size;

    MyTreeSet() {
        data = (E[]) new Object[1000];
        size = 0;
    }

    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int compare = compare(data[mid], (E) o);

            if (compare < 0) {
                low = mid + 1;
            } else if (compare > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -(low + 1);
    }

    @Override
    public String toString() {
        String result = "[";

        for (int i = 0; i < size; i++) {
            result = result + data[i];

            if (i < size - 1) {
                result = result + ", ";
            }
        }

        return result + "]";
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
        Arrays.fill(data, null);
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (size == 0) {
            return false;
        }

        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false;
        }

        int insertPosition = -index - 1;

        if (size == data.length) {
            grow();
        }

        for (int i = size; i > insertPosition; i--) {
            data[i] = data[i - 1];
        }
        data[insertPosition] = e;
        size++;

        return true;
    }

    private void grow() {
        E[] bigger = (E[]) new Object[data.length * 2];

        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
        }

        data = bigger;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false;
        }

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;

        return true;
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
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;

        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, true);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, false);
    }

    private boolean filter(Collection<?> c, boolean keepIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }

        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i]) == keepIfContained) {
                data[newSize] = data[i];
                newSize++;
            }
        }

        boolean changed = newSize != size;

        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }
        size = newSize;

        return changed;
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