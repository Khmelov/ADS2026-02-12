package by.it.group510902.kudan.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    //отсортированный по возрастанию массив, занято первые size ячеек
    private E[] arr;

    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        arr = (E[]) new Object[16];
    }

    //бинарный поиск, если элемент найден возвращает его индекс
    //если нет, возвращает минус место для вставки минус один
    @SuppressWarnings("unchecked")
    private int find(Object o) {
        int lo = 0;
        int hi = size - 1;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            int cmp = ((Comparable<Object>) o).compareTo(arr[mid]);

            if (cmp == 0) {
                return mid;
            }

            if (cmp < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }

        return -(lo + 1);
    }

    @Override
    public String toString() {
        //массив уже отсортирован, просто выводим его по порядку
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            sb.append(arr[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }

        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            arr[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E element) {
        int i = find(element);

        //такой элемент уже есть
        if (i >= 0) {
            return false;
        }

        //превращаем результат поиска в место для вставки
        i = -i - 1;

        //если массив заполнен, делаем вдвое больше
        if (size == arr.length) {
            E[] big = (E[]) new Object[size * 2];
            for (int j = 0; j < size; j++) {
                big[j] = arr[j];
            }
            arr = big;
        }

        //сдвигаем элементы правее места вставки на одну ячейку вправо
        for (int j = size; j > i; j--) {
            arr[j] = arr[j - 1];
        }

        arr[i] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int i = find(element);

        if (i < 0) {
            return false;
        }

        //сдвигаем элементы правее удаляемого на одну ячейку влево
        for (int j = i; j < size - 1; j++) {
            arr[j] = arr[j + 1];
        }

        arr[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        return find(element) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        //достаточно найти один элемент которого нет
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;

        for (E e : c) {
            if (add(e)) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;

        for (Object o : c) {
            if (remove(o)) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        //один проход, нужные элементы сдвигаем в начало массива, порядок сохраняется
        int w = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(arr[i])) {
                arr[w++] = arr[i];
            }
        }

        boolean res = w != size;

        //хвост очищаем
        for (int i = w; i < size; i++) {
            arr[i] = null;
        }

        size = w;
        return res;
    }

    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}