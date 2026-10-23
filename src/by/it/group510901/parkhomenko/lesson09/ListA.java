package by.it.group510901.parkhomenko.lesson09;

import java.util.*;
//минимум: добавить в конец, удалить по номеру, узнать размер, напечатать


@SuppressWarnings("unchecked")


public class ListA<E> implements List<E> {


    private Object[] data = new Object[10];
    private int size = 0;


    // Вызывается перед каждым добавлением, чтобы для нового элемента всегда было место.
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



    // вставить элемент на место index
    @Override
    public void add(int index, E element) { throw new UnsupportedOperationException(); }

    // удалить элемент по значению
    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    // заменить элемент на месте index
    @Override
    public E set(int index, E element) { throw new UnsupportedOperationException(); }

    // пуст ли список
    @Override
    public boolean isEmpty() { throw new UnsupportedOperationException(); }

    // удалить все элементы
    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    // номер первого такого элемента (или -1, если его нет)
    @Override
    public int indexOf(Object o) { throw new UnsupportedOperationException(); }

    // взять элемент по номеру
    @Override
    public E get(int index) { throw new UnsupportedOperationException(); }

    // есть ли такой элемент в списке
    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    // номер последнего такого элемента (или -1, если его нет)
    @Override
    public int lastIndexOf(Object o) { throw new UnsupportedOperationException(); }

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
