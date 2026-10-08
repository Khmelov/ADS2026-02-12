package by.it.group510902.klimantovich.lesson01.lesson09;
import java.util.*;
public class ListB<E> implements List<E> {
    private Object[] elements; // массив для хранения всех элементов списка B
    private int size; // счетчик реального количества объектов
    public ListB() {
        this.elements = new Object[10]; // начальный размер внутреннего массива равен 10
        this.size = 0; // изначально список пустой
    }
    private void grow() {
        Object[] newElements = new Object[elements.length * 2]; // увеличиваю емкость в два раза
        System.arraycopy(elements, 0, newElements, 0, elements.length); // копирую старые данные
        elements = newElements; // обновляю ссылку на массив
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // собираю элементы через StringBuilder
            if (i < size - 1) {
                sb.append(", "); // разделяю их запятыми
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow(); // расширяю массив если свободные ячейки кончились
        }
        elements[size] = e; // добавляю новый объект в самый конец
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // валидация индекса
        }
        E removedElement = (E) elements[index]; // сохраняю удаляемое значение
        int numMoved = size - index - 1; // считаю количество элементов для сдвига
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved); // сдвигаю элементы влево
        }
        size--;
        elements[size] = null; // очищаю ячейку в конце для сборщика мусора
        return removedElement;
    }
    @Override
    public int size() {
        return size; // возвращаю текущее количество элементов
    }
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка границ
        }
        if (size == elements.length) {
            grow(); // увеличиваю размер массива если он полон
        }
        System.arraycopy(elements, index, elements, index + 1, size - index); // раздвигаю элементы вправо
        elements[index] = element; // вставляю новый элемент на нужную позицию
        size++;
    }
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o); // ищу индекс этого объекта
        if (index >= 0) {
            remove(index); // если нашла то удаляю по индексу
            return true;
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        E oldElement = (E) elements[index]; // запоминаю старый объект
        elements[index] = element; // заменяю его на новый
        return oldElement;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю пустой ли наш список
    }
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // зануляю массив чтобы очистить ссылки
        }
        size = 0;
    }
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i; // поиск null значения
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i; // поиск обычного объекта через equals
            }
        }
        return -1;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка границ
        }
        return (E) elements[index]; // возвращаю элемент с приведением к типу E
    }
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // проверяю наличие объекта через indexOf
    }
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i; // ищу null с конца списка
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i; // ищу объект с конца через equals
            }
        }
        return -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного элемента нет то false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true; // добавляю коллекцию в конец списка
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
            add(index++, e); // добавляю элементы коллекции по индексу со сдвигом
            modified = true;
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            while (remove(e)) {
                modified = true; // удаляю все элементы из коллекции c
            }
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i); // удаляю те которых нет в коллекции c
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
        System.arraycopy(elements, 0, result, 0, size); // возвращаю копию массива точной длины
        return result;
    }
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                return cursor < size; // проверяю есть ли следующий элемент в итераторе
            }
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) elements[cursor++]; // отдаю элемент и двигаю курсор итератора
            }
        };
    }
}
