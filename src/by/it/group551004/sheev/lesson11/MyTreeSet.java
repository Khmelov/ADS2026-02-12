package by.it.group551004.sheev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    // Начальная ёмкость внутреннего массива
    private static final int DEFAULT_CAPACITY = 32;

    // Отсортированный массив — хранит элементы в порядке возрастания.
    // Именно сортированность даёт логарифмический бинарный поиск.
    private Object[] elements;
    private int size;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        // Обнуляем только занятые ячейки, чтобы GC мог освободить ссылки
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean add(E e) {
        // Ищем позицию: если >= 0 — элемент уже есть, дубликат не добавляем
        int index = binarySearch(e);
        if (index >= 0) {
            return false;
        }
        // Отрицательный результат кодирует точку вставки: -(lo + 1)
        int pos = -(index + 1);
        ensureCapacity(size + 1);
        // Сдвигаем хвост массива вправо на одну позицию — O(n)
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[pos] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false; // элемента нет
        }
        // Сдвигаем хвост влево, затирая удаляемую ячейку — O(n)
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // обнуляем последнюю, чтобы не держать ссылку
        return true;
    }

    public boolean contains(Object o) {
        // Бинарный поиск в отсортированном массиве — O(log n)
        return binarySearch(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    public String toString() {
        // Благодаря сортированности массив уже выводится по возрастанию
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean containsAll(Collection<?> c) {
        // Проверяем, что каждый элемент присутствует
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы, отслеживая, изменилось ли множество
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        // Идём по массиву; при удалении НЕ увеличиваем i,
        // потому что на место удалённого сдвинулся следующий элемент
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elements[i])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        // Аналогично, но удаляем те, которых НЕТ в c
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    // Классический бинарный поиск в отсортированном массиве.
    // Возвращает индекс, если найден, иначе -(lo + 1), где lo — точка вставки.
    // Приём с -(lo + 1) позволяет отличить "не найдено" от индекса 0.
    private int binarySearch(Object key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1; // беззнаковый сдвиг — безопаснее (lo+hi)/2
            // Приводим key к Comparable: элементы должны быть сравнимы
            int cmp = ((Comparable<Object>) key).compareTo(elements[mid]);
            if (cmp == 0) {
                return mid;
            }
            if (cmp < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -(lo + 1);
    }

    // Удаление по индексу со сдвигом хвоста влево — O(n)
    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    // Расширение массива вдвое (или до нужного размера, если больше)
    private void ensureCapacity(int needed) {
        if (needed <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        if (newCapacity < needed) {
            newCapacity = needed;
        }
        Object[] newArr = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }
}