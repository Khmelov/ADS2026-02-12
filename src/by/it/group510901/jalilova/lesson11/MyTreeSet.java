package by.it.group510901.jalilova.lesson11;
/*              toString()
                size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)

                containsAll(Collection)
                addAll(Collection)
                removeAll(Collection)
                retainAll(Collection)*/

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] data;      // отсортированный массив
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        data = new Object[DEFAULT_CAPACITY];
    }

    //поиск

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // Бинарный поиск: возвращает индекс элемента, если найден,
    // иначе -(точка вставки) - 1  (как Arrays.binarySearch)
    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = ((Comparable<? super Object>) data[mid]).compareTo(key);
            if (cmp < 0) lo = mid + 1;
            else if (cmp > 0) hi = mid - 1;
            else return mid;
        }
        return -(lo + 1);
    }

    @Override
    public boolean add(E e) {
        int pos = binarySearch(e);
        if (pos >= 0) return false;       // уже есть

        int insertAt = -pos - 1;          // где вставить
        ensureCapacity(size + 1);
        System.arraycopy(data, insertAt, data, insertAt + 1, size - insertAt);
        data[insertAt] = e;
        size++;
        return true;
    }

    private void ensureCapacity(int needed) {
        if (needed > data.length) {
            int newCap = data.length + (data.length >> 1) + 1;  // *1.5
            Object[] newData = new Object[newCap];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    @Override
    public boolean remove(Object o) {
        int pos = binarySearch(o);
        if (pos < 0) return false;

        System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        data[size - 1] = null;            // помогаем GC
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

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
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    // Массив уже отсортирован — просто идём слева направо
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append("]").toString();
    }

    //bulk-операции

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        // идём слева направо; удаление сдвигает хвост влево —
        // поэтому после remove не инкрементируем i
        int i = 0;
        while (i < size) {
            if (c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (index >= size) throw new java.util.NoSuchElementException();
                return (E) data[index++];
            }
        };
    }

    //toArray

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(data, 0, arr, 0, size);
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        for (int i = 0; i < size; i++) a[i] = (T) data[i];
        if (a.length > size) a[size] = null;
        return a;
    }
}