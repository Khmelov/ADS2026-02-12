package by.it.group551003.yakushev.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {
    private Object[] heap;
    private int size;

    public MyPriorityQueue() {
        this.heap = new Object[16];
        this.size = 0;
    }

    private void grow() {
        int oldCapacity = heap.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        Object[] newHeap = new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = (E) heap[parent];
            if (compare(x, e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = (E) heap[child];
            int right = child + 1;
            if (right < size && compare(c, (E) heap[right]) > 0) {
                c = (E) heap[child = right];
            }
            if (compare(x, c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    /** Построение кучи за O(n), как в java.util.PriorityQueue. */
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) heap[i]);
        }
    }

    /**
     * Массовое удаление за O(n), как в JDK 9+:
     * 1) вычисляем предикат для всех элементов (до любых изменений),
     * 2) уплотняем массив, сохраняя порядок оставшихся,
     * 3) делаем heapify.
     */
    private boolean bulkRemove(Predicate<? super E> filter) {
        final int end = size;
        boolean[] remove = new boolean[end];
        boolean any = false;
        for (int i = 0; i < end; i++) {
            if (filter.test((E) heap[i])) {
                remove[i] = true;
                any = true;
            }
        }
        if (!any) return false;

        int w = 0;
        for (int i = 0; i < end; i++) {
            if (!remove[i]) heap[w++] = heap[i];
        }
        for (int i = w; i < end; i++) heap[i] = null;
        size = w;

        heapify();
        return true;
    }

    /**
     * Точная копия java.util.PriorityQueue.removeAt.
     * Возвращает элемент, который был перемещён siftUp (или null).
     * Это критически важно для итератора (forgetMeNot).
     */
    private E removeAt(int i) {
        int s = --size;
        if (s == i) {
            heap[i] = null;
            return null;
        } else {
            E moved = (E) heap[s];
            heap[s] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
                if (heap[i] != moved) {
                    return moved;
                }
            }
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (size == heap.length) grow();
        siftUp(size++, e);
        return true;
    }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (heap[i] == null) return i;
            }
        }
        return -1;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = (E) heap[0];
        removeAt(0);
        return result;
    }

    @Override
    public E element() {
        E e = peek();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E peek() { return (size == 0) ? null : (E) heap[0]; }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
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
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        return bulkRemove(e -> c.contains(e));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        return bulkRemove(e -> !c.contains(e));
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Точная копия java.util.PriorityQueue.Itr.
     * Ключевой механизм: forgetMeNot — буфер для элементов, которые при removeAt
     * были перемещены siftUp на уже пройденную итератором позицию.
     */
    private class Itr implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;
        private Object[] forgetMeNot = null;
        private int fmnSize = 0;
        private E lastRetElt = null;

        @Override
        public boolean hasNext() {
            return cursor < size || (forgetMeNot != null && fmnSize > 0);
        }

        @Override
        public E next() {
            if (cursor < size) {
                lastRet = cursor++;
                return (E) heap[lastRet];
            }
            if (forgetMeNot != null && fmnSize > 0) {
                lastRet = -1;
                lastRetElt = (E) forgetMeNot[0];
                // poll из forgetMeNot (FIFO)
                System.arraycopy(forgetMeNot, 1, forgetMeNot, 0, fmnSize - 1);
                fmnSize--;
                forgetMeNot[fmnSize] = null;
                return lastRetElt;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (lastRet != -1) {
                E moved = removeAt(lastRet);
                cursor = lastRet;
                lastRet = -1;
                if (moved != null) {
                    if (forgetMeNot == null) {
                        forgetMeNot = new Object[4];
                    }
                    if (fmnSize == forgetMeNot.length) {
                        Object[] newFmn = new Object[forgetMeNot.length * 2];
                        System.arraycopy(forgetMeNot, 0, newFmn, 0, fmnSize);
                        forgetMeNot = newFmn;
                    }
                    forgetMeNot[fmnSize++] = moved;
                }
            } else if (lastRetElt != null) {
                for (int i = 0; i < fmnSize; i++) {
                    if (forgetMeNot[i] == lastRetElt) {
                        System.arraycopy(forgetMeNot, i + 1, forgetMeNot, i, fmnSize - i - 1);
                        fmnSize--;
                        forgetMeNot[fmnSize] = null;
                        break;
                    }
                }
                lastRetElt = null;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}