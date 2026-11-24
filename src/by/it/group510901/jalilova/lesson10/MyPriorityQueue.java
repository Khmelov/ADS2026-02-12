package by.it.group510901.jalilova.lesson10;


import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*  Задание на уровень C

    Создайте class MyPriorityQueue<E>, который реализует интерфейс Queue<E>
    и работает на основе кучи, построенной на приватном массиве типа E[]
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

                toString()
                size()
                clear()
                add(E element)
                remove()
                contains(E element)

                offer(E element)
                poll()
                peek()
                element()
                isEmpty()

                containsAll(Collection<E> c)
                addAll(Collection<E> c)
                removeAll(Collection<E> c)
                retainAll(Collection<E> c) */

import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap = (E[]) new Object[11];
    private int size = 0;

    private void siftUp(int k, E x){
        Comparable<? super E> key = (Comparable<? super E>) x;
        while(k>0){
            int parent = (k - 1) / 2;
            E parentNode = heap[parent];
            // Если родитель меньше или равен x
            if(key.compareTo(parentNode) >= 0){
                break;
            }
            heap[k] = parentNode;
            k = parent;

        }
        heap[k] = x;
    }

    private void siftDown(int k, E x){
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size / 2;
        while (k < half) {
            int child = 2 * k + 1;
            E c = heap[child];
            int right = child + 1;

            if (right < size && ((Comparable<? super E>) c).compareTo(heap[right]) > 0) {
                child = right;
                c = heap[right];
            }

            if (key.compareTo(c) <= 0) {
                break;
            }

            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private void grow() {
        int oldCapacity = heap.length;
        int newCapacity = oldCapacity * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    // --- Методы интерфейса Collection ---

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (heap[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }


    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            }
        }
        if (writeIndex != size) {
            for (int i = writeIndex; i < size; i++) {
                heap[i] = null; // зачищаем хвост
            }
            size = writeIndex;
            heapify(); // восстанавливаем кучу
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            }
        }
        if (writeIndex != size) {
            for (int i = writeIndex; i < size; i++) {
                heap[i] = null; // зачищаем хвост
            }
            size = writeIndex;
            heapify(); // восстанавливаем кучу
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    // --- Методы интерфейса Queue ---

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (size >= heap.length) {
            grow();
        }
        if (size == 0) {
            heap[0] = e;
        } else {
            siftUp(size, e);
        }
        size++;
        return true;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = heap[0];
        E x = heap[s];
        heap[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

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

    private E removeAt(int i) {
        int s = --size;
        if (s == i) { // Если удаляем самый последний элемент
            heap[i] = null;
        } else {
            E moved = heap[s];
            heap[s] = null;
            siftDown(i, moved); // Пытаемся опустить replacement вниз
            if (heap[i] == moved) {
                siftUp(i, moved); // Если он не опустился, пробуем поднять вверх
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        removeAt(i);
        return true;
    }

}
