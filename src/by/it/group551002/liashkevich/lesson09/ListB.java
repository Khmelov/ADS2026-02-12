package by.it.group551002.liashkevich.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {


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

    @Override
    public void add(int index, E element) {
        if ((index < 0) || (index >= size()))
            return;
        Node newNode = new Node(element);
        Node current = head;
        Node prev = null;
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
        Node current = head;
        Node prev = null;
        while (current != null && !o.equals(current.data)) {
            prev = current;
            current = current.next;
        }
        if (current == null)
            return false;
        Node temp = current.next;
        if (prev != null)
            prev.next = temp;
        else
            head = current.next;
        return true;
    }

    @Override
    public E set(int index, E element) {
        Node current = head;
        E toReturn;
        int i = 0;
        while (current != null && i < index) {
            ++i;
            current = current.next;
        }
        toReturn = current != null ? current.data : null;
        if (current != null)
            current.data = element;
        return toReturn;
    }


    @Override
    public boolean isEmpty() {
        return head == null;
    }


    @Override
    public void clear() {
        Node current = head;
        Node next;
        while (current != null) {
            next = current.next;
            current = next;
        }
        head = null;
    }

    @Override
    public int indexOf(Object o) {
        Node current = head;
        int i = 0;
        while (current != null) {
            if (o.equals(current.data))
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
        Node current = head;
        E data = null;
        int i = 0;
        while (current != null) {
            if (i == index)
                data = current.data;
            ++i;
            current = current.next;
        }
        return data;
    }

    @Override
    public boolean contains(Object o) {
        Node current = head;
        while (current != null) {
            if (o.equals(current.data))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node current = head;
        int i = 0;
        int lastIndex = -1;
        while (current != null) {
            if (o.equals(current.data))
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

}
