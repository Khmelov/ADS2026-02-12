package by.it.group551001.belozorchik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    //внутр массив для хранения отсор эл
    private E[] elements;
    private int size = 0;
    private static final int def_capacity = 10;

    public MyTreeSet() {
        elements = (E[]) new Object[def_capacity];
    }

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
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {

        if (o == null || size == 0) {
            return false;
        }
        int low = 0;
        int high = size - 1;
        try {
            Comparable<E> key = (Comparable<E>) o;

            while (low <= high) {
                int mid = (low + high) >>> 1;
                int cmp = key.compareTo(elements[mid]);
                if (cmp < 0) {
                    high = mid - 1; //в левой половине
                } else if (cmp > 0) {
                    low = mid + 1;  //в правой
                } else {
                    return true;    //найден
                }
            }
        } catch (ClassCastException e) {
            //если переданный объект несравним с эл TreeSet
            return false;
        }

        return false; //не найден
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    @Override
    public boolean add(E e) {
        //расширять ли массив
        ensureCapacity();

        //если пустой, кладем в нулевой индекс
        if (size == 0) {
            elements[0] = e;
            size++;
            return true;
        }

        int low = 0;
        int high = size - 1;
        Comparable<E> key = (Comparable<E>) e;

        //ищем позицию для вставки
        while (low <= high) {

            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return false; //эл-повторка
            }
        }
        //указ на точный индекс для вставки
        int index = low;
        //сдвиг вправо
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        //вставка
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {

        if (o == null || size == 0) {
            return false;
        }

        int low = 0;
        int high = size - 1;
        int index = -1;
        //поиск индекса эл
        try {
            Comparable<E> key = (Comparable<E>) o;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                int cmp = key.compareTo(elements[mid]);

                if (cmp < 0) {
                    high = mid - 1;
                } else if (cmp > 0) {
                    low = mid + 1;
                } else {
                    index = mid; //найден
                    break;
                }
            }
        } catch (ClassCastException e) {
            return false; //если объект несравним
        }
        //не найден в множестве
        if (index == -1) {
            return false;
        }
        //сдвиг влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            for (int i = 0; i < numMoved; i++) {
                elements[index + i] = elements[index + i + 1];
            }
        }
        //зануляем последний эл
        elements[--size] = null;
        return true;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i] == this ? "(this Collection)" : elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        boolean modified = false;
        //временный массив для сохранения нужных элементов
        E[] newElements = (E[]) new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newElements[newSize++] = elements[i]; //оставляем эл
            } else {
                modified = true; //эл удалю
            }
        }

        if (modified) {
            elements = newElements;
            size = newSize;
        }
        return modified;
    }

    //заглушки для остальных методов
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
