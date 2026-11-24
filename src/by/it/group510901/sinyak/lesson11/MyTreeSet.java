package by.it.group510901.sinyak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size;

    public MyTreeSet() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2 + 1];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private int findIndex(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
            return -1;
        }
        Comparable<? super E> key = (Comparable<? super E>) o;
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = elements[mid];
            int cmp;
            if (midVal == null) {
                cmp = 1; 
            } else {
                cmp = key.compareTo(midVal);
            }
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // Key found
            }
        }
        return -(low + 1); // Key not found
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return true;
                }
            }
            return false;
        }
        Comparable<? super E> key = (Comparable<? super E>) o;
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = elements[mid];
            int cmp;
            if (midVal == null) {
                cmp = 1;
            } else {
                cmp = key.compareTo(midVal);
            }
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return false;
                }
            }
            if (size == elements.length) {
                grow();
            }
            elements[size++] = null;
            return true;
        }
        Comparable<? super E> key = (Comparable<? super E>) e;
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = elements[mid];
            int cmp;
            if (midVal == null) {
                cmp = 1;
            } else {
                cmp = key.compareTo(midVal);
            }
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return false; // already exists
            }
        }
        // Insert at low
        if (size == elements.length) {
            grow();
        }
        System.arraycopy(elements, low, elements, low + 1, size - low);
        elements[low] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                    elements[--size] = null;
                    return true;
                }
            }
            return false;
        }
        Comparable<? super E> key = (Comparable<? super E>) o;
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = elements[mid];
            int cmp;
            if (midVal == null) {
                cmp = 1;
            } else {
                cmp = key.compareTo(midVal);
            }
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                // found at mid, remove it
                System.arraycopy(elements, mid + 1, elements, mid, size - mid - 1);
                elements[--size] = null;
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
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        System.arraycopy(elements, 0, res, 0, size);
        return res;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
