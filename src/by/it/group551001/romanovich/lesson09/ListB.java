package by.it.group551001.romanovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public class ListB<E> implements List<E> {

    private static class Node<E> {
        private E item;
        private Node<E> prev;
        private Node<E> next;

        private Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> node = first; node != null; node = node.next) {
            sb.append(node.item);
            if (node.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> node = new Node<>(last, e, null);
        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if (index == size) {
            add(element);
            return;
        }
        Node<E> next = node(index);
        Node<E> prev = next.prev;
        Node<E> node = new Node<>(prev, element, next);
        next.prev = node;
        if (prev == null) {
            first = node;
        } else {
            prev.next = node;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> node = first; node != null; node = node.next) {
            if (equal(o, node.item)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> node = node(index);
        E old = node.item;
        node.item = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            node.item = null;
            node.prev = null;
            node.next = null;
            node = next;
        }
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<E> node = first; node != null; node = node.next, index++) {
            if (equal(o, node.item)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        for (Node<E> node = last; node != null; node = node.prev, index--) {
            if (equal(o, node.item)) {
                return index;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        boolean modified = false;
        int position = index;
        for (E e : c) {
            add(position++, e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            if (c.contains(node.item)) {
                unlink(node);
                modified = true;
            }
            node = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            if (!c.contains(node.item)) {
                unlink(node);
                modified = true;
            }
            node = next;
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("from=" + fromIndex + " to=" + toIndex);
        }
        ListB<E> result = new ListB<>();
        Node<E> node = (fromIndex == size) ? null : node(fromIndex);
        for (int i = fromIndex; i < toIndex; i++, node = node.next) {
            result.add(node.item);
        }
        return result;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a;
        if (a.length < size) {
            result = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> node = first; node != null; node = node.next) {
            result[i++] = (T) node.item;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = first; node != null; node = node.next) {
            result[i++] = node.item;
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    private Node<E> node(int index) {
        if (index < size / 2) {
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        }
        Node<E> node = last;
        for (int i = size - 1; i > index; i--) {
            node = node.prev;
        }
        return node;
    }

    private E unlink(Node<E> node) {
        E item = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return item;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private static boolean equal(Object a, Object b) {
        return (a == null) ? b == null : a.equals(b);
    }
}
