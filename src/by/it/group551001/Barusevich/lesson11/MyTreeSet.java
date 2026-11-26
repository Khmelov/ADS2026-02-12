package by.it.group551001.Barusevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Object[] elements;
    private int size;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<Object>) first).compareTo(second);
    }

    private int search(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            int comparison = compare(value, elements[middle]);
            if (comparison == 0) {
                return middle;
            }
            if (comparison < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -(low + 1);
    }

    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }
        Object[] expanded = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            expanded[i] = elements[i];
        }
        elements = expanded;
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
    public boolean contains(Object value) {
        return search(value) >= 0;
    }

    @Override
    public boolean add(E value) {
        if (size == 0 && value != null) {
            compare(value, value);
        }
        int found = search(value);
        if (found >= 0) {
            return false;
        }
        int insertionPoint = -found - 1;
        ensureCapacity();
        for (int i = size; i > insertionPoint; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertionPoint] = value;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object value) {
        int found = search(value);
        if (found < 0) {
            return false;
        }
        for (int i = found; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
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
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (collection.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int nextIndex;
            private E lastReturned;
            private boolean canRemove;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = (E) elements[nextIndex++];
                canRemove = true;
                return lastReturned;
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(lastReturned);
                nextIndex--;
                canRemove = false;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        T[] result = array.length >= size
                ? array
                : (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) elements[i];
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result += ", ";
            }
            result += elements[i];
        }
        return result + "]";
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < size; i++) {
            Object value = elements[i];
            if (value != null) {
                result += value.hashCode();
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Set<?>)) {
            return false;
        }
        Set<?> other = (Set<?>) object;
        if (other.size() != size) {
            return false;
        }
        try {
            return containsAll(other);
        } catch (ClassCastException | NullPointerException exception) {
            return false;
        }
    }
}
