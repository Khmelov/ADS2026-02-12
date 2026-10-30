package by.it.group510902.kislyi.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements;
    private int size;
    private final java.util.Comparator<? super E> comparator;

    public MyTreeSet() {
        this(null);
    }

    public MyTreeSet(java.util.Comparator<? super E> comparator) {
        this.elements = new Object[10];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private int indexOf(Object o) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            @SuppressWarnings("unchecked")
            int cmp = compare((E) o, (E) elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -(left + 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int idx = indexOf(e);
        if (idx >= 0) return false;
        int pos = -(idx + 1);
        if (size == elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, pos);
            newArr[pos] = e;
            System.arraycopy(elements, pos, newArr, pos + 1, size - pos);
            elements = newArr;
        } else {
            System.arraycopy(elements, pos, elements, pos + 1, size - pos);
            elements[pos] = e;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx < 0) return false;
        System.arraycopy(elements, idx + 1, elements, idx, size - idx - 1);
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
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
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}