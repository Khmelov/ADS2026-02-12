package by.it.group510902.makaruk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    // Внутренний динамический массив для хранения элементов
    private E[] elements;
    // Количество фактически добавленных уникальных элементов
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод динамического расширения емкости массива
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 3 / 2 + 1;
            E[] newElements = (E[]) new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Классический бинарный поиск для поиска элемента или позиции вставки
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> k = (Comparable<? super E>) key;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = k.compareTo(elements[mid]);

            if (cmp > 0) {
                low = mid + 1;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                return mid; // Элемент найден
            }
        }
        // Если элемент не найден, возвращаем позицию вставки в инвертированном виде
        return -(low + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        // Так как массив постоянно поддерживается отсортированным, просто выводим его подряд
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
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not supported by MyTreeSet");
        }

        int index = binarySearch(e);
        // Если индекс >= 0, значит такой элемент уже есть, а дубликаты в Set запрещены
        if (index >= 0) {
            return false;
        }

        // Переводим отрицательный маркер в реальную позицию вставки
        int insertIndex = -(index + 1);
        ensureCapacity();

        // Освобождаем место для нового элемента, сдвигая правую часть подмассива вправо
        int numMoved = size - insertIndex;
        if (numMoved > 0) {
            System.arraycopy(elements, insertIndex, elements, insertIndex + 1, numMoved);
        }

        elements[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int index = binarySearch(o);
        // Если элемент найден, удаляем его со сдвигом элементов влево
        if (index >= 0) {
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elements, index + 1, elements, index, numMoved);
            }
            elements[--size] = null; // Зануляем освободившийся хвост для сборщика мусора
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Проходим по массиву с конца, чтобы удаление текущего элемента не ломало счетчик цикла
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                // Используем прямое удаление по индексу, чтобы сэкономить на повторном бинарном поиске
                int numMoved = size - i - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, i + 1, elements, i, numMoved);
                }
                elements[--size] = null;
                modified = true;
            }
        }
        return modified;
    }

    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}