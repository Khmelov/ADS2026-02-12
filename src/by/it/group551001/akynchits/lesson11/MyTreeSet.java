package by.it.group551001.akynchits.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

// Ограничиваем E типами, которые можно сравнивать между собой
public class MyTreeSet<E extends Comparable<? super E>> implements Set<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        // Создаем массив. Используем Comparable[], так как E расширяет Comparable
        this.elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // =====================================================================
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ИЗ ЗАДАНИЯ
    // =====================================================================

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
            elements[i] = null; // Помогаем сборщику мусора
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("MyTreeSet does not permit null elements");
        }

        if (size == elements.length) {
            grow();
        }

        // 1. Находим позицию для вставки с помощью бинарного поиска
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = e.compareTo(elements[mid]);

            if (cmp == 0) {
                return false; // Элемент уже существует, дубликаты запрещены
            }
            if (cmp < 0) {
                right = mid - 1; // Ищем в левой половине
            } else {
                left = mid + 1;  // Ищем в правой половине
            }
        }

        // К этому моменту 'left' содержит индекс, куда нужно вставить элемент,
        // чтобы сохранить порядок сортировки.

        // 2. Сдвигаем элементы вправо, чтобы освободить место
        for (int i = size; i > left; i--) {
            elements[i] = elements[i - 1];
        }

        // 3. Вставляем элемент и увеличиваем размер
        elements[left] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            @SuppressWarnings("unchecked")
            E key = (E) o;
            int index = binarySearch(key);

            if (index != -1) {
                // Сдвигаем элементы влево, заполняя "дыру"
                for (int i = index; i < size - 1; i++) {
                    elements[i] = elements[i + 1];
                }
                elements[size - 1] = null; // Очищаем последнюю ссылку
                size--;
                return true;
            }
        } catch (ClassCastException e) {
            // Если тип объекта несовместим с E, он точно не содержится в множестве
            return false;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            return binarySearch((E) o) != -1;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    // =====================================================================
    // МАССОВЫЕ ОПЕРАЦИИ (Оптимизированные для отсортированного массива)
    // =====================================================================

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;

        // Алгоритм "in-place compaction" (сжатие на месте)
        // Мы переписываем массив, оставляя только те элементы, которых НЕТ в коллекции c.
        // Это автоматически сохраняет порядок сортировки!
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        // Очищаем "хвост" массива для сборщика мусора
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;

        // Оставляем только те элементы, которые ЕСТЬ в коллекции c.
        // Порядок сортировки также сохраняется.
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return modified;
    }

    // =====================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =====================================================================


    private int binarySearch(E key) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = key.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -1;
    }

    // Увеличение массива вручную
    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = elements.length + (elements.length >> 1); // Увеличиваем в 1.5 раза
        E[] newElements = (E[]) new Comparable[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    // =====================================================================
    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ИНТЕРФЕЙСА (для компиляции)
    // =====================================================================

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
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

    private class MyIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return elements[currentIndex++];
        }
    }
}
