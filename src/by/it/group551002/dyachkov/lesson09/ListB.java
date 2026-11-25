package by.it.group551002.dyachkov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    static class Node<E> {
        E data;
        Node<E> next;

        Node(E object) {
            data = object;
            next = null;
        }
    }

    Node<E> head = null;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "[";
        Node<E> current = head;
        while (current != null) {
            result += String.valueOf(current.data);
            if (current.next != null)
                result += ", ";
            current = current.next;
        }
        result += "]";
        return result;
    }

    @Override
    public boolean add(E e) {
        if (head == null) {
            head = new Node<>(e);
            return true;
        }
        Node<E> newNode = new Node<>(e);
        Node<E> current = head;
        while (current.next != null)
            current = current.next;
        current.next = newNode;
        return true;
    }

    @Override
    public E remove(int index) {
        if ((index < 0) || (index >= size()))
            return null;
        Node<E> current = head;
        Node<E> prev = null;
        int i = 0;
        while (current != null && i < index) {
            prev = current;
            current = current.next;
            i++;
        }
        E toReturn = current.data;
        if (prev != null)
            prev.next = current.next;
        else
            head = current.next;
        return toReturn;
    }

    @Override
    public int size() {
        int size = 0;
        Node<E> current = head;
        while (current != null) {
            ++size;
            current = current.next;
        }
        return size;
    }

    @Override
    public void add(int index, E element) {
        if ((index < 0) || (index > size()))
            return;
        Node<E> newNode = new Node<>(element);
        Node<E> current = head;
        Node<E> prev = null;
        int i = 0;
        while (current != null && i < index) {
            prev = current;
            ++i;
            current = current.next;
        }
        newNode.next = current;
        if (prev != null)
            prev.next = newNode;
        else
            head = newNode;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        Node<E> prev = null;
        while (current != null && !equalsValue(current.data, o)) {
            prev = current;
            current = current.next;
        }
        if (current == null)
            return false;
        if (prev != null)
            prev.next = current.next;
        else
            head = current.next;
        return true;
    }

    @Override
    public E set(int index, E element) {
        Node<E> current = head;
        int i = 0;
        while (current != null && i < index) {
            ++i;
            current = current.next;
        }
        if (current == null)
            return null;
        E toReturn = current.data;
        current.data = element;
        return toReturn;
    }


    @Override
    public boolean isEmpty() {
        return head == null;
    }


    @Override
    public void clear() {
        head = null;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            if (equalsValue(current.data, o))
                return i;
            ++i;
            current = current.next;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if ((index < 0) || (index >= size()))
            return null;
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            if (i == index)
                return current.data;
            ++i;
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = head;
        int i = 0;
        int lastIndex = -1;
        while (current != null) {
            if (equalsValue(current.data, o))
                lastIndex = i;
            ++i;
            current = current.next;
        }
        return lastIndex;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
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
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    private static boolean equalsValue(Object stored, Object target) {
        return stored == null ? target == null : stored.equals(target);
    }

}