package by.it.group551004.brovka.lesson10;

public class MyLinkedList<E> implements java.util.Deque<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> pre;

        Node(E data) {
            this.data = data;
        }
    }


    public String toString() {
        String str = "[";
        Node<E> curr = head;
        while (curr != null) {
            str += curr.data;
            curr = curr.next;
            if (curr != null) str += ", ";
        }
        return str + "]";
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> curr = head;
        for (int i = 0; i < index; i++) curr = curr.next;
        if (curr.pre == null) head = curr.next;
        else curr.pre.next = curr.next;
        if (curr.next == null) tail = curr.pre;
        else curr.next.pre = curr.pre;
        size--;
        return curr.data;
    }

    public boolean remove(Object element) {
        Node<E> curr = head;
        while (curr != null) {
            if (element == null ? curr.data == null : element.equals(curr.data)) {
                if (curr.pre == null) head = curr.next;
                else curr.pre.next = curr.next;
                if (curr.next == null) tail = curr.pre;
                else curr.next.pre = curr.pre;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void addFirst(E element) {
        Node<E> node = new Node<>(element);
        node.next = head;
        if (head != null) head.pre = node;
        else tail = node;
        head = node;
        size++;
    }

    public void addLast(E element) {
        Node<E> node = new Node<>(element);
        node.pre = tail;
        if (tail != null) tail.next = node;
        else head = node;
        tail = node;
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        if (head == null) throw new java.util.NoSuchElementException();
        return head.data;
    }

    public E getLast() {
        if (tail == null) throw new java.util.NoSuchElementException();
        return tail.data;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (head == null) return null;
        E data = head.data;
        head = head.next;
        if (head == null) tail = null;
        else head.pre = null;
        size--;
        return data;
    }

    public E pollLast() {
        if (tail == null) return null;
        E data = tail.data;
        tail = tail.pre;
        if (tail == null) head = null;
        else tail.next = null;
        size--;
        return data;
    }

    public E remove() { return null; }
    public E removeFirst() { return null; }
    public E removeLast() { return null; }
    public boolean offer(E e) { return false; }
    public boolean offerFirst(E e) { return false; }
    public boolean offerLast(E e) { return false; }
    public E peek() { return null; }
    public E peekFirst() { return null; }
    public E peekLast() { return null; }
    public void push(E e) { }
    public E pop() { return null; }
    public boolean removeFirstOccurrence(Object e) { return false; }
    public boolean removeLastOccurrence(Object e) { return false; }
    public boolean contains(Object e) { return false; }
    public boolean isEmpty() { return false; }
    public void clear() { }
    public java.util.Iterator<E> iterator() { return null; }
    public java.util.Iterator<E> descendingIterator() { return null; }
    public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    public boolean containsAll(java.util.Collection<?> c) { return false; }
    public boolean removeAll(java.util.Collection<?> c) { return false; }
    public boolean retainAll(java.util.Collection<?> c) { return false; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}
