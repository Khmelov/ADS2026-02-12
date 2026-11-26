package by.it.group551002.kubarko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements = new Object[10];
    private int size;

    private int search(Object value) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int comparison = compare(value, elements[middle]);

            if (comparison == 0) {
                return middle;
            }
            if (comparison < 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        return -left - 1;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<Object>) first).compareTo(second);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] expanded = new Object[elements.length * 2];
            System.arraycopy(elements, 0, expanded, 0, size);
            elements = expanded;
        }
    }

    private void removeAt(int index) {
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elements, index + 1, elements, index, moved);
        }
        elements[--size] = null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(elements[i]);
        }
        return result.append("]").toString();
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
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        compare(value, value);

        int index = search(value);
        if (index >= 0) {
            return false;
        }

        index = -index - 1;
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        int index = search(value);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return search(value) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object value : collection) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;
        for (E value : collection) {
            if (add(value)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection == this) {
            boolean changed = !isEmpty();
            clear();
            return changed;
        }

        boolean changed = false;
        for (Object value : collection) {
            if (remove(value)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (collection == this) {
            return false;
        }

        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!collection.contains(elements[i])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
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
    public <T> T[] toArray(T[] array) {
        throw new UnsupportedOperationException();
    }
}
