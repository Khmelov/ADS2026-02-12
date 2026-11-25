package by.it.group510901.petsevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public ListC() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // =====================================================================
    //                        ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ
    // =====================================================================

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        // Сдвигаем хвост влево на 1 вручную
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        // Сдвигаем хвост вправо на 1 вручную
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx < 0) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (eq(o, elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E value = (E) elements[index];
        return value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (eq(o, elements[i])) return i;
        }
        return -1;
    }


    // =====================================================================
    //                        ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =====================================================================

    private void ensureCapacity(int required) {
        if (required > elements.length) {
            int newCap = 2 * elements.length;
            if (newCap < required) newCap = required;
            Object[] bigger = new Object[newCap];
            for (int i = 0; i < size; i++) bigger[i] = elements[i];
            elements = bigger;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
    }

    private static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    // =====================================================================
    //                        ОПЦИОНАЛЬНЫЕ МЕТОДЫ
    // =====================================================================

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        Object[] arr = c.toArray();
        if (arr.length == 0) return false;
        ensureCapacity(size + arr.length);

        // Сдвигаем хвост вправо сразу на arr.length
        for (int i = size - 1; i >= index; i--) {
            elements[i + arr.length] = elements[i];
        }
        // Копируем новые элементы
        for (int i = 0; i < arr.length; i++) {
            @SuppressWarnings("unchecked")
            E value = (E) arr[i];
            elements[index + i] = value;
        }
        size += arr.length;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int w = 0; // write pointer
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        size = w;
        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException(
                    "fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        return new SubList(fromIndex, toIndex);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
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
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) result[i] = (T) elements[i];
            return result;
        }
        for (int i = 0; i < size; i++) a[i] = (T) elements[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    // =====================================================================
    //                        ITERATOR
    // =====================================================================

    private class Itr implements Iterator<E> {
        protected int cursor = 0;
        protected int lastRet = -1;

        @Override
        public boolean hasNext() { return cursor < size; }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor >= size) throw new NoSuchElementException();
            E value = (E) elements[cursor];
            lastRet = cursor++;
            return value;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    // =====================================================================
    //                        LIST ITERATOR
    // =====================================================================

    private class ListItr extends Itr implements ListIterator<E> {

        ListItr(int index) { cursor = index; }

        @Override public boolean hasPrevious() { return cursor > 0; }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            if (cursor <= 0) throw new NoSuchElementException();
            E value = (E) elements[--cursor];
            lastRet = cursor;
            return value;
        }

        @Override public int nextIndex() { return cursor; }
        @Override public int previousIndex() { return cursor - 1; }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(cursor++, e);
            lastRet = -1;
        }
    }

    // =====================================================================
    //                        SUBLIST (VIEW)
    // =====================================================================

    private class SubList implements List<E> {

        private final int offset;
        private int subSize;

        SubList(int fromIndex, int toIndex) {
            this.offset = fromIndex;
            this.subSize = toIndex - fromIndex;
        }

        private void checkSubIndex(int index) {
            if (index < 0 || index >= subSize)
                throw new IndexOutOfBoundsException("index: " + index + ", size: " + subSize);
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > subSize)
                throw new IndexOutOfBoundsException("index: " + index + ", size: " + subSize);
        }

        @Override public int size() { return subSize; }
        @Override public boolean isEmpty() { return subSize == 0; }
        @Override public boolean contains(Object o) { return indexOf(o) >= 0; }
        @Override public Iterator<E> iterator() { return listIterator(); }

        @Override
        public Object[] toArray() {
            Object[] r = new Object[subSize];
            for (int i = 0; i < subSize; i++) r[i] = ListC.this.elements[offset + i];
            return r;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if (a.length < subSize) {
                T[] r = (T[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), subSize);
                for (int i = 0; i < subSize; i++) r[i] = (T) ListC.this.elements[offset + i];
                return r;
            }
            for (int i = 0; i < subSize; i++) a[i] = (T) ListC.this.elements[offset + i];
            if (a.length > subSize) a[subSize] = null;
            return a;
        }

        @Override
        public boolean add(E e) {
            ListC.this.add(offset + subSize, e);
            subSize++;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            int i = indexOf(o);
            if (i < 0) return false;
            remove(i);
            return true;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c) if (!contains(o)) return false;
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) { return addAll(subSize, c); }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            Object[] arr = c.toArray();
            if (arr.length == 0) return false;
            ListC.this.addAll(offset + index, c);
            subSize += arr.length;
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean changed = false;
            for (int i = subSize - 1; i >= 0; i--) {
                if (c.contains(ListC.this.elements[offset + i])) {
                    ListC.this.remove(offset + i);
                    subSize--;
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean changed = false;
            for (int i = subSize - 1; i >= 0; i--) {
                if (!c.contains(ListC.this.elements[offset + i])) {
                    ListC.this.remove(offset + i);
                    subSize--;
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public void clear() {
            for (int i = subSize - 1; i >= 0; i--) ListC.this.remove(offset + i);
            subSize = 0;
        }

        @Override
        public E get(int index) {
            checkSubIndex(index);
            return ListC.this.get(offset + index);
        }

        @Override
        public E set(int index, E element) {
            checkSubIndex(index);
            return ListC.this.set(offset + index, element);
        }

        @Override
        public void add(int index, E element) {
            rangeCheckForAdd(index);
            ListC.this.add(offset + index, element);
            subSize++;
        }

        @Override
        public E remove(int index) {
            checkSubIndex(index);
            E old = ListC.this.remove(offset + index);
            subSize--;
            return old;
        }

        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < subSize; i++)
                if (eq(o, ListC.this.elements[offset + i])) return i;
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            for (int i = subSize - 1; i >= 0; i--)
                if (eq(o, ListC.this.elements[offset + i])) return i;
            return -1;
        }

        @Override public ListIterator<E> listIterator() { return listIterator(0); }

        @Override
        public ListIterator<E> listIterator(int index) {
            rangeCheckForAdd(index);
            return new ListIterator<E>() {
                private int cursor = index;
                private int lastRet = -1;

                @Override public boolean hasNext() { return cursor < subSize; }

                @Override public E next() {
                    if (cursor >= subSize) throw new NoSuchElementException();
                    E value = ListC.this.get(offset + cursor);
                    lastRet = cursor++;
                    return value;
                }

                @Override public boolean hasPrevious() { return cursor > 0; }

                @Override public E previous() {
                    if (cursor <= 0) throw new NoSuchElementException();
                    E value = ListC.this.get(offset + --cursor);
                    lastRet = cursor;
                    return value;
                }

                @Override public int nextIndex() { return cursor; }
                @Override public int previousIndex() { return cursor - 1; }

                @Override
                public void remove() {
                    if (lastRet < 0) throw new IllegalStateException();
                    SubList.this.remove(lastRet);
                    cursor = lastRet;
                    lastRet = -1;
                }

                @Override
                public void set(E e) {
                    if (lastRet < 0) throw new IllegalStateException();
                    SubList.this.set(lastRet, e);
                }

                @Override
                public void add(E e) {
                    SubList.this.add(cursor++, e);
                    lastRet = -1;
                }
            };
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > subSize || fromIndex > toIndex)
                throw new IndexOutOfBoundsException(
                        "fromIndex: " + fromIndex + ", toIndex: " + toIndex);
            return new SubList(offset + fromIndex, offset + toIndex);
        }
    }
}