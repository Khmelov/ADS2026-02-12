package by.it.group551001.romanov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[16];
        size = 0;
    }

    // Вспомогательный метод для увеличения массива
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    // Бинарный поиск.
    // Если элемент найден, возвращает его индекс (>= 0).
    // Если нет, возвращает отрицательное число: -(точка вставки) - 1.
    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> k = (Comparable<? super E>) key;

        while (low <= high) {
            int mid = (low + high) / 2;
            Comparable<? super E> midVal = (Comparable<? super E>) elements[mid];
            int cmp = midVal.compareTo((E) k);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1); // Элемент не найден, возвращаем позицию для вставки
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return binarySearch(o) >= 0; // Если индекс >= 0, значит элемент есть
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Nulls are not allowed");

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // Элемент уже существует (множество не хранит дубликаты)
        }

        // Вычисляем реальную позицию для вставки
        int insertIndex = -(index + 1);

        if (size == elements.length) {
            grow();
        }

        // Сдвигаем элементы вправо, освобождая место для нового
        for (int i = size; i > insertIndex; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем элемент
        elements[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int index = binarySearch(o);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево, затирая удаляемый элемент
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null; // Очищаем ссылку для сборщика мусора
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        // Метод двух указателей: перезаписываем массив "на лету"
        // без дополнительных сдвигов для каждого элемента
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Очищаем оставшиеся хвосты
            for (int i = newSize; i < size; i++) {
                elements[i] = null;
            }
            size = newSize;
        }

        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

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

    /////////////////////////////////////////////////////////////////////////
    //////    Методы-заглушки (требуются интерфейсом, но не заданием) ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}