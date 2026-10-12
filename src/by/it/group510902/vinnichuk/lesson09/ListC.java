package by.it.group510902.vinnichuk.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    //внутренний массив
    private Object[] elements;
    //количество элементов
    private int size;
    public ListC(){
        //начальная емкость-10 ячеек
        this.elements=new Object[10];
        this.size=0;
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    //вывод элементов в строку
    public String toString() {
        //начало строки
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            //если не последний элемент, то запятая
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        //если последний
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        //если не хватает размера, то увеличиваем и переносим данные
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 3 / 2 + 1];
            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        //добавляем элемент
        elements[size++] = e;
        return true;
    }

    @Override
    //удаление по элементу
    public E remove(int index) {
            E removedElement = (E) elements[index];
            //проходимся и смещаем справа налево

            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
    //уменьшаем счетчик и обнуляем последний элемент
            size--;
            elements[size] = null;
            //выводим удаленный элемент
            return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    //добавляем по индексу, проходимся и сдвигаем слева направо
    //начинаем с конца
    public void add(int index, E element) {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 3 / 2 + 1];
            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        //где находится элемент
        int index=indexOf(o);
        //проверяем индекс
        if (index >= 0){
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        //заменяем старый элемент на новый
        E oldElement = (E) elements[index];
        elements[index] = element;
        //вывод того, что было до
        return oldElement;
    }


    @Override
    //есть ли что-то
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    //полное удаление и размер =0
    public void clear() {
        for (int i=0; i<size; i++){
            elements[i]=null;
        }
    size=0;
    }

    @Override
    //ищем индекс элемента
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                //сравниваем число
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    //по индексу возвращаем то, что там находится
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        //есть ли элемент
        return indexOf(o) >=0;
    }

    @Override
    //ищем элемент, но справа налево
    public int lastIndexOf(Object o) {
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
    //совпадают ли переданные элементы с теми, которые уже были
    public boolean containsAll(Collection<?> c) {
        // Проходим по каждому элементу переданной коллекции
        for (Object item : c) {
            // Если хотя бы одного элемента у нас нет, возвращаем false
            if (!contains(item)) {
                //если хотя бы одной нет
                return false;
            }
        }
        return true;
    }

    @Override
    //добавляем переданные элементы в конец
    public boolean addAll(Collection<? extends E> c) {
        // если переданная коллекция пустая, то список не изменился
        if (c.isEmpty()) return false;

        // поочередно добавляем каждый элемент коллекции в конец через метод add()
        for (E item : c) {
            add(item);
        }
        return true;
    }

    @Override
    //поочередно добавляем элементы коллекции по индексу, двигая старые элементы
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) return false;

        int currentInsertIndex = index;
        // Поочередно вставляем элементы один за другим, смещая индекс вставки вправо
        for (E item : c) {
            add(currentInsertIndex++, item);
        }
        return true;
    }

    @Override
    //удаление, если совпадает, сравнивая с новой коллекцией
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        // Перебираем все элементы списка с начала до конца
        for (int i = 0; i < size; i++) {
            // Если переданная коллекция содержит текущий элемент, удаляем его
            if (c.contains(elements[i])) {
                remove(i);
                //делаем шаг назад и снова проверяем
                i--;
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    //удаление, если нет, сравнивая с новой коллекцией
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        for (int i = 0; i < size; i++) {
            // Если переданная коллекция не содержит наш текущий элемент, удаляем его
            if (!c.contains(elements[i])) {
                remove(i);
                // Сдвигаем индекс назад, чтобы не перескочить через элемент, сместившийся влево
                i--;
                isModified = true;
            }
        }
        return isModified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
        //создаем новый массив и перезаписываем
        Object[] exactArray = new Object[size];
        for (int i = 0; i < size; i++) {
            exactArray[i] = elements[i];
        }
        return exactArray;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            //дошел ли до конца
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            //если нечего выдавать
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return (E) elements[cursor++];
            }
        };
    }
}
