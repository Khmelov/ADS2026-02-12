package by.it.group510901.krot.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    // Внутреннее хранилище элементов и фактический размер списка
    private Object[] elements;
    private int size;

    // Конструктор по умолчанию с начальной емкостью 10
    public ListC() {
        this.elements = new Object[10];
        this.size = 0;
    }

    // Вспомогательный метод: расширение емкости массива при нехватке места
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            // Ручной перенос существующих элементов в новый массив
            Object[] newArray = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }

    // Вспомогательный метод: проверка границ индекса [0, size - 1] для доступа/удаления
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    // Вспомогательный метод: проверка границ индекса [0, size] для вставки
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // 1. Формирует строковое представление списка: "[item1, item2, ...]"
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // 2. Добавляет один элемент в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size] = e;
        size++;
        return true;
    }

    // 3. Удаляет элемент по указанному индексу со сдвигом последующих элементов влево
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // очищаем ссылку для сборщика мусора
        size--;

        return removedElement;
    }

    // 4. Возвращает размер списка
    @Override
    public int size() {
        return size;
    }

    // Вставляет элемент по индексу, сдвигая элементы от этой позиции вправо
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Удаляет первое совпадение с переданным объектом (с учетом null)
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    // Заменяет элемент на позиции index и возвращает старое значение
    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // Возвращает true, если список не содержит элементов
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает весь список
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Находит индекс первого вхождения объекта в список
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Возвращает элемент по индексу
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    // Проверяет, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Находит индекс последнего вхождения объекта (поиск справа налево)
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Проверяет, входят ли ВСЕ элементы коллекции c в наш список
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    //  Добавляет все элементы переданной коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        ensureCapacity(size + c.size());
        for (E item : c) {
            elements[size++] = item;
        }
        return true;
    }

    // Вставляет все элементы переданной коллекции начиная с индекса index
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) {
            return false;
        }
        int numNew = c.size();
        ensureCapacity(size + numNew);

        // Раздвигаем существующие элементы вправо на numNew позиций
        for (int i = size - 1; i >= index; i--) {
            elements[i + numNew] = elements[i];
        }

        // Заполняем освободившийся промежуток новыми элементами
        int cur = index;
        for (E item : c) {
            elements[cur++] = item;
        }
        size += numNew;
        return true;
    }

    // 17. Удаляет из списка все элементы, которые присутствуют в коллекции
    // Реализовано алгоритмом двух указателей (за 1 проход без лишних сдвигов)
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        // Зануляем остаток массива для сборщика мусора
        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    // 18. Оставляет в списке ТОЛЬКО те элементы, которые присутствуют в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            } else {
                modified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            elements[i] = null;
        }
        size = writeIndex;
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает подсписок от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListC<E> sub = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(get(i));
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Преобразует список в обычный массив Object[]
    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        return copy;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы нужны для корректной отладки / for-each  /////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Реализация итератора для цикла for-each
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return (E) elements[cursor++];
            }
        };
    }
}