package by.it.group551003.kakhno.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements = new Object[10];
    private int size = 0;

    // Вспомогательный метод для увеличения размера массива при необходимости
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// ///               Обязательные к реализации методы             ///////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {  //формирует строку
        StringBuilder sb = new StringBuilder("[");  //накапливаем элементы в стринг билдер
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");  // вставляет ,
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {  //добавляет один элемент в коне на позицию size
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {  //удаляет элемент по индексу
        if (index < 0 || index >= size) {
            return null;
        }
        E removedElement = (E) elements[index];  //запоминаем знач
        for (int i = index; i < size - 1; i++) {  //циклом сдвигаем влево
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {  //вставляем на конкретную позицию
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(size + 1);   //готовим место
        for (int i = size; i > index; i--) {  //сдвигает вправо на 1 шаг
            elements[i] = elements[i - 1];
        }
        elements[index] = element;  //вставляет
        size++;
    }

    @Override
    public boolean remove(Object o) {  //удаляет первое вхождение
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {  //перезаписывате элемент по индексу
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
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
    public int indexOf(Object o) {  //поиск первой позиции
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {  //поиск последней позиции
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {   //проверяет содержание коллекции с в списке
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {    //добавляет элементы коллекции с в список в конец
        if (c.isEmpty()) {
            return false;
        }
        ensureCapacity(size + c.size());
        for (E item : c) {
            elements[size++] = item;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {  //вставляет коллекцию с начиная с конкретного индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (c.isEmpty()) {
            return false;
        }
        int numNew = c.size();
        ensureCapacity(size + numNew);

        // Сдвигаем элементы вправо, освобождая место для вставляемых элементов
        for (int i = size - 1; i >= index; i--) {
            elements[i + numNew] = elements[i];
        }

        // Вставляем новые элементы
        for (E item : c) {
            elements[index++] = item;
        }
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {  //удаляет элементы из списка, содержащие элементы коллекции с
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем индекс, так как элементы сдвинулись влево
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {   //оставляет только те элементы что принадлежат с
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);

                i--; // Уменьшаем индекс, так как элементы сдвинулись влево
                modified = true;
            }
        }
        return modified;
    }


    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// ///               Опциональные к реализации методы             ///////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
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

    @Override
    public Object[] toArray() {
        return new Object[0];
    }


    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// /////        Эти методы имплементировать необязательно    ////////////
    /// /////        но они будут нужны для корректной отладки    ////////////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
