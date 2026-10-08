package by.it.group551003.kalach.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head = 0;   // индекс первого элемента
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[8];
    }

    // ---------- вспомогательные методы ----------

    private int index(int i) {
        return (head + i) % elements.length;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) return;
        E[] bigger = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[index(i)];
        }
        elements = bigger;
        head = 0;
    }

    private void checkNotNull(E e) {
        if (e == null) throw new NullPointerException();
    }

    ////////////////////////////////////////////////////////////////////////
    //////             Обязательные к реализации методы             ///////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[index(i)]);
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
        checkNotNull(element);
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        checkNotNull(element);
        ensureCapacity();
        elements[index(size)] = element;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[index(size - 1)];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        int last = index(size - 1);
        E result = elements[last];
        elements[last] = null;
        size--;
        return result;
    }

    ////////////////////////////////////////////////////////////////////////
    //////        Остальные методы интерфейса (не обязательны)      ///////
    ////////////////////////////////////////////////////////////////////////

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
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : elements[index(size - 1)];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[index(i)].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[index(i)].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int i) {
        for (int j = i; j < size - 1; j++) {
            elements[index(j)] = elements[index(j + 1)];
        }
        elements[index(size - 1)] = null;
        size--;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
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
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
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
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[index(i)].equals(o)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        head = 0;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            while (removeFirstOccurrence(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[index(i)])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }

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
                return elements[index(i++)];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int i = size - 1;

            @Override
            public boolean hasNext() {
                return i >= 0;
            }

            @Override
            public E next() {
                if (i < 0) throw new NoSuchElementException();
                return elements[index(i--)];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[index(i)];
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[index(i)];
        }
        if (a.length > size) a[size] = null;
        return a;
    }
}