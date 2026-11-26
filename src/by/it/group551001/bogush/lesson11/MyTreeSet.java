package by.it.group551001.bogush.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Множество на отсортированном массиве. Поиск -- бинарный, вставка/удаление -- со сдвигом.
// Порядок -- естественный (E должен быть Comparable), как в java.util.TreeSet.
public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements; // элементы строго по возрастанию, без дубликатов
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // ------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private int compare(E a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    // Бинарный поиск.
    // Нашли -> индекс элемента (>= 0).
    // Не нашли -> -(позиция вставки) - 1 (< 0).
    private int search(Object o) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = compare(elements[mid], o);
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

    @SuppressWarnings("unchecked")
    private void growIfFull() {
        if (size < elements.length) {
            return;
        }
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
    }

    // Общая часть removeAll / retainAll:
    // removeIfContained = true  -> удаляем то, что есть в c
    // removeIfContained = false -> удаляем то, чего нет в c
    // Порядок оставшихся не нарушается, поэтому массив остаётся отсортированным.
    private boolean removeMatching(Collection<?> c, boolean removeIfContained) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            E element = elements[i];
            boolean contained = c.contains(element);
            if (contained != removeIfContained) {
                elements[j] = element;
                j++;
            }
        }
        if (j == size) {
            return false;
        }
        for (int i = j; i < size; i++) {
            elements[i] = null;
        }
        size = j;
        return true;
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

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
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        int index = search(element);
        if (index >= 0) {
            return false; // уже есть
        }
        int position = -(index + 1);
        growIfFull();
        // сдвигаем хвост вправо, освобождая ячейку position
        for (int i = size; i > position; i--) {
            elements[i] = elements[i - 1];
        }
        elements[position] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int index = search(o);
        if (index < 0) {
            return false;
        }
        // сдвигаем хвост влево, затирая элемент index
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return search(o) >= 0;
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
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == this) {
            boolean hadElements = size > 0;
            clear();
            return hadElements;
        }
        return removeMatching(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == this) {
            return false;
        }
        return removeMatching(c, false);
    }

    // ------------------------------------------------------------------
    // Остальное из интерфейса -- не обязательно, заглушки
    // ------------------------------------------------------------------

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