package by.it.group510902.kudan.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    //куча лежит в массиве, у элемента i дети стоят в 2*i+1 и 2*i+2, родитель в (i-1)/2
    private E[] heap;

    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[11];
    }

    //поднимаем элемент x с позиции k вверх пока он меньше родителя
    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<E> key = (Comparable<E>) x;

        while (k > 0) {
            int parent = (k - 1) / 2;
            E e = heap[parent];

            if (key.compareTo(e) >= 0) {
                break;
            }

            //родитель спускается вниз на место k
            heap[k] = e;
            k = parent;
        }

        heap[k] = x;
    }

    //опускаем элемент x с позиции k вниз пока у него есть меньший ребенок
    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<E> key = (Comparable<E>) x;
        int half = size / 2;

        //пока у позиции k есть хотя бы один ребенок
        while (k < half) {
            int child = 2 * k + 1;
            E c = heap[child];
            int right = child + 1;

            //из двух детей берем меньшего
            if (right < size && ((Comparable<E>) c).compareTo(heap[right]) > 0) {
                child = right;
                c = heap[child];
            }

            if (key.compareTo(c) <= 0) {
                break;
            }

            //меньший ребенок поднимается на место k
            heap[k] = c;
            k = child;
        }

        heap[k] = x;
    }

    //удаляем элемент с позиции i и чиним кучу
    private void removeAt(int i) {
        size--;

        if (size == i) {
            //удалили последний элемент, чинить нечего
            heap[i] = null;
        } else {
            //последний элемент ставим на место удаленного и двигаем вниз, если не сдвинулся то вверх
            E moved = heap[size];
            heap[size] = null;
            siftDown(i, moved);

            if (heap[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    //ищем позицию элемента в массиве кучи
    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    //оставляем в массиве только нужные элементы в том же порядке и перестраиваем кучу за o(n)
    //если remove true, то убираем элементы которые есть в c, иначе оставляем только их
    private boolean bulk(Collection<?> c, boolean remove) {
        int n = size;
        int w = 0;

        for (int i = 0; i < n; i++) {
            if (c.contains(heap[i]) != remove) {
                heap[w++] = heap[i];
            }
        }

        //ничего не удалили, куча осталась как была
        if (w == n) {
            return false;
        }

        for (int i = w; i < n; i++) {
            heap[i] = null;
        }
        size = w;

        //heapify, идем от последнего родителя к корню и опускаем каждый элемент вниз
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }

        return true;
    }

    @Override
    public String toString() {
        //выводим массив кучи как есть, по порядку хранения
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        //add это то же самое что offer
        return offer(element);
    }

    @Override
    public E remove() {
        //remove берет минимум как poll, но если куча пустая выбрасывает исключение
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public boolean contains(Object element) {
        return indexOf(element) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        //если массив заполнен, увеличиваем его вдвое
        if (size == heap.length) {
            E[] big = (E[]) new Object[heap.length * 2];
            for (int i = 0; i < size; i++) {
                big[i] = heap[i];
            }
            heap = big;
        }

        //кладем в первую свободную ячейку и поднимаем вверх
        siftUp(size, element);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        //минимум лежит в корне, на его место ставим последний элемент и опускаем вниз
        E res = heap[0];
        size--;
        E x = heap[size];
        heap[size] = null;

        if (size > 0) {
            siftDown(0, x);
        }

        return res;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        //element это peek, но с исключением для пустой кучи
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        //достаточно найти один элемент которого нет
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;

        for (E e : c) {
            if (add(e)) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulk(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulk(c, false);
    }

    @Override
    public boolean remove(Object element) {
        int i = indexOf(element);
        if (i < 0) {
            return false;
        }
        removeAt(i);
        return true;
    }

    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}