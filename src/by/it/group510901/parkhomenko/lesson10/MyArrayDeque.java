package by.it.group510901.parkhomenko.lesson10;

import java.util.*;
//двусторонняя очередь на кольцевом массиве: добавить/взять/удалить с начала и с конца


@SuppressWarnings("unchecked")


public class MyArrayDeque<E> implements Deque<E> {


    private E[] data = (E[]) new Object[10];
    private int head = 0;
    private int size = 0;


    // Если массив заполнен - переносим элементы по порядку в массив вдвое больше.
    private void grow() {
        if (size == data.length) {
            E[] bigger = (E[]) new Object[size * 2];
            for (int i = 0; i < size; i++) bigger[i] = data[index(i)];
            data = bigger;
            head = 0;
        }
    }

    // Переводит номер элемента в очереди (0 - первый) в номер ячейки массива по кругу.
    private int index(int i) {
        return (head + i) % data.length;
    }

    // Превращает очередь в текст вида "[5, 7, 3]" - от первого к последнему, как у ArrayDeque.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[index(i)]);
        }
        return sb.append(']').toString();
    }

    // Возвращает, сколько элементов сейчас в очереди.
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в конец очереди.
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Добавляет элемент в начало: голова сдвигается на ячейку назад по кругу.
    @Override
    public void addFirst(E e) {
        grow();
        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        size++;
    }

    // Добавляет элемент в конец: кладём в ячейку сразу за последним.
    @Override
    public void addLast(E e) {
        grow();
        data[index(size)] = e;
        size++;
    }

    // Возвращает первый элемент, не удаляя его.
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент; если очередь пуста - ошибка.
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    // Возвращает последний элемент; если очередь пуста - ошибка.
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[index(size - 1)];
    }

    // Забирает и удаляет первый элемент (null, если пусто).
    @Override
    public E poll() {
        return pollFirst();
    }

    // Забирает первый элемент, голова сдвигается вперёд; null, если пусто.
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = data[head];
        data[head] = null;
        head = index(1);
        size--;
        return e;
    }

    // Забирает последний элемент; null, если пусто.
    @Override
    public E pollLast() {
        if (size == 0) return null;
        int last = index(size - 1);
        E e = data[last];
        data[last] = null;
        size--;
        return e;
    }



    // добавить в начало (вернуть true/false вместо ошибки)
    @Override
    public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }

    // добавить в конец (вернуть true/false вместо ошибки)
    @Override
    public boolean offerLast(E e) { throw new UnsupportedOperationException(); }

    // удалить и вернуть первый (ошибка, если пусто)
    @Override
    public E removeFirst() { throw new UnsupportedOperationException(); }

    // удалить и вернуть последний (ошибка, если пусто)
    @Override
    public E removeLast() { throw new UnsupportedOperationException(); }

    // посмотреть первый (null, если пусто)
    @Override
    public E peekFirst() { throw new UnsupportedOperationException(); }

    // посмотреть последний (null, если пусто)
    @Override
    public E peekLast() { throw new UnsupportedOperationException(); }

    // удалить первое вхождение o
    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    // удалить последнее вхождение o
    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    // добавить в конец (вернуть true/false вместо ошибки)
    @Override
    public boolean offer(E e) { throw new UnsupportedOperationException(); }

    // удалить и вернуть первый (ошибка, если пусто)
    @Override
    public E remove() { throw new UnsupportedOperationException(); }

    // посмотреть первый (null, если пусто)
    @Override
    public E peek() { throw new UnsupportedOperationException(); }

    // добавить в конец все элементы из c
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    // положить в начало (как в стек)
    @Override
    public void push(E e) { throw new UnsupportedOperationException(); }

    // снять с начала (как со стека)
    @Override
    public E pop() { throw new UnsupportedOperationException(); }

    // удалить первое вхождение o
    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    // есть ли o в очереди
    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    // есть ли все элементы из c
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // удалить все элементы, которые есть в c
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // оставить только элементы, которые есть в c
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    // удалить все элементы
    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    // пуста ли очередь
    @Override
    public boolean isEmpty() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив Object
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив типа T
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // «указка» для перебора от начала к концу
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    // «указка» для перебора от конца к началу
    @Override
    public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

}
