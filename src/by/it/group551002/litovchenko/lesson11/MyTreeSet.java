package by.it.group551002.litovchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private int capacity = 16;
    private int size = 0;
    private Object[] elements = new Object[capacity];

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        Object[] newElements = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    @SuppressWarnings("unchecked")
    private int binSearch(Object element) {
        if (element == null) return -1;
        int left = 0;
        int right = size - 1;

        @SuppressWarnings("rawtypes")
        Comparable key = (Comparable) element;

        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = key.compareTo(elements[mid]);

            if (cmp > 0) {
                left = mid + 1;
            } else if (cmp < 0) {
                right = mid - 1;
            } else {
                return mid; // элемент найден
            }
        }
        return -(left + 1); // точка вставки, если элемента нет
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
    public boolean contains(Object o) {
        if (o == null || size == 0) return false;
        int index = binSearch(o);
        return index >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) return false;

        if (size == 0) {
            elements[0] = e;
            size++;
            return true;
        }

        int index = binSearch(e);
        if (index >= 0) {
            return false; // дубликаты в Set запрещены
        }

        int insertIndex = -index - 1;

        if (size >= capacity) {
            resize();
        }

        // Сдвигаем элементы вправо для сохранения сортировки
        for (int i = size; i > insertIndex; i--) {
            elements[i] = elements[i - 1];
        }

        elements[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || size == 0) return false;
        int index = binSearch(o);
        if (index < 0) {
            return false; // элемента нет
        }

        // Сдвигаем элементы влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
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
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
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

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // Обязательные методы интерфейса Set (заглушки/базовая реализация, чтобы компилировалось)
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) elements[currentIndex++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = elements[i];
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}