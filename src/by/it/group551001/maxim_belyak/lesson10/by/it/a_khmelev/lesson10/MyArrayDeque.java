package by.it.a_khmelev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // Дек на основе приватного массива E[] БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ.
    // Массив "кольцевой": head - индекс первого элемента, size - количество элементов.
    // Элемент с номером i (0 - первый) лежит в ячейке (head + i) % elements.length

    private E[] elements;
    private int head = 0;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[8];
    }

    // физический индекс ячейки по порядковому номеру элемента
    private int index(int i) {
        return (head + i) % elements.length;
    }

    // массив заполнен - создаём вдвое больший и переписываем элементы по порядку с нуля
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[index(i)];
        }
        elements = bigger;
        head = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[index(i)]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (size == elements.length) {
            grow();
        }
        head = (head - 1 + elements.length) % elements.length; // сдвигаем начало назад по кругу
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) {
            grow();
        }
        elements[index(size)] = element; // первая свободная ячейка после последнего элемента
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[index(size - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int last = index(size - 1);
        E result = elements[last];
        elements[last] = null;
        size--;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)        ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        E result = getFirst(); // бросит исключение, если дек пуст
        pollFirst();
        return result;
    }

    @Override
    public E removeLast() {
        E result = getLast();
        pollLast();
        return result;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : elements[index(size - 1)];
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
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
    public void push(E e) {
        addFirst(e);
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
    public void clear() {
        while (size > 0) {
            pollLast();
        }
        head = 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[index(i)] == null : o.equals(elements[index(i)])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
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
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

}