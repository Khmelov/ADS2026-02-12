package by.it.group551002.yermakou.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] a = new Object[10]; // отсортированный массив
    private int size;

    // бинарный поиск: индекс элемента, или -(место вставки)-1, если его нет
    @SuppressWarnings("unchecked")
    private int find(Object o) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = ((Comparable<Object>) a[mid]).compareTo(o);
            if (cmp == 0) return mid;
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return -(left + 1);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        a = new Object[10];
        size = 0;
    }

    public boolean contains(Object o) {
        return find(o) >= 0;
    }

    public boolean add(E e) {
        int i = find(e);
        if (i >= 0) return false; // уже есть
        i = -(i + 1);             // место вставки

        if (size == a.length) {   // массив полон - увеличиваем
            Object[] bigger = new Object[size * 2];
            for (int k = 0; k < size; k++) bigger[k] = a[k];
            a = bigger;
        }
        for (int k = size; k > i; k--) a[k] = a[k - 1]; // сдвиг вправо
        a[i] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int i = find(o);
        if (i < 0) return false;
        for (int k = i; k < size - 1; k++) a[k] = a[k + 1]; // сдвиг влево
        size--;
        a[size] = null;
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : toArray()) { // обходим копию, чтобы можно было удалять
            if (!c.contains(o)) {
                remove(o);
                changed = true;
            }
        }
        return changed;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int k = 0; k < size; k++) result[k] = a[k];
        return result;
    }

    public String toString() {
        String s = "[";
        for (int k = 0; k < size; k++) {
            s += a[k];
            if (k < size - 1) s += ", ";
        }
        return s + "]";
    }

    // не требуются по заданию
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] arr) {
        throw new UnsupportedOperationException();
    }
}
