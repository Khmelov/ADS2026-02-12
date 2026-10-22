package by.it.group551002.puteev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size = 0;

    private static final int INITIAL_CAPACITY = 16;

    public MyTreeSet() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
    }

    private void grow() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Бинарный поиск элемента или индекса для вставки в отсортированном массиве
    private int binarySearch(Object o) {
        int low = 0;
        int high = size - 1;
        Comparable<Object> key = (Comparable<Object>) o;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);

            if (cmp > 0) {
                low = mid + 1;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                return mid; // Элемент найден
            }
        }
        return -(low + 1); // Элемент не найден: возвращаем (-(point) - 1)
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        int index = binarySearch(e);
        if (index >= 0) {
            return false; // Элемент уже существует (дубликаты запрещены)
        }

        int insertionPoint = -(index + 1);
        if (size >= elements.length) {
            grow();
        }

        // Сдвигаем элементы вправо для сохранения порядка сортировки
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int index = binarySearch(o);
        if (index < 0) {
            return false; // Элемент не найден
        }

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return binarySearch(o) >= 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
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
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                  Заглушки интерфейса Set                   ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}