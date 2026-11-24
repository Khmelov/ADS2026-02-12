package by.it.group551002.yermakou.lesson09;

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
        Node newNode = new Node(e);
        if (head == null) {
            head = newNode;
            return true;
        }
        Node current = head;
        while (current.next != null)
            current = current.next;
        current.next = newNode;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size())
            return null;
        Node current = head;
        Node prev = null;
        int i = 0;
        while (i < index) {
            prev = current;
            current = current.next;
            i++;
        }
        E toReturn = current.data;
        if (prev == null)
            head = current.next;
        else
            prev.next = current.next;
        return toReturn;
    }

    @Override
    public int size() {
        int size = 0;
        Node current = head;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size())
            return;
        Node newNode = new Node(element);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            return;
        }
        Node current = head;
        int i = 0;
        while (i < index - 1) {
            current = current.next;
            i++;
        }
        newNode.next = current.next;
        current.next = newNode;
    }

    @Override
    public boolean remove(Object o) {
        Node current = head;
        Node prev = null;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                if (prev == null)
                    head = current.next;
                else
                    prev.next = current.next;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size())
            return null;
        Node current = head;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        E old = current.data;
        current.data = element;
        return old;
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
        Node current = head;
        int i = 0;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data))
                return i;
            current = current.next;
            i++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size())
            return null;
        Node current = head;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.data;
    }

    @Override
    public boolean contains(Object o) {
        Node current = head;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node current = head;
        int i = 0;
        int result = -1;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data))
                result = i;
            current = current.next;
            i++;
        }
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next()))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        Iterator<? extends E> it = c.iterator();
        while (it.hasNext()) {
            add(it.next());
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size())
            return false;
        boolean changed = false;
        int i = index;
        Iterator<? extends E> it = c.iterator();
        while (it.hasNext()) {
            add(i, it.next());
            i++;
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            while (remove(o))
                changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node current = head;
        Node prev = null;
        while (current != null) {
            Node next = current.next;
            if (!c.contains(current.data)) {
                if (prev == null)
                    head = next;
                else
                    prev.next = next;
                changed = true;
            } else {
                prev = current;
            }
            current = next;
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
