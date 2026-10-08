package by.it.group551001.vinogradov.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;
        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> first = new Node<E>(null);
    private Node<E> last = new Node<E>(null);
    private int size = 0;

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    public MyLinkedList() {
        first.next = last;
        last.prev = first;
    }

    @Override
    public String toString() {
        String s = "[";
        Node<E> curr = first.next;
        while(curr != last) {
            if (curr != first.next) s += ", ";
            s += curr.value;
            curr = curr.next;
        }
        return s + "]";
    }

    @Override
    public boolean add(E element) {
        Node<E> llast = last.prev;
        llast.next = new Node<E>(element);
        llast.next.prev = llast;
        llast.next.next = last;
        last.prev = llast.next;
        size++;
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> curr = first.next;
        int idx = 0;
        while(curr != last) {
           if (idx == index) {
               Node<E> llast = curr.prev;
               llast.next=curr.next;
               curr.next.prev = llast;
               size--;
               return curr.value;
           }
           curr=curr.next;
           idx++;
        }
        return null;
    }

    @Override
    public boolean remove(Object element) {
        Node<E> curr = first.next;
        while(curr != last) {
            if (Objects.equals(element, curr.value)) {
                Node<E> llast = curr.prev;
                llast.next=curr.next;
                curr.next.prev = llast;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> ffirst = first.next;
        ffirst.prev = new Node<E>(element);
        ffirst.prev.next = ffirst;
        ffirst.prev.prev = first;
        first.next = ffirst.prev;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> llast = last.prev;
        llast.next = new Node<E>(element);
        llast.next.prev = llast;
        llast.next.next = last;
        last.prev = llast.next;
        size++;
    }

    @Override
    public E element() {
        if (first.next == last) throw new NoSuchElementException();
        return first.next.value;
    }

    @Override
    public E getFirst() {
        if (first.next == last) throw new NoSuchElementException();
        return first.next.value;
    }

    @Override
    public E getLast() {
        if (first.next == last) throw new NoSuchElementException();
        return last.prev.value;
    }

    @Override
    public E poll() {
        if (first.next == last) return null;
        Node<E> ffirst = first.next;
        ffirst.next.prev = first;
        first.next = ffirst.next;
        size--;
        return ffirst.value;
    }

    @Override
    public E pollFirst() {
        if (first.next == last) return null;
        Node<E> ffirst = first.next;
        ffirst.next.prev = first;
        first.next = ffirst.next;
        size--;
        return ffirst.value;
    }

    @Override
    public E pollLast() {
        if (first.next == last) return null;
        Node<E> llast = last.prev;
        llast.prev.next = last;
        last.prev = llast.prev;
        size--;
        return llast.value;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E element) {
        throw notImplemented();
    }

    @Override
    public boolean offerLast(E element) {
        throw notImplemented();
    }

    @Override
    public E removeFirst() {
        throw notImplemented();
    }

    @Override
    public E removeLast() {
        throw notImplemented();
    }

    @Override
    public E peekFirst() {
        throw notImplemented();
    }

    @Override
    public E peekLast() {
        throw notImplemented();
    }

    @Override
    public boolean removeFirstOccurrence(Object object) {
        throw notImplemented();
    }

    @Override
    public boolean removeLastOccurrence(Object object) {
        throw notImplemented();
    }

    @Override
    public boolean offer(E element) {
        throw notImplemented();
    }

    @Override
    public E remove() {
        throw notImplemented();
    }

    @Override
    public E peek() {
        throw notImplemented();
    }

    @Override
    public void push(E element) {
        throw notImplemented();
    }

    @Override
    public E pop() {
        throw notImplemented();
    }

    @Override
    public boolean contains(Object object) {
        throw notImplemented();
    }

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw notImplemented();
    }

    @Override
    public boolean isEmpty() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw notImplemented();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw notImplemented();
    }

    @Override
    public void clear() {
        throw notImplemented();
    }
}
