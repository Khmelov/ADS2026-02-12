package by.it.group551002.liashkevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

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
    public int size() {
        int size = 0;
        for (E element : this)
            ++size;
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
        int i = 0;
        for (E element : this) {
            if (element.equals(o))
                return i;
            ++i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if ((index < 0) || (index >= size()))
            return null;
        E data = null;
        int i = 0;
        for (E element : this) {
            if (i == index)
                data = element;
            ++i;
        }
        return data;
    }

    @Override
    public boolean contains(Object o) {
        for (E element : this) {
            if (element.equals(o))
                return true;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node current = head;
        int i = 0;
        int lastIndex = -1;
        for (E element : this) {
            if (element.equals(o))
                lastIndex = i;
            ++i;
        }
        return lastIndex;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Node current = head;
        Node prev = null;
        while (current != null) {
            prev = current;
            current = current.next;
        }
        for (E element : c) {
            if (prev == null) {
                head = new Node(element);
                prev = head;
            }
            else
                prev.next = new Node(element);
            prev = prev.next;
        }
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if ((index < 0) || (index > size()))
            return false;
        Node current = head;
        Node prev = null;
        int i = 0;
        while (current != null && i < index) {
            prev = current;
            ++i;
            current = current.next;
        }
        for (E element : c) {
            Node newNode = new Node(element);
            if (prev != null) {
                newNode.next = prev.next;
                prev.next = newNode;
                prev = newNode;
            }
            else {
                newNode.next = head;
                head = newNode;
                prev = head;
            }
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Node current = head;
        Node prev = null;
        boolean changed = false;
        while (current != null) {
            if (!c.contains(current.data)) {
                prev = current;
            }
            else {
                if (prev == null)
                    head = current.next;
                else
                    prev.next = current.next;
                changed = true;
            }
            current = current.next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Node current = head;
        Node prev = null;
        boolean changed = false;
        while (current != null) {
            if (c.contains(current.data)) {
                prev = current;
            }
            else {
                if (prev == null)
                    head = current.next;
                else
                    prev.next = current.next;
                changed = true;
            }
            current = current.next;
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        /*if ((fromIndex < 0) || (toIndex > size()))
            throw new IndexOutOfBoundsException();
        if ((fromIndex > toIndex))
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        if (fromIndex == toIndex)
            return null;
        ListC<E> result = new ListC<E>();
        Node current = head;
        int i = 0;
        while (current != null && i < fromIndex) {
            ++i;
            current = current.next;
        }
        while (current != null && i < toIndex) {
            result.add(current.data);
            ++i;
            current = current.next;
        }
        return result;*/
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
    ////////, но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            Node current = head;
            public boolean hasNext() {
                return current != null;
            }
            public E next() {
                E toReturn = current.data;
                current = current.next;
                return toReturn;
            }
        }
        return new Iter();
    }

}
