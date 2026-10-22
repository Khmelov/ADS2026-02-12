package by.it.group551001.telipko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class MyArrayDeque<E> implements Deque<E> {

    public static void main(String[] args) {
        MyArrayDeque<String> d = new MyArrayDeque<>();
        System.out.println(d);
        d.addLast("B");
        d.addFirst("A");
        d.addLast("C");
        System.out.println(d);
        System.out.println(d.pollFirst());
        System.out.println(d.pollLast());
        System.out.println(d);
    }

    private Object[] array;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        array = new Object[16];
        head = 7;
        tail = 7;
        size = 0;
    }


  private void grow() {
        int oldCapacity = array.length;
        Object[] newArray = new Object[oldCapacity * 2];

        int headPortionLength = oldCapacity - head;
        System.arraycopy(array, head, newArray, 0, headPortionLength);
        System.arraycopy(array, 0, newArray, headPortionLength, tail);

        array = newArray;
        head = 0;
        tail = oldCapacity;
    }

    private int physical(int logicalIndex) {
        return (head + logicalIndex) % array.length;
    }

    private void removeAt(int logicalIndex) {
        for (int k = logicalIndex; k < size - 1; k++) {
            array[physical(k)] = array[physical(k + 1)];
        }
        pollLast();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");

        int currentIdx = head;
        for (int step = 0; step < size; step++) {
            stringBuilder.append(array[currentIdx]);

            if (step < size - 1) {
                stringBuilder.append(", ");
            }

            currentIdx = (currentIdx + 1) % array.length;
        }

        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void addFirst(E e) {
        if (size == array.length) {
            grow();
        }
        head = (head - 1 + array.length) % array.length;
        array[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == array.length) {
            grow();
        }
        array[tail] = e;
        tail = (tail + 1) % array.length;
        size++;
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
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public void push(E e) {
        addFirst(e);
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


    @SuppressWarnings("unchecked")
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) array[head];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) array[(tail - 1 + array.length) % array.length];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : getLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }


    @SuppressWarnings("unchecked")
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E result = (E) array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        size--;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + array.length) % array.length;
        E result = (E) array[tail];
        array[tail] = null;
        size--;
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }


    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            Object cur = array[physical(i)];
            if (o == null ? cur == null : o.equals(cur)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            Object cur = array[physical(i)];
            if (o == null ? cur == null : o.equals(cur)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (c.contains(array[physical(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(array[physical(i)])) {
                removeAt(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        while (size > 0) {
            pollFirst();
        }
        head = 7;
        tail = 7;
    }


    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            Object cur = array[physical(i)];
            if (o == null ? cur == null : o.equals(cur)) {
                return true;
            }
        }
        return false;
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
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                return (E) array[physical(index++)];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (index < 0) {
                    throw new NoSuchElementException();
                }
                return (E) array[physical(index--)];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = array[physical(i)];
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) array[physical(i)];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}