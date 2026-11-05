package by.it.group551001.romanov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[16];
        size = 0;
    }

    // Увеличение массива при необходимости
    private void grow() {
        int newCapacity = elements.length * 2;
        @SuppressWarnings("unchecked")
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Всплытие элемента наверх
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            Comparable<? super E> current = (Comparable<? super E>) elements[index];
            Comparable<? super E> parent = (Comparable<? super E>) elements[parentIndex];

            // Если текущий меньше родителя, меняем местами
            if (current.compareTo((E) parent) < 0) {
                E temp = elements[index];
                elements[index] = elements[parentIndex];
                elements[parentIndex] = temp;
                index = parentIndex; // Идем дальше наверх
            } else {
                break; // Элемент на своем месте
            }
        }
    }

    // Погружение элемента вниз
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (2 * index + 1 < size) { // Пока есть хотя бы левый ребенок
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int minChild = leftChild;

            // Если есть правый ребенок и он меньше левого, выбираем его
            if (rightChild < size) {
                Comparable<? super E> right = (Comparable<? super E>) elements[rightChild];
                if (right.compareTo(elements[leftChild]) < 0) {
                    minChild = rightChild;
                }
            }

            Comparable<? super E> current = (Comparable<? super E>) elements[index];
            // Если текущий больше наименьшего ребенка, меняем местами
            if (current.compareTo(elements[minChild]) > 0) {
                E temp = elements[index];
                elements[index] = elements[minChild];
                elements[minChild] = temp;
                index = minChild; // Идем дальше вниз
            } else {
                break; // Элемент на своем месте
            }
        }
    }

    // Перестроение кучи (используется после массового удаления)
    private void heapify() {
        // Начинаем с середины массива и идем к началу, проталкивая элементы вниз
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) {
            grow();
        }
        elements[size] = element;
        siftUp(size); // Восстанавливаем порядок кучи снизу вверх
        size++;
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        E result = elements[0]; // Самый приоритетный элемент
        size--;
        elements[0] = elements[size]; // Ставим последний элемент на вершину
        elements[size] = null; // Очищаем для сборщика мусора

        if (size > 0) {
            siftDown(0); // Восстанавливаем порядок кучи сверху вниз
        }
        return result;
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return elements[0]; // Вершина кучи
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (offer(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        // Оставляем только те элементы, которых НЕТ в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Очищаем хвост массива
            for (int i = newSize; i < size; i++) {
                elements[i] = null;
            }
            size = newSize;
            heapify(); // Восстанавливаем кучу
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        // Оставляем только те элементы, которые ЕСТЬ в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = newSize; i < size; i++) {
                elements[i] = null;
            }
            size = newSize;
            heapify(); // Восстанавливаем кучу
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Методы-заглушки (требуются интерфейсом, но не заданием) ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}