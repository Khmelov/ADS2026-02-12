package by.it.group510902.makaruk.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    // Внутренний массив для дерева кучи
    private E[] elements = (E[]) new Object[16];
    // Счетчик реального количества элементов в очереди
    private int size = 0;

    // Вспомогательный метод динамического расширения емкости кучи
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Восстановление свойств кучи: проталкивание нового элемента снизу вверх
    private void siftUp(int index) {
        Comparable<? super E> key = (Comparable<? super E>) elements[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1; // Индекс родителя: (i - 1) / 2
            E e = elements[parent];
            if (key.compareTo(e) >= 0) break; // Если родитель меньше или равен — балансировка окончена
            elements[index] = e;
            index = parent;
        }
        elements[index] = (E) key;
    }

    // Восстановление свойств кучи: проталкивание элемента сверху вниз
    private void siftDown(int index) {
        Comparable<? super E> key = (Comparable<? super E>) elements[index];
        int half = size >>> 1; // Узлы после половины элементов не имеют потомков
        while (index < half) {
            int child = (index << 1) + 1; // Левый потомок: 2 * i + 1
            E c = elements[child];
            int right = child + 1;        // Правый потомок
            // Выбираем наименьшего потомка из двух существующих
            if (right < size && ((Comparable<? super E>) c).compareTo(elements[right]) > 0) {
                child = right;
                c = elements[child];
            }
            if (key.compareTo(c) <= 0) break; // Если текущий элемент меньше детей — балансировка окончена
            elements[index] = c;
            index = child;
        }
        elements[index] = (E) key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        elements[size] = element; // Вставляем в самый конец кучи
        siftUp(size++);           // Проталкиваем вверх
        return true;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = elements[0];     // Минимальный элемент всегда в корне (индекс 0)
        E x = elements[--size];     // Берем самый последний элемент кучи
        elements[size] = null;
        if (size > 0) {
            elements[0] = x;        // Ставим его на место корня
            siftDown(0);            // Просеиваем вниз для выравнивания кучи
        }
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return elements[0];
    }

    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }


    public boolean removeAll(Collection<?> c) {
        int w = 0; // Индекс записи для выживших элементов
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w == size) return false; // Изменений нет
        // Зануляем оставшиеся пустые хвосты массива
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        heapify(); // Восстанавливаем кучу за O(n)
        return true;
    }


    public boolean retainAll(Collection<?> c) {
        int w = 0; // Индекс записи для выживших элементов
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w == size) return false; // Изменений нет
        // Зануляем оставшиеся пустые хвосты массива
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        heapify(); // Восстанавливаем кучу за O(n)
        return true;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                return elements[cursor++];
            }
        };
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                int s = --size;
                if (s == i) {
                    elements[i] = null;
                } else {
                    E moved = elements[s];
                    elements[s] = null;
                    elements[i] = moved;
                    siftDown(i);
                    if (elements[i] == moved) {
                        siftUp(i);
                    }
                }
                return true;
            }
        }
        return false;
    }
}