package by.it.group551001.maxim_belyak.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private E[] queue;
    private int size;

    public MyPriorityQueue() {
        this.queue = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = 1;
        this.queue = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    // ================== внутренние помощники ==================

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > queue.length) {
            int newCap = queue.length + (queue.length >> 1) + 1;
            if (newCap < minCapacity) newCap = minCapacity;
            E[] newArr = (E[]) new Object[newCap];
            for (int i = 0; i < size; i++) newArr[i] = queue[i];
            queue = newArr;
        }
    }

    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int index) {
        E x = queue[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            if (compare(x, queue[parent]) >= 0) break;
            queue[index] = queue[parent];
            index = parent;
        }
        queue[index] = x;
    }

    private void siftDown(int index) {
        E x = queue[index];
        int half = size >>> 1;
        while (index < half) {
            int left = (index << 1) + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && compare(queue[right], queue[left]) < 0) {
                smallest = right;
            }
            if (compare(x, queue[smallest]) <= 0) break;
            queue[index] = queue[smallest];
            index = smallest;
        }
        queue[index] = x;
    }

    // ================== Queue ==================

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException("null not allowed");
        ensureCapacity(size + 1);
        queue[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = queue[0];
        E last = queue[--size];
        queue[size] = null;
        if (size > 0) {
            queue[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return queue[0];
    }

    @Override
    public E peek() {
        return size == 0 ? null : queue[0];
    }

    // ================== Collection ==================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) queue[i] = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(queue[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) {
            // копируем во временный массив, чтобы избежать бесконечного цикла
            Object[] tmp = new Object[size];
            for (int i = 0; i < size; i++) tmp[i] = queue[i];
            for (Object o : tmp) add((E) o);
            return tmp.length > 0;
        }
        boolean changed = false;
        for (E e : c) { add(e); changed = true; }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(queue[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        // собираем заново, чтобы не нарушить кучу
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                queue[w++] = queue[i];
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) queue[i] = null;
        size = w;
        if (changed) rebuildHeap();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int w = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                queue[w++] = queue[i];
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) queue[i] = null;
        size = w;
        if (changed) rebuildHeap();
        return changed;
    }

    private void rebuildHeap() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i);
    }

    private void removeAt(int index) {
        E last = queue[--size];
        queue[size] = null;
        if (index == size) return;
        queue[index] = last;
        if (index > 0 && compare(last, queue[(index - 1) >>> 1]) < 0) {
            siftUp(index);
        } else {
            siftDown(index);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = queue[i];
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) queue[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(queue[i]);
        }
        return sb.append("]").toString();
    }

    // ================== Iterator ==================
    // Итератор обходит внутренний массив в его текущем (кучевом) порядке.
    // Метод remove() удаляет последний возвращённый элемент по индексу.

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;
            private int expected = size;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (expected != size) throw new java.util.ConcurrentModificationException();
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return queue[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expected = size;
            }
        };
    }
}