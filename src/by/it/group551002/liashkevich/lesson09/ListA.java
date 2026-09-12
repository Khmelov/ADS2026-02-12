package by.it.group551002.liashkevich.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    class Node {
        E data;
        Node next;

        Node(E object) {
            data = object;
            next = null;
        }
    }

    Node head = null;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "[";
        Node current = head;
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
            head = new Node(e);
            return true;
        }
        Node newNode = new Node(e);
        Node current = head;
        while (current.next != null)
            current = current.next;
        current.next = newNode;
        return true;
    }

    @Override
    public E remove(int index) {
        if ((index < 0) || (index >= size()))
            return null;
        if (head == null)
            return null;
        Node current = head;
        Node prev = null;
        E toReturn;
        int i = 0;
        while (current != null && i < index) {
            prev = current;
            current = current.next;
            i++;
        }
        Node temp = current.next;
        toReturn = current.data;
        if (prev != null)
            prev.next = temp;
        else
            head = current.next;
        return toReturn;
    }

    @Override
    public int size() {
        int size = 0;
        Node current = head;
        while (current != null) {
            ++size;
            current = current.next;
        }
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

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

}
