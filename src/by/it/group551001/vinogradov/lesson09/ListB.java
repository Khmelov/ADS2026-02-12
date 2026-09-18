package by.it.group551001.vinogradov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<E> head = new Node<E>(null);

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String s = "[";
        Node<E> curr = head.next;
        if (curr != null) {
            s += curr.value;
            curr = curr.next;
            while (curr != null) {
                s += ", ";
                s += curr.value;
                curr = curr.next;
            }
        }
        s += "]";
        return s;
    }

    @Override
    public boolean add(E e) {
        Node<E> curr = head;
        while (curr.next != null) curr = curr.next;
        curr.next = new Node<E>(e);
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        int idx = 0;
        Node<E> curr = head;
        E rem = null;
        while(curr.next != null) {
            if (idx == index) {
                Node<E> next = curr.next;
                rem = next.value;
                curr.next = next.next;
                break;
            }
            curr = curr.next;
            idx++;
        }
        return rem;
    }

    @Override
    public int size() {
        Node<E> curr = head;
        int idx = 0;
        while(curr.next != null) {
            curr = curr.next;
            idx++;
        }
        return idx;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> curr = head;
        Node<E> n = new Node<E>(element);
        int idx = 0;
        while (curr != null) {
            if (index == idx) {
                Node<E> next = curr.next;
                if (next != null) {
                    n.next = next;
                }
                curr.next = n;
                break;
            }
            idx++;
            curr = curr.next;
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean f = false;
        Node<E> curr = head;
        while(curr.next != null) {
            if (Objects.equals(o, curr.next.value)) {
                Node<E> next = curr.next;
                curr.next = next.next;
                f = true;
                break;
            }
            curr = curr.next;
        }
        return f;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        int idx = 0;
        Node<E> curr = head;
        E val = null;
        while(curr.next != null) {
            if (idx == index) {
                Node<E> next = curr.next;
                val = next.value;
                next.value = element;
                break;
            }
            curr = curr.next;
            idx++;
        }
        return val;
    }


    @Override
    public boolean isEmpty() {
        return (head.next == null);
    }


    @Override
    public void clear() {
        head.next = null;
    }

    @Override
    public int indexOf(Object o) {
        int idx = 0;
        Node<E> curr = head;
        while (curr.next != null) {
            if (Objects.equals(o, curr.next.value)) {
                return idx;
            }
            idx++;
            curr = curr.next;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> curr = head;
        int idx = 0;
        while(curr.next != null) {
            if (idx == index) {
                return curr.next.value;
            }
            idx++;
            curr = curr.next;
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return (indexOf(o) > -1);
    }

    @Override
    public int lastIndexOf(Object o) {
        int idx = 0;
        int l_idx = -1;
        Node<E> curr = head;
        while (curr.next != null) {
            if (Objects.equals(o, curr.next.value)) {
                l_idx = idx;
            }
            idx++;
            curr = curr.next;
        }
        return l_idx;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Node<E> curr = head;
        while (curr.next != null) curr = curr.next;
        for (E element : c) {
            curr.next = new Node<E>(element);
            curr = curr.next;
        }
        return (!c.isEmpty());
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> curr = head;
        int idx = 0;
        boolean f = false;
        while (curr != null) {
            if (index == idx) {
                if (!c.isEmpty()) {
                    f = true;
                    Node<E> next = curr.next;
                    for (E element: c) {
                        Node<E> n = new Node<E>(element);
                        curr.next = n;
                        curr = curr.next;
                    }
                    if (next != null) {
                        curr.next = next;
                    }
                }
                break;
            }
            idx++;
            curr = curr.next;
        }
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean f = false;
        Node<E> curr = head;

        while (curr.next != null) {
            if (c.contains(curr.next.value)) {
                curr.next = curr.next.next;
                f = true;
            } else {
                curr = curr.next;
            }
        }

        return f;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean f = false;
        Node<E> curr = head;

        while (curr.next != null) {
            if (!c.contains(curr.next.value)) {
                curr.next = curr.next.next;
                f = true;
            } else {
                curr = curr.next;
            }
        }

        return f;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size()) {
            throw new IndexOutOfBoundsException();
        }

        if (fromIndex > toIndex) {
            throw new IllegalArgumentException();
        }
        ListB<E> l = new ListB<>();
        l.head = new Node<E>(null);
        Node<E> curr = head;
        Node<E> lcurr = l.head;
        int idx = -1;
        while (curr.next != null) {
            idx++;
            if (idx < fromIndex) {
                curr = curr.next;
                continue;
            }
            if (idx == toIndex) break;
            lcurr.next = new Node<E>(curr.next.value);
            lcurr = lcurr.next;
            curr = curr.next;
        }
        return l;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        int size = size();
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        return new ListIterator<E>() {
            private int cursor = index;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor < size();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                E value = ListB.this.get(cursor);
                lastReturned = cursor;
                cursor++;

                return value;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }

                cursor--;
                lastReturned = cursor;

                return ListB.this.get(cursor);
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
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }

                ListB.this.remove(lastReturned);

                if (lastReturned < cursor) {
                    cursor--;
                }

                lastReturned = -1;
            }

            @Override
            public void set(E element) {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }

                ListB.this.set(lastReturned, element);
            }

            @Override
            public void add(E element) {
                ListB.this.add(cursor, element);
                cursor++;
                lastReturned = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size) {
            array = (T[]) java.lang.reflect.Array.newInstance(
                    array.getClass().getComponentType(),
                    size
            );
        }
        Node<E> curr = head.next;

        for (int i = 0; i < size; i++) {
            array[i] = (T) curr.value;
            curr = curr.next;
        }
        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        Node<E> curr = head.next;

        for (int i = 0; i < size; i++) {
            result[i] = curr.value;
            curr = curr.next;
        }

        return result;
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

}
