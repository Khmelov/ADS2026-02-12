package by.it.group510901.petsevich.lesson10;

import java.security.PrivateKey;
import java.util.*;

/*
toString()
add(Object)
remove(int)
remove(Object)
size()

addFirst(Object)
addLast(Object)

element()
getFirst()
getLast()

poll()
pollFirst()
pollLast()
 */

public class MyLinkedList<E> implements Deque<E>{

    ListNode<E> front, back;
    private int size;

    MyLinkedList() {
        front = back = null;
        size = 0;
    }

    @Override
    public void addFirst(E e) {
        ListNode<E> newNode = new ListNode<E>(e);
        if(size == 0) {
            front = back = newNode;
        }
        else {
            front.previous = newNode;
            newNode.next = front;
            front = front.previous;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        ListNode<E> newNode = new ListNode<E>(e);
        if(size == 0) {
            front = back = newNode;
        }
        else {
            back.next = newNode;
            newNode.previous = back;
            back = back.next;
        }
        size++;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        if(size == 0)
            return null;

        E object = front.value;
        front = front.next;
        if(front != null)
            front.previous = null;
        size--;

        return object;
    }

    @Override
    public E pollLast() {
        if(size == 0)
            return null;

        E object = back.value;
        back = back.previous;
        if(back != null)
            back.next = null;
        size--;

        return object;
    }

    @Override
    public E getFirst() {
        if(size == 0)
            throw new NoSuchElementException();
        return front.value;
    }

    @Override
    public E getLast() {
        if(size == 0)
            throw new NoSuchElementException();
        return back.value;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        E deleted = removeFirst();
        if(deleted == null)
            throw new NoSuchElementException();
        return deleted;
    }

    public E remove(int index) {
        ListNode<E> cur = front;
        int it = 0;
        while (cur != null) {
            if (it == index) {
                if (cur.previous != null)
                    cur.previous.next = cur.next;

                if (cur.next != null)
                    cur.next.previous = cur.previous;
                size--;
                return cur.value;
            }
            it++;
            cur = cur.next;
        }
        return null;
    }


    @Override
    public void clear() {
        ListNode<E> cur = front;
        while (cur != null) {
            cur.previous = null;
            ListNode<E> tmp = cur.next;
            cur.next = null;
            cur = tmp;
        }
        front = back = null;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        ListNode<E> cur = front;
        E elemToDelete = (E)o;
        while(cur != null) {
            if(cur.value == elemToDelete) {
                if(cur.previous != null)
                    cur.previous.next = cur.next;

                if(cur.next != null)
                    cur.next.previous = cur.previous;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        ListNode<E> cur = front;
        while (cur != null)
        {
            builder.append(cur.value);
            if(cur.next != null) builder.append(", ");
            cur = cur.next;
        }
        builder.append(']');
        return builder.toString();
    }
}

class ListNode<E> {
    public E value;
    public ListNode<E> previous;
    public ListNode<E> next;
    ListNode(E value) {
        previous = null;
        next = null;
        this.value = value;
    }
}


