package by.it.group510901.krot.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListA<E> implements List<E> {

    // Внутренний массив для хранения данных
    private Object[] elements;
    // Количество фактически добавленных элементов в список
    private int size;

    // Конструктор: выделяет начальную память под 10 элементов
    public ListA() {
        this.elements = new Object[10];
        this.size = 0;
    }

    // Вспомогательный метод: если места в массиве не хватает, увеличиваем его емкость более чем в 2 раза
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            // Вручную переносим элементы в новый, более вместительный массив
            Object[] newArray = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }

    // Вспомогательный метод: проверка попадания индекса в диапазон [0, size - 1] для чтения/удаления
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    // Вспомогательный метод: проверка диапазона [0, size] для операции вставки нового элемента
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

    // Формирует строковое представление списка в виде: "[elem1, elem2, elem3]"
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

    // Добавляет элемент в самый конец списка с расширением массива при необходимости
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size] = e;
        size++;
        return true;
    }

    // Удаляет элемент по указанному индексу, сдвигает оставшиеся элементы влево и возвращает удаленный
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) elements[index];

        // Сдвигаем элементы на один шаг влево, затирая удаляемый
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // зануляем последний элемент для очистки памяти
        size--;

        return removedElement;
    }

    // Возвращает текущее количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Вставляет элемент в произвольную позицию 'index', сдвигая последующие элементы вправо
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);

        // Освобождаем ячейку сдвигом элементов вправо от конца к началу
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Ищет и удаляет первое вхождение переданного объекта
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    // Заменяет элемент по индексу на новый и возвращает старое значение
    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Полностью очищает список (зануляет ссылки для сборщика мусора и сбрасывает счетчик)
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Находит индекс первого вхождения объекта в список. Если объект не найден — возвращает -1
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

    // Возвращает элемент по его индексу
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    // Проверяет, содержится ли переданный объект в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
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

    // Проверяет, содержатся ли все элементы из переданной коллекции в данном списке
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы переданной коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E item : c) {
            add(item);
        }
        return true;
    }

    // Вставляет все элементы переданной коллекции начиная с определенного индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) {
            return false;
        }
        for (E item : c) {
            add(index++, item);
        }
        return true;
    }

    // Удаляет из списка все элементы, которые присутствуют в переданной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // шаг назад, так как элементы сдвинулись влево
                modified = true;
            }
        }
        return modified;
    }

    // Оставляет в списке только те элементы, которые присутствуют в переданной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    // Возвращает новый список, содержащий срез элементов от fromIndex (включительно) до toIndex (исключительно)
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(get(i));
        }
        return sub;
    }

    // Преобразует текущий список в массив Object[]
    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        return copy;
    }

    // Заполняет переданный массив типизированными элементами списка
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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы нужны для обхода списка циклом for-each //////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Реализация простого однонаправленного итератора (для цикла for (E item : list))
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0; // текущая позиция курсора

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

            @Override
            public void remove() {
                if (cursor <= 0) {
                    throw new IllegalStateException();
                }
                ListA.this.remove(--cursor);
            }
        };
    }

    // Создает двунаправленный итератор с начала списка
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    // Создает двунаправленный итератор с указанной позиции
    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);

        return new ListIterator<E>() {
            private int cursor = index;
            private int lastRet = -1; // индекс последнего возвращенного элемента

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (cursor <= 0) throw new NoSuchElementException();
                cursor--;
                lastRet = cursor;
                return (E) elements[cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.set(lastRet, e);
            }

            @Override
            public void add(E e) {
                ListA.this.add(cursor, e);
                cursor++;
                lastRet = -1;
            }
        };
    }
}