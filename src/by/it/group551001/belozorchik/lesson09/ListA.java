package by.it.group551001.belozorchik.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] data; //массив объктов
    private int size;

    public ListA() {
        data = new Object[10];
        size = 0;
    }

    //метод расширения массива
    private void grow() {
        //новый массивчик
        Object[] new_data = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            new_data[i] = data[i];
        }
        data = new_data;
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        //если пустой список
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            //записали, разделили запятой
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //добавление в конец
    @Override
    public boolean add(E e) {
        //е - эл кот н добавить
        //проверка на переполненность м
        if (size == data.length) {
            grow();
        }
        data[size] = e;
        size++;
        return true;
    }

    //удаление по индексу
    @Override
    public E remove(int index) {
        //index эл кот н удалить

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E removedElement = (E) data[index];
        //сдвиг эл влево
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i+1];
        }
        //последний эл
        data[size-1] = null;
        size--;
        return removedElement;
    }

    //вернет кол-во добавленных эл
    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    //вставка эл по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        //не полон ли наш массивчик
        if (size == data.length) {
            grow();
        }
        //сдвиг впаво
        for (int i = size - 1; i >= index; i--) {
            data[i+1] = data[i];
        }

        data[index] = element;
        size++;
    }

    //удаление по объекту
    @Override
    public boolean remove(Object o) {
        //ищем
        int index = indexOf(o);
        //если не нашлось
        if (index == -1) {
            return false;
        }

        remove(index);
        return true;
    }

    //замена эл по индексу
    @Override
    public E set(int index, E element) {
        //нашли
        E oldElement = get(index);
        //замена на новое знач
        data[index] = element;
        return oldElement;
    }

    //проверка на пустой список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //очистка спсика
    @Override
    public void clear() {
        //для каждой ячейки
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    //поиск индекса эл
    //-1 - если нет таакого
    @Override
    public int indexOf(Object o) {
        // o - то что ищем

        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (data[i] == null) {
                    return i;
                }
            } else {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    //получить знач эл по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        //приводим к типу Е
        return (E) data[index];
    }

    //проверка на наличие эл
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
