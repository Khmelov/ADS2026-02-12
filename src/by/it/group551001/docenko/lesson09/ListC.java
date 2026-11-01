package by.it.group551001.docenko.lesson09;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ


    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private int modCount = 0;

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        E[] bigger = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
    }

    private static void checkElementIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
        }
    }

    private static void checkPositionIndex(int index, int length) {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
        }
    }

    private static void checkSubListRange(int from, int to, int length) {
        if (from < 0 || to > length || from > to) {
            throw new IndexOutOfBoundsException("fromIndex: " + from + ", toIndex: " + to + ", Size: " + length);
        }
    }

    private static boolean isEqual(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private int indexOfRange(Object o, int from, int to) {
        for (int i = from; i < to; i++) {
            if (isEqual(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    private int lastIndexOfRange(Object o, int from, int to) {
        for (int i = to - 1; i >= from; i--) {
            if (isEqual(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    private String toStringRange(int from, int to) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = from; i < to; i++) {
            if (i > from) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        return sb.append(']').toString();
    }

    private Object[] toArrayRange(int from, int to) {
        Object[] result = new Object[to - from];
        for (int i = from; i < to; i++) {
            result[i - from] = elements[i];
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T[] toArrayRange(T[] a, int from, int to) {
        int length = to - from;
        if (a.length < length) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), length);
        }
        for (int i = 0; i < length; i++) {
            a[i] = (T) elements[from + i];
        }
        if (a.length > length) {
            a[length] = null;
        }
        return a;
    }

    private void removeRange(int from, int to) {
        int removed = to - from;
        if (removed == 0) {
            return;
        }
        for (int i = to; i < size; i++) {
            elements[i - removed] = elements[i];
        }
        for (int i = size - removed; i < size; i++) {
            elements[i] = null;
        }
        size -= removed;
        modCount++;
    }

    private int filterRange(Collection<?> c, boolean keepContained, int from, int to) {
        int write = from;
        for (int read = from; read < to; read++) {
            if (c.contains(elements[read]) == keepContained) {
                elements[write++] = elements[read];
            }
        }
        int removed = to - write;
        removeRange(write, to);
        return removed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return toStringRange(0, size);
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        modCount++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkElementIndex(index, size);
        E removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        modCount++;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkPositionIndex(index, size);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
        modCount++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        checkElementIndex(index, size);
        E old = elements[index];
        elements[index] = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        removeRange(0, size);
    }

    @Override
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    @Override
    public E get(int index) {
        checkElementIndex(index, size);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o, 0, size);
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
        return addAll(size, c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index, size);
        Object[] added = c.toArray();
        int count = added.length;
        if (count == 0) {
            return false;
        }
        ensureCapacity(size + count);
        for (int i = size - 1; i >= index; i--) {
            elements[i + count] = elements[i];
        }
        for (int i = 0; i < count; i++) {
            elements[index + i] = (E) added[i];
        }
        size += count;
        modCount++;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filterRange(c, false, 0, size) > 0;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filterRange(c, true, 0, size) > 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkSubListRange(fromIndex, toIndex, size);
        return new SubList(null, fromIndex, toIndex - fromIndex);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index, size);
        return new ListItr(this, index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return toArrayRange(a, 0, size);
    }

    @Override
    public Object[] toArray() {
        return toArrayRange(0, size);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    private class ListItr implements ListIterator<E> {
        private final List<E> list;
        private int cursor;
        private int lastReturned = -1;
        private int expectedModCount = modCount;

        ListItr(List<E> list, int index) {
            this.list = list;
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < list.size();
        }

        @Override
        public E next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = cursor++;
            return list.get(lastReturned);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            checkForComodification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = --cursor;
            return list.get(lastReturned);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            list.remove(lastReturned);
            cursor = lastReturned;
            lastReturned = -1;
            expectedModCount = modCount;
        }

        @Override
        public void set(E e) {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            list.set(lastReturned, e);
        }

        @Override
        public void add(E e) {
            checkForComodification();
            list.add(cursor++, e);
            lastReturned = -1;
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class SubList implements List<E> {
        private final SubList parent;
        private final int offset;
        private int size;
        private int expectedModCount = modCount;

        SubList(SubList parent, int offset, int size) {
            this.parent = parent;
            this.offset = offset;
            this.size = size;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private void updateSizeAndModCount(int delta) {
            for (SubList s = this; s != null; s = s.parent) {
                s.size += delta;
                s.expectedModCount = modCount;
            }
        }

        @Override
        public int size() {
            checkForComodification();
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public E get(int index) {
            checkElementIndex(index, size);
            checkForComodification();
            return ListC.this.get(offset + index);
        }

        @Override
        public E set(int index, E element) {
            checkElementIndex(index, size);
            checkForComodification();
            return ListC.this.set(offset + index, element);
        }

        @Override
        public boolean add(E e) {
            add(size, e);
            return true;
        }

        @Override
        public void add(int index, E element) {
            checkPositionIndex(index, size);
            checkForComodification();
            ListC.this.add(offset + index, element);
            updateSizeAndModCount(1);
        }

        @Override
        public E remove(int index) {
            checkElementIndex(index, size);
            checkForComodification();
            E removed = ListC.this.remove(offset + index);
            updateSizeAndModCount(-1);
            return removed;
        }

        @Override
        public boolean remove(Object o) {
            int index = indexOf(o);
            if (index < 0) {
                return false;
            }
            remove(index);
            return true;
        }

        @Override
        public void clear() {
            checkForComodification();
            removeRange(offset, offset + size);
            updateSizeAndModCount(-size);
        }

        @Override
        public int indexOf(Object o) {
            checkForComodification();
            int index = indexOfRange(o, offset, offset + size);
            return index < 0 ? -1 : index - offset;
        }

        @Override
        public int lastIndexOf(Object o) {
            checkForComodification();
            int index = lastIndexOfRange(o, offset, offset + size);
            return index < 0 ? -1 : index - offset;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) >= 0;
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
            return addAll(size, c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            checkPositionIndex(index, size);
            checkForComodification();
            int sizeBefore = ListC.this.size;
            ListC.this.addAll(offset + index, c);
            int added = ListC.this.size - sizeBefore;
            if (added == 0) {
                return false;
            }
            updateSizeAndModCount(added);
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return filter(c, false);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return filter(c, true);
        }

        private boolean filter(Collection<?> c, boolean keepContained) {
            checkForComodification();
            int removed = filterRange(c, keepContained, offset, offset + size);
            if (removed == 0) {
                return false;
            }
            updateSizeAndModCount(-removed);
            return true;
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            checkSubListRange(fromIndex, toIndex, size);
            checkForComodification();
            return new SubList(this, offset + fromIndex, toIndex - fromIndex);
        }

        @Override
        public Iterator<E> iterator() {
            return listIterator();
        }

        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            checkPositionIndex(index, size);
            checkForComodification();
            return new ListItr(this, index);
        }

        @Override
        public Object[] toArray() {
            checkForComodification();
            return toArrayRange(offset, offset + size);
        }

        @Override
        public <T> T[] toArray(T[] a) {
            checkForComodification();
            return toArrayRange(a, offset, offset + size);
        }

        @Override
        public String toString() {
            checkForComodification();
            return toStringRange(offset, offset + size);
        }
    }

}
