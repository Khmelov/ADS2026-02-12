package by.it.group510902.klimantovich.lesson01.lesson09;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
public class ListC<E> implements List<E> {
    private Object[] elements; // внутренний массив для хранения объектов списка C
    private int size; // счетчик элементов
    public ListC() {
        this.elements = new Object[10]; // сразу выделяю память под 10 стартовых ячеек
        this.size = 0; // изначально список пуст
    }
    private void grow() {
        Object[] newElements = new Object[elements.length * 2]; // удваиваю размер при заполнении
        System.arraycopy(elements, 0, newElements, 0, elements.length); // копирую старые ссылки
        elements = newElements; // сохраняю новый массив
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // собираю элементы в красивую строку
            if (i < size - 1) {
                sb.append(", "); // разделяю через запятую
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow(); // расширяю массив если места не осталось
        }
        elements[size] = e; // пишу новый объект в конец
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        E removedElement = (E) elements[index]; // запоминаю объект для возврата
        int numMoved = size - index - 1; // считаю сколько элементов надо сдвинуть влево
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved); // сдвигаю элементы
        }
        size--;
        elements[size] = null; // зануляю последнюю ячейку для сборщика мусора
        return removedElement;
    }
    @Override
    public int size() {
        return size; // возвращаю текущее заполнение
    }
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка границ
        }
        if (size == elements.length) {
            grow(); // увеличиваю емкость если нужно
        }
        System.arraycopy(elements, index, elements, index + 1, size - index); // сдвигаю элементы вправо
        elements[index] = element; // вставляю новый объект
        size++;
    }
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o); // ищу индекс объекта в массиве
        if (index >= 0) {
            remove(index); // если нашла удаляю по индексу
            return true;
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка границ
        }
        E oldElement = (E) elements[index]; // сохраняю старое значение ячейки
        elements[index] = element; // записываю новое значение
        return oldElement;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю пустой ли список
    }
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // зануляю все ссылки в массиве
        }
        size = 0; // сбрасываю счетчик
    }
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i; // поиск null элементов
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i; // сравнение обычных объектов через equals
            }
        }
        return -1;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        return (E) elements[index]; // отдаю элемент с приведением типа
    }
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // проверяю наличие объекта через indexOf
    }
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i; // ищу null с конца массива
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i; // поиск объекта через equals с конца
            }
        }
        return -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного объекта нет в списке то false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true; // добавляю поочередно элементы из коллекции c в конец
        }
        return modified;
    }
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        boolean modified = false;
        for (E e : c) {
            add(index++, e); // вставляю элементы коллекции по индексу со сдвигом остальных
            modified = true;
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            while (remove(e)) {
                modified = true; // полностью вычищаю все вхождения элементов коллекции c
            }
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i); // удаляю те элементы которых нет в коллекции c
                modified = true;
            }
        }
        return modified;
    }
    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override
    public ListIterator<E> listIterator(int index) { return null; }
    @Override
    public ListIterator<E> listIterator() { return null; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size); // возвращаю чистый массив точной длины проекта
        return result;
    }
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0; // курсор для итератора
            @Override
            public boolean hasNext() {
                return cursor < size; // проверяю есть ли следующий элемент
            }
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) elements[cursor++]; // выдаю элемент и двигаю курсор итератора вперед
            }
        };
    }
}
