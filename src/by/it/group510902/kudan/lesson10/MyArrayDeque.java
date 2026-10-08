package by.it.group510902.kudan.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    //массив работает как кольцевой буфер, элементы лежат по кругу начиная с индекса head
    private E[] arr;

    //индекс первого элемента деки
    private int head;

    //сколько элементов сейчас в деке
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        //массив E напрямую не создать, поэтому создаем массив объектов и приводим тип
        arr = (E[]) new Object[16];
    }

    //когда массив заполнился, делаем новый вдвое больше и переносим элементы
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] big = (E[]) new Object[arr.length * 2];

        //первый элемент ставим в индекс 0, остальные идут за ним
        for (int i = 0; i < size; i++) {
            big[i] = arr[(head + i) % arr.length];
        }

        arr = big;
        head = 0;
    }

    @Override
    public String toString() {
        //собираем строку вида [1, 2, 3] от первого элемента к последнему
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            sb.append(arr[(head + i) % arr.length]);
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
    public boolean add(E element) {
        //add это то же самое что addLast
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (size == arr.length) {
            grow();
        }

        //двигаем head на шаг назад по кругу и кладем элемент в эту ячейку
        head = (head - 1 + arr.length) % arr.length;
        arr[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (size == arr.length) {
            grow();
        }

        //кладем элемент в ячейку сразу после последнего
        arr[(head + size) % arr.length] = element;
        size++;
    }

    @Override
    public E element() {
        //element это то же самое что getFirst
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return arr[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return arr[(head + size - 1) % arr.length];
    }

    @Override
    public E poll() {
        //poll это то же самое что pollFirst
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        //запоминаем элемент, очищаем ячейку и двигаем head вперед по кругу
        E x = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        size--;
        return x;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        //индекс последнего элемента, его убираем, head не трогаем
        int i = (head + size - 1) % arr.length;
        E x = arr[i];
        arr[i] = null;
        size--;
        return x;
    }

    //дальше методы которых нет в задании, простые сделаны, остальные заглушки
    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E removeFirst() {
        //getFirst сам выбросит исключение если дека пустая
        E x = getFirst();
        pollFirst();
        return x;
    }

    @Override
    public E removeLast() {
        E x = getLast();
        pollLast();
        return x;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : arr[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : arr[(head + size - 1) % arr.length];
    }

    @Override
    public boolean offer(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        //просто идем по всем элементам и сравниваем через equals
        for (int i = 0; i < size; i++) {
            if (o != null && o.equals(arr[(head + i) % arr.length])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
        head = 0;
        size = 0;
    }

    public boolean removeFirstOccurrence(Object o) { return false; }
    public boolean removeLastOccurrence(Object o) { return false; }
    public boolean remove(Object o) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public Iterator<E> iterator() { return null; }
    public Iterator<E> descendingIterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}
