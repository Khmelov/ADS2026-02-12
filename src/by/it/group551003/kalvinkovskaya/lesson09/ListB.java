package by.it.group551003.kalvinkovskaya.lesson09;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListB<E> implements List<E> {
    private class NodeB {
        E data;
        NodeB next;
        NodeB prev;

        NodeB(E data, NodeB prev, NodeB next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private NodeB first;
    private NodeB last;
    private int size;
    private int modCount = 0;

    public ListB() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        NodeB current = first;
        str.append(current.data);
        current = current.next;
        while (current != null) {
            str.append(", ").append(current.data);
            current = current.next;
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean add(E e) {
        NodeB newNode = new NodeB(e, last, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        size++;
        modCount++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        NodeB to_delete = node(index);
        E deleted_data = to_delete.data;

        if (to_delete.prev == null) {
            first = to_delete.next;
        } else {
            to_delete.prev.next = to_delete.next;
            //to_delete.prev = null;
        }

        if (to_delete.next == null) {
            last = to_delete.prev;
        } else {
            to_delete.next.prev = to_delete.prev;
            to_delete.next = null;
        }
        size--;
        modCount++;
        return deleted_data;
    }

    private NodeB node(int index) {
        if (index < (size >> 1)) {
            NodeB current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            NodeB current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
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
        NodeB current = node(index);
        NodeB newNode = new NodeB(element, current.prev, current);
        if (current.prev == null) {
            first = newNode;
        } else {
            current.prev.next = newNode;
        }
        current.prev = newNode;
        size++;
        modCount++;
    }

    @Override
    public boolean remove(Object o) {
        NodeB current = first;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                if (current.prev == null) {
                    first = current.next;
                } else {
                    current.prev.next = current.next;
                }
                if (current.next == null) {
                    last = current.prev;
                } else {
                    current.next.prev = current.prev;
                }
                size--;
                modCount++;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        NodeB current = node(index);
        E oldData = current.data;
        current.data = element;
        modCount++;
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
        modCount++;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        NodeB current = first;
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
        int index = size - 1;
        NodeB current = last;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
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
        ListB<E> subList = new ListB<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        NodeB current = first;
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
        NodeB current = first;
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
        return new ListBIterator(0);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListBIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListBIterator(index);
    }

    private class ListBIterator implements ListIterator<E> {
        private int cursor;
        private int lastRet = -1;
        private int expectedModCount = modCount;

        public ListBIterator(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
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
            if (!hasNext()) {
                throw new NoSuchElementException();
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
            checkForComodification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
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
            checkForComodification();
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListB.this.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
            expectedModCount = modCount; // Синхронизируем после изменения
        }

        @Override
        public void set(E e) {
            checkForComodification();
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            ListB.this.set(lastRet, e);
            expectedModCount = modCount; // Синхронизируем после изменения
        }

        @Override
        public void add(E e) {
            checkForComodification();
            ListB.this.add(cursor, e);
            cursor++;
            lastRet = -1;
            expectedModCount = modCount; // Синхронизируем после изменения
        }
    }
}