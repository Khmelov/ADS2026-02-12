package by.it.group551001.belozorchik.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Deque;

// кольцо - после последнего индекса идет нулевой

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;   //сам массив для хоан эл
    private int head;   //индекс первого
    private int tail;   //индекс след свобод ячейки
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[10];
        head = 0;
        tail = 0;
        size = 0;
    }

    //метод расширения М
    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = data.length * 2;
        E[] newData = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % data.length];
        }
        data = newData;
        head = 0;
        tail = size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % data.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    //добавление в начало
    @Override
    public void addFirst(E element) {
        //заполнен/нет
        if (size == data.length) {
            grow();
        }
        //сдвигаем назад по кругу
        head = (head - 1 + data.length) % data.length;
        //запис эл в новую позицию
        data[head] = element;
        size++;
    }

    //добавление в конец
    @Override
    public void addLast(E element) {
        //не заполнен ли массив
        if (size == data.length) {
            grow();
        }
        //записываем эл в ячейку
        data[tail] = element;
        //сдвигаем вперед по кругу
        tail = (tail + 1) % data.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    //вернуть эл, кот наход в начале
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return data[head];
    }

    //эл в конце
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        int lastIndex = (tail - 1 + data.length) % data.length;
        return data[lastIndex];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    //удаление эл из начала
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        //запоминаем ячейку
        E res = data[head];
        //зануляем
        data[head] = null;
        //сдвигаем вперед по кругу
        head = (head + 1) % data.length;
        size--;
        return res;
    }

    //удаление эл из конца
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + data.length) % data.length;
        E res = data[tail];
        data[tail] = null;
        size--;
        return res;
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Технические заглушки интерфейса Deque (чтобы код компилировался)
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { E res = pollFirst(); if (res == null) throw new NoSuchElementException(); return res; }
    @Override public E removeLast() { E res = pollLast(); if (res == null) throw new NoSuchElementException(); return res; }
    @Override public E peek() { return size == 0 ? null : getFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size == 0 ? null : getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { data = (E[]) new Object[10]; head = 0; tail = 0; size = 0; }
    @Override public Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) res[i] = data[(head + i) % data.length];
        return res;
    }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}