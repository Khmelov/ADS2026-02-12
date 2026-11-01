package by.it.group551001.romanovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 8;

    private E[] elements = (E[]) new Object[DEFAULT_CAPACITY];
    private int head = 0;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(at(i));
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        elements[(head + size) % elements.length] = element;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return at(size - 1);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int index = (head + size - 1) % elements.length;
        E result = elements[index];
        elements[index] = null;
        size--;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    private E at(int index) {
        return elements[(head + index) % elements.length];
    }

    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }
        E[] newElements = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newElements[i] = at(i);
        }
        elements = newElements;
        head = 0;
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[(head + i) % elements.length] = at(i + 1);
        }
        elements[(head + size - 1) % elements.length] = null;
        size--;
    }

    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? at(i) == null : o.equals(at(i))) {
                return i;
            }
        }
        return -1;
    }

    private int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? at(i) == null : o.equals(at(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : at(size - 1);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        int index = lastIndexOf(o);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            addLast(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(at(i))) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(at(i))) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        size = 0;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
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
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return at(index++);
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                if (index < 0) {
                    throw new NoSuchElementException();
                }
                return at(index--);
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = at(i);
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) at(i);
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }
}
