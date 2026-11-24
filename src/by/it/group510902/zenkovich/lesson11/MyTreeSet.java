package by.it.group510902.zenkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements;
    private int size;

    public MyTreeSet() {
        this.elements = new Object[10];
        this.size = 0;
    }

    private int compare(E a, E b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private int binarySearch(E e) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, e);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        elements = new Object[10];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = binarySearch(e);


        if (index >= 0) {
            return false;
        }

        ensureCapacity();


        int insertPos = -index - 1;


        System.arraycopy(elements, insertPos, elements, insertPos + 1, size - insertPos);

        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        try {
            E e = (E) o;
            int index = binarySearch(e);

            if (index >= 0) {
                int numMoved = size - index - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, index + 1, elements, index, numMoved);
                }
                elements[--size] = null;
                return true;
            }
        } catch (ClassCastException ignored) {
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        try {
            E e = (E) o;
            return binarySearch(e) >= 0;
        } catch (ClassCastException ignored) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
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
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                temp[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = temp;
            size = newSize;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                temp[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = temp;
            size = newSize;
        }
        return modified;
    }


    private class MyIterator implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) {
                throw new IndexOutOfBoundsException();
            }
            lastRet = cursor;
            return (E) elements[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            MyTreeSet.this.remove(elements[lastRet]);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
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
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}