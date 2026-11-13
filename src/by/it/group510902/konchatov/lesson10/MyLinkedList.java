package by.it.group510902.konchatov.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements List<E>, Deque<E> {
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;  
    private Node<E> tail;  
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    private Node<E> node(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur=head;
        for (int i = 0; i < index; i++) {
        cur = cur.next;
    }
        return cur;
    }

    private void del(Node<E> n) {
        Node<E> p = n.prev;
        Node<E> q = n.next;
        if (p == null) head = q;
        else p.next = q;
        if (q == null) tail = p; 
        else q.prev = p;
        n.prev = n.next = null;
        n.value = null;
        size--;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
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
        Node<E> n = new Node<>(element);
        if (head == null) {
            head = tail = n;
        } else {
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> n = new Node<>(element);
        if (tail == null) {
            head = tail = n;
        } else {
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E v = head.value;
        del(head);
        return v;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E v = tail.value;
        del(tail);
        return v;
    }

    @Override
    public E remove(int index) {
        Node<E> n = node(index);
        E v = n.value;
        del(n);
        return v;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                del(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }
    @Override public MyLinkedList<E> reversed() {return null;}
    @Override public boolean offer(E e) { return false; }
    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peek() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean contains(Object o) { return false; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { }
    @Override public void push(E e) { }
    @Override public E pop() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public Iterator<E> descendingIterator() { return null; }

    @Override public E get(int index) { return null; }
    @Override public E set(int index, E element) { return null; }
    @Override public void add(int index, E element) { }
    @Override public int indexOf(Object o) { return -1; }
    @Override public int lastIndexOf(Object o) { return -1; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
}