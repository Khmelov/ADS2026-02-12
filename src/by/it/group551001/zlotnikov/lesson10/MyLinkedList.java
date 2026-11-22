package by.it.group551001.zlotnikov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;
        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private void unlink(Node<E> x) {
        if (x.prev != null) {
            x.prev.next = x.next;
        } else {
            head = x.next;
        }
        if (x.next != null) {
            x.next.prev = x.prev;
        } else {
            tail = x.prev;
        }
        x.data = null;
        x.prev = null;
        x.next = null;
        size--;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "[";
        Node<E> x = head;
        while (x != null) {
            result += x.data;
            if (x.next != null) {
                result += ", ";
            }
            x = x.next;
        }
        return result + "]";
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> x = head;
        for (int i = 0; i < index; i++) {
            x = x.next;
        }
        E old = x.data;
        unlink(x);
        return old;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Deque is empty");
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Deque is empty");
        }
        return tail.data;
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
        E result = head.data;
        unlink(head);
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E result = tail.data;
        unlink(tail);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Deque / Queue                ///////
    /////////////////////////////////////////////////////////////////////////

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
        E result = pollFirst();
        if (result == null) {
            throw new IndexOutOfBoundsException("Deque is empty");
        }
        return result;
    }

    @Override
    public E removeLast() {
        E result = pollLast();
        if (result == null) {
            throw new IndexOutOfBoundsException("Deque is empty");
        }
        return result;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : head.data;
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : tail.data;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Node<E> x = head;
        while (x != null) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                unlink(x);
                return true;
            }
            x = x.next;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> x = tail;
        while (x != null) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                unlink(x);
                return true;
            }
            x = x.prev;
        }
        return false;
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
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean contains(Object o) {
        Node<E> x = head;
        while (x != null) {
            if (o == null ? x.data == null : o.equals(x.data)) {
                return true;
            }
            x = x.next;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Методы Collection                             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> x = head;
        int i = 0;
        while (x != null) {
            result[i++] = x.data;
            x = x.next;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < size) {
            throw new UnsupportedOperationException("Array too small");
        }
        Node<E> x = head;
        int i = 0;
        while (x != null) {
            a[i++] = (T) x.data;
            x = x.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            if (c.contains(x.data)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            if (!c.contains(x.data)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<E> x = head;
        while (x != null) {
            Node<E> next = x.next;
            x.data = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        head = null;
        tail = null;
        size = 0;
    }
}