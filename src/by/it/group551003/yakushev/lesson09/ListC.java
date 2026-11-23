package by.it.group551003.yakushev.lesson09;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    private Object[] elements;
    private int size;
    private int modCount = 0;

    public ListC() {
        elements = new Object[10];
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1);
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            Object e = elements[i];
            result.append(e == this ? "(this Collection)" : e);
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
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
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E old = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            for (int i = 0; i < numMoved; i++) {
                elements[index + i] = elements[index + i + 1];
            }
        }
        elements[--size] = null; // help GC
        modCount++;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
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
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        modCount++;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        int oldSize = size;
        for (E e : c) {
            ensureCapacity(size + 1);
            elements[size++] = e;
        }
        if (size != oldSize) {
            modCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (c == null) throw new NullPointerException();

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) return false;

        ensureCapacity(size + numNew);

        for (int i = size - 1; i >= index; i--) {
            elements[i + numNew] = elements[i];
        }

        for (int i = 0; i < numNew; i++) {
            elements[index + i] = a[i];
        }

        size += numNew;
        modCount++;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }
        return new SubList(fromIndex, toIndex);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                result[i] = (T) elements[i];
            }
            return result;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Вспомогательные методы и внутренние классы   ////////////
    /////////////////////////////////////////////////////////////////////////

    void removeRange(int fromIndex, int toIndex) {
        int numMoved = size - toIndex;
        for (int i = 0; i < numMoved; i++) {
            elements[fromIndex + i] = elements[toIndex + i];
        }
        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        modCount++;
    }

    private class ListItr implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;
        private int expectedModCount = modCount;

        public ListItr(int index) {
            this.cursor = index;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            checkForComodification();
            if (cursor >= size) throw new NoSuchElementException();
            lastRet = cursor++;
            return (E) elements[lastRet];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            checkForComodification();
            if (cursor <= 0) throw new NoSuchElementException();
            lastRet = --cursor;
            return (E) elements[lastRet];
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
            if (lastRet < 0) throw new IllegalStateException();
            checkForComodification();
            ListC.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            checkForComodification();
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            checkForComodification();
            ListC.this.add(cursor, e);
            cursor++;
            lastRet = -1;
            expectedModCount = modCount;
        }
    }

    private class SubList implements List<E> {
        private int fromIndex;
        private int toIndex;
        private int expectedModCount;

        public SubList(int fromIndex, int toIndex) {
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private void updateExpectedModCount() {
            this.expectedModCount = modCount;
        }

        private boolean equalsObjects(Object a, Object b) {
            return a == null ? b == null : a.equals(b);
        }

        @Override
        public int size() {
            checkForComodification();
            return toIndex - fromIndex;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        @Override
        public Iterator<E> iterator() {
            return listIterator();
        }

        @Override
        public Object[] toArray() {
            checkForComodification();
            Object[] result = new Object[size()];
            for (int i = 0; i < size(); i++) {
                result[i] = elements[fromIndex + i];
            }
            return result;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            checkForComodification();
            if (a.length < size()) {
                T[] result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
                for (int i = 0; i < size(); i++) {
                    result[i] = (T) elements[fromIndex + i];
                }
                return result;
            }
            for (int i = 0; i < size(); i++) {
                a[i] = (T) elements[fromIndex + i];
            }
            if (a.length > size()) {
                a[size()] = null;
            }
            return a;
        }

        @Override
        public boolean add(E e) {
            add(size(), e);
            return true;
        }

        @Override
        public boolean remove(Object o) {
            int i = indexOf(o);
            if (i == -1) return false;
            remove(i);
            return true;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            checkForComodification();
            for (Object o : c) {
                if (!contains(o)) return false;
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return addAll(size(), c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            int count = 0;
            for (E e : c) count++;
            if (count == 0) return false;

            ListC.this.addAll(fromIndex + index, c);
            toIndex += count;
            updateExpectedModCount();
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c == null) throw new NullPointerException();
            checkForComodification();
            boolean modified = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (c == null) throw new NullPointerException();
            checkForComodification();
            boolean modified = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (!c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public void clear() {
            checkForComodification();
            if (size() == 0) return;
            ListC.this.removeRange(fromIndex, toIndex);
            toIndex = fromIndex;
            updateExpectedModCount();
        }

        @Override
        public E get(int index) {
            if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            return ListC.this.get(fromIndex + index);
        }

        @Override
        public E set(int index, E element) {
            if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            return ListC.this.set(fromIndex + index, element);
        }

        @Override
        public void add(int index, E element) {
            if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            ListC.this.add(fromIndex + index, element);
            toIndex++;
            updateExpectedModCount();
        }

        @Override
        public E remove(int index) {
            if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            E result = ListC.this.remove(fromIndex + index);
            toIndex--;
            updateExpectedModCount();
            return result;
        }

        @Override
        public int indexOf(Object o) {
            checkForComodification();
            for (int i = 0; i < size(); i++) {
                if (equalsObjects(o, elements[fromIndex + i])) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            checkForComodification();
            for (int i = size() - 1; i >= 0; i--) {
                if (equalsObjects(o, elements[fromIndex + i])) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException();
            }
            checkForComodification();
            return new SubList(this.fromIndex + fromIndex, this.fromIndex + toIndex);
        }

        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
            checkForComodification();
            return new SubListIterator(index);
        }

        private class SubListIterator implements ListIterator<E> {
            private int cursor;
            private int lastRet = -1;
            private int expectedModCount;

            public SubListIterator(int index) {
                this.cursor = index;
                this.expectedModCount = SubList.this.expectedModCount;
            }

            private void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return cursor < size();
            }

            @Override
            public E next() {
                checkForComodification();
                if (cursor >= size()) throw new NoSuchElementException();
                lastRet = cursor++;
                return get(lastRet);
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                checkForComodification();
                if (cursor <= 0) throw new NoSuchElementException();
                lastRet = --cursor;
                return get(lastRet);
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
                if (lastRet < 0) throw new IllegalStateException();
                checkForComodification();
                SubList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = SubList.this.expectedModCount;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0) throw new IllegalStateException();
                checkForComodification();
                SubList.this.set(lastRet, e);
            }

            @Override
            public void add(E e) {
                checkForComodification();
                SubList.this.add(cursor, e);
                cursor++;
                lastRet = -1;
                expectedModCount = SubList.this.expectedModCount;
            }
        }
    }
}