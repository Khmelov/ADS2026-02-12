package by.it.group551003.kalach.lesson11;


import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;   // всегда отсортирован по возрастанию, без повторов
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[10];
    }

    // ---------- вспомогательные методы ----------

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    // бинарный поиск: индекс элемента, если найден; иначе -(место вставки) - 1
    private int search(Object key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], key);
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) return;
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = elements[i];
        elements = bigger;
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
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
        if (element == null) throw new NullPointerException();
        int pos = search(element);
        if (pos >= 0) return false;            // уже есть
        pos = -(pos + 1);                      // место вставки
        ensureCapacity();
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[pos] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) return false;
        int pos = search(element);
        if (pos < 0) return false;
        removeAt(pos);
        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) return false;
        return search(element) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (i >= size) throw new NoSuchElementException();
                return elements[i++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) result[i] = elements[i];
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) elements[i];
        if (a.length > size) a[size] = null;
        return a;
    }
}