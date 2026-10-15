package by.it.group551003.kalvinkovskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private class NodeC {
        E data;
        NodeC next;

        NodeC(E data, NodeC next) {
            this.data = data;
            this.next = next;
        }
    }

    private NodeC first;
    private NodeC last;
    private int size;

    public ListC() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    private NodeC node(int index) {
        NodeC current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        NodeC curr = first;
        str.append(curr.data);
        curr = curr.next;
        while (curr != null) {
            str.append(", ").append(curr.data);
            curr = curr.next;
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean add(E e) {
        NodeC newNode = new NodeC(e, null);
        if (first == null) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E deletedData;
        if (index == 0) {
            deletedData = first.data;
            first = first.next;
            if (first == null) {
                last = null;
            }
        } else {
            NodeC prev = node(index - 1);
            deletedData = prev.next.data;
            prev.next = prev.next.next;
            if (index == size - 1) {
                last = prev;
            }
        }
        size--;
        return deletedData;
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
        if (index == size) {
            add(element);
            return;
        }
        if (index == 0) {
            first = new NodeC(element, first);
            if (size == 0) {
                last = first;
            }
        } else {
            NodeC prev = node(index - 1);
            prev.next = new NodeC(element, prev.next);
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        NodeC current = first;
        NodeC prev = null;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                if (prev == null) {
                    first = current.next;
                    if (first == null) {
                        last = null;
                    }
                } else {
                    prev.next = current.next;
                    if (current.next == null) {
                        last = prev;
                    }
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        NodeC current = node(index);
        E oldData = current.data;
        current.data = element;
        return oldData;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        NodeC current = first;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return node(index).data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        int index = 0;
        NodeC current = first;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                lastIndex = index;
            }
            current = current.next;
            index++;
        }
        return lastIndex;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        for (E item : c) {
            add(item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        boolean modified = false;
        int currentIndex = index;
        for (E item : c) {
            add(currentIndex, item);
            currentIndex++;
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (c.contains(get(i))) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(get(i))) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }
        ListC<E> subList = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        NodeC current = first;
        while (current != null) {
            result[i++] = current.data;
            current = current.next;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        T[] result;
        if (a.length < size) {
            result = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        } else {
            result = a;
        }
        int i = 0;
        NodeC current = first;
        while (current != null) {
            result[i++] = (T) current.data;
            current = current.next;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorC();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListCIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListCIterator(index);
    }

    private class IteratorC implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastRet = cursor;
            cursor++;
            return node(lastRet).data;
        }

        @Override
        public void remove() {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    private class ListCIterator implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;

        public ListCIterator(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastRet = cursor;
            cursor++;
            return node(lastRet).data;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new java.util.NoSuchElementException();
            }
            cursor--;
            lastRet = cursor;
            return node(lastRet).data;
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
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(cursor, e);
            cursor++;
            lastRet = -1;
        }
    }
}