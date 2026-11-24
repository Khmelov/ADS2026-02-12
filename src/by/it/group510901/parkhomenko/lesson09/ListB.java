package by.it.group510901.parkhomenko.lesson09;

import java.util.*;
//вставка в середину, замена, поиск, проверка «есть ли», очистка


@SuppressWarnings("unchecked")

// ListB - наш собственный список. <E> - тип элементов (его подставят при создании, например Integer).

public class ListB<E> implements List<E> {


    private Object[] data = new Object[10];
    private int size = 0;

    private void grow() {
        if (size == data.length) {
            Object[] bigger = new Object[size * 2];
            for (int i = 0; i < size; i++) bigger[i] = data[i];
            data = bigger;
        }
    }



    // Превращает список в текст вида "[5, 7, 3]" - точно как у ArrayList (тест сравнивает эти строки).
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append(']').toString();
    }

    // Добавляет элемент e в конец списка.
    @Override
    public boolean add(E e) {
        grow();
        data[size++] = e;
        return true;
    }

    // Удаляет элемент с номером index и возвращает его.
    @Override
    public E remove(int index) {
        E old = (E) data[index];
        for (int i = index; i < size - 1; i++) data[i] = data[i + 1];
        data[--size] = null;
        return old;
    }

    // Возвращает, сколько элементов сейчас в списке.
    @Override
    public int size() {
        return size;
    }

    // Вставляет element на место index; всё, что было с этого места и правее, сдвигается вправо.
    @Override
    public void add(int index, E element) {
        grow();
        for (int i = size; i > index; i--) data[i] = data[i - 1];
        data[index] = element;
        size++;
    }

    // Удаляет первый элемент, равный o. Возвращает true, если удалил, и false, если такого не было.
    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    // Заменяет элемент на месте index на element и возвращает старый элемент.
    @Override
    public E set(int index, E element) {
        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    // Отвечает, пуст ли список: true, если элементов нет.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Удаляет все элементы: заводим новый пустой склад и обнуляем счётчик.
    @Override
    public void clear() {
        data = new Object[10];
        size = 0;
    }

    // Ищет o слева направо и возвращает номер первого совпадения или -1, если такого нет.
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }

    // Возвращает элемент с номером index.
    @Override
    public E get(int index) {
        return (E) data[index];
    }

    // Отвечает, есть ли o в списке: есть, если indexOf нашёл номер 0 или больше.
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Ищет o справа налево и возвращает номер последнего совпадения или -1, если такого нет.
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }



    // есть ли в списке все элементы из c
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // добавить в конец все элементы из c
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    // вставить все элементы из c, начиная с места index
    @Override
    public boolean addAll(int index, Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    // удалить все элементы, которые есть в c
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // оставить только элементы, которые есть в c
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // кусок списка от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }

    // «указка» для хождения по списку вперёд и назад, начиная с места index
    @Override
    public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }

    // то же самое, но с начала списка
    @Override
    public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив типа T
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив Object
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    // «указка» для перебора элементов по одному (нужна для цикла for-each)
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

}
