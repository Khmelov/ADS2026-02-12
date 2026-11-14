package by.it.group551001.bogush.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

// Min-куча на массиве. Порядок элементов -- естественный (E должен быть Comparable).
// Алгоритмы просеивания сделаны так же, как в java.util.PriorityQueue,
// чтобы toString() выдавал тот же порядок элементов в массиве.
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // ------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void growIfFull() {
        if (size < heap.length) {
            return;
        }
        E[] bigger = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = heap[i];
        }
        heap = bigger;
    }

    // Поднимает element с позиции k вверх, пока он меньше родителя
    private void siftUp(int k, E element) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            E parentValue = heap[parent];
            if (compare(element, parentValue) >= 0) {
                break;
            }
            heap[k] = parentValue;
            k = parent;
        }
        heap[k] = element;
    }

    // Опускает element с позиции k вниз, пока он больше наименьшего ребёнка
    private void siftDown(int k, E element) {
        int half = size / 2; // позиции >= half -- листья
        while (k < half) {
            int child = 2 * k + 1;
            E smallest = heap[child];
            int right = child + 1;
            if (right < size && compare(smallest, heap[right]) > 0) {
                child = right;
                smallest = heap[child];
            }
            if (compare(element, smallest) <= 0) {
                break;
            }
            heap[k] = smallest;
            k = child;
        }
        heap[k] = element;
    }

    // Строит кучу из произвольного массива за O(n)
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    // Удаляет элемент с позиции index, сохраняя свойство кучи
    private void removeAt(int index) {
        size--;
        if (size == index) {
            heap[index] = null;
        } else {
            E moved = heap[size];
            heap[size] = null;
            siftDown(index, moved);
            if (heap[index] == moved) {
                siftUp(index, moved);
            }
        }
    }

    private int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return i;
            }
        }
        return -1;
    }

    // Общая часть removeAll / retainAll:
    // removeIfContained = true  -> удаляем то, что есть в c (removeAll)
    // removeIfContained = false -> удаляем то, чего нет в c (retainAll)
    private boolean removeMatching(Collection<?> c, boolean removeIfContained) {
        if (c == null) {
            throw new NullPointerException();
        }
        int j = 0;
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            boolean contained = c.contains(element);
            if (contained != removeIfContained) {
                heap[j] = element;
                j++;
            }
        }
        if (j == size) {
            return false;
        }
        for (int i = j; i < size; i++) {
            heap[i] = null;
        }
        size = j;
        heapify();
        return true;
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        growIfFull();
        size++;
        if (size == 1) {
            heap[0] = element;
        } else {
            siftUp(size - 1, element);
        }
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        E last = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0, last);
        }
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeMatching(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeMatching(c, false);
    }

    // ------------------------------------------------------------------
    // Бонус: remove(Object) -- дёшево делается через removeAt
    // ------------------------------------------------------------------

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) {
            return false;
        }
        removeAt(i);
        return true;
    }

    // ------------------------------------------------------------------
    // Остальное из интерфейса -- не обязательно, заглушки
    // ------------------------------------------------------------------

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}