package by.it.group551001.belozorchik.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] data; //массив объктов
    private int size;

    public ListC() {
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

    @Override
    public int size() {
        return size;
    }

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

    @Override
    public E set(int index, E element) {
        //нашли
        E oldElement = get(index);
        //замена на новое знач
        data[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        //для каждой ячейки
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

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

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        //приводим к типу Е
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    //поиск последнего вхождения
    @Override
    public int lastIndexOf(Object o) {
        //из конца в начало
        for (int i = size - 1; i >= 0; i--) {
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

    //проверка наличия всех эл в списке
    @Override
    public boolean containsAll(Collection<?> c) {
        //передаем коллекцию эл

        //для каждого обращ к списку
        for (Object item : c) {
            //если хотя бы 1ого не будет -
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    //массовое добавление в конец
    @Override
    public boolean addAll(Collection<? extends E> c) {
        //если пустая
        if (c.isEmpty()) {
            return false;
        }
        //для каждого - add
        for (E item : c) {
            add(item);
        }
        return true;
    }

    //вставка коллекции по индексу
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        //проверка границ индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (c == null || c.isEmpty()) {
            return false;
        }

        //увелич массива под новый объем данных
        while (size + c.size() > data.length) {
            grow();
        }

        //сдвиг врпаво на c.size позиций
        int numMoved = size - index;
        if (numMoved > 0) {
            for (int i = size - 1; i >= index; i--) {
                data[i + c.size()] = data[i];
            }
        }

        //вставка эл из коллекции в образ промежуток
        int i = index;
        for (E item : c) {
            data[i] = item;
            i++;
        }
        //увелич на кол-во добавленных
        size += c.size();
        return true;
    }

    //массовое удаление эл
    @Override
    public boolean removeAll(Collection<?> c) {
        //пустая?
        if (c == null || c.isEmpty() || size == 0) {
            return false;
        }

        //флаг измениней
        boolean modified = false;

        //удаляем с конца тк индексы!!!!
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(data[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    //оставить только нужное те
    // удалить из списка эл кот нет в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        //если коллекция пуста - нафик удалить всё
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        //из конца в начало
        for (int i = size - 1; i >= 0; i--) {
            //если коллекция не содерж - удаляем
            if (!c.contains(data[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
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
