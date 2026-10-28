package by.it.group510901.parkhomenko.lesson10;

import java.util.*;
//очередь с приоритетом на куче: наверху (в ячейке 0) всегда самый маленький элемент


@SuppressWarnings("unchecked")


public class MyPriorityQueue<E> implements Queue<E> {


    private E[] heap = (E[]) new Object[10];
    private int size = 0;


    // Если массив заполнен - переносим элементы в массив вдвое больше.
    private void grow() {
        if (size == heap.length) {
            E[] bigger = (E[]) new Object[size * 2];
            for (int i = 0; i < size; i++) bigger[i] = heap[i];
            heap = bigger;
        }
    }

    // Сравнивает два элемента: меньше нуля - a меньше b.
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // Поднимает x с места k вверх, пока родитель больше него.
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            if (compare(x, heap[parent]) >= 0) break;
            heap[k] = heap[parent];
            k = parent;
        }
        heap[k] = x;
    }

    // Опускает x с места k вниз, меняя с меньшим из детей, пока он больше ребёнка.
    private void siftDown(int k, E x) {
        int half = size / 2;
        while (k < half) {
            int child = 2 * k + 1;
            int right = child + 1;
            if (right < size && compare(heap[child], heap[right]) > 0) child = right;
            if (compare(x, heap[child]) <= 0) break;
            heap[k] = heap[child];
            k = child;
        }
        heap[k] = x;
    }

    // Делает кучу из произвольного массива за O(n): просеиваем вниз всех, у кого есть дети.
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) siftDown(i, heap[i]);
    }

    // Убирает элементы, у которых «есть в c» совпадает с inC, сдвигая оставшихся влево, и чинит кучу за O(n).
    private boolean removeIf(Collection<?> c, boolean inC) {
        int w = 0;
        for (int i = 0; i < size; i++)
            if (c.contains(heap[i]) != inC) heap[w++] = heap[i];
        if (w == size) return false;
        for (int i = w; i < size; i++) heap[i] = null;
        size = w;
        heapify();
        return true;
    }

    // Превращает очередь в текст вида "[3, 5, 7]" - в порядке массива, как у PriorityQueue.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append(']').toString();
    }

    // Возвращает, сколько элементов сейчас в очереди.
    @Override
    public int size() {
        return size;
    }

    // Удаляет все элементы.
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    // Добавляет элемент в очередь.
    @Override
    public boolean add(E e) {
        return offer(e);
    }

    // Забирает и удаляет минимум; если пусто - ошибка.
    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    // Отвечает, есть ли o в очереди (простой перебор массива).
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, heap[i])) return true;
        return false;
    }

    // Кладёт элемент в конец массива и поднимает его на своё место.
    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        grow();
        siftUp(size++, e);
        return true;
    }

    // Забирает минимум: на его место ставим последний элемент и опускаем вниз. null, если пусто.
    @Override
    public E poll() {
        if (size == 0) return null;
        E min = heap[0];
        E last = heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0, last);
        return min;
    }

    // Возвращает минимум, не удаляя его (null, если пусто).
    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    // Возвращает минимум, не удаляя его; если пусто - ошибка.
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    // Отвечает, пуста ли очередь.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Есть ли в очереди все элементы из c.
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    // Добавляет по одному все элементы из c. true, если что-то добавили.
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c)
            if (add(e)) changed = true;
        return changed;
    }

    // Удаляет все элементы, которые есть в c.
    @Override
    public boolean removeAll(Collection<?> c) {
        return removeIf(c, true);
    }

    // Оставляет только элементы, которые есть в c.
    @Override
    public boolean retainAll(Collection<?> c) {
        return removeIf(c, false);
    }



    // удалить один элемент, равный o
    @Override
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив Object
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив типа T
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // «указка» для перебора элементов по одному
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

}
