package by.it.group551002.rybik.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {


    private class Node {
        E data;
        Node next;
        Node prev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
        Node(){
            this.data = null;
            this.next = null;
            this.prev = null;
        }
    }
    private int size = 0;
    private Node head = null;
    private Node tail = null;




    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        Node current = this.head;

        while(current != null){
            sb.append(current.data);
            sb.append(", ");
            current = current.next;
        }
        sb.setLength(sb.length() - 2);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {

        Node newNode = new Node(e);
        newNode.next = null;
        newNode.prev = this.tail;

        if(this.head == null){
            this.head = newNode;
            this.tail = newNode;
        }


        this.tail.next = newNode;
        this.tail = newNode;

        this.size++;

        return true;
    }

    public E remove(int index) {
        if(index == 0) return getFirst();
        if(index == size - 1) return getLast();

        if( !(index > 0 && index < size) ) return null;

        Node current;

        boolean leftOrRight = ( index < (size / 2));
        int i;

        if(leftOrRight){
            i = 0;
            current = this.head;

            while( i != index){
                i++;
                current = current.next;
            }

        } else {
            i = size - 1;
            current = this.tail;

            while( i != index){
                i--;
                current = current.prev;
            }

        }
        current.prev.next = current.next;
        current.next.prev = current.prev;

        this.size--;
        return current.data;
    }

    @Override
    public boolean remove(Object o) {
        Node current = new Node();
        current = this.head;

        while(current != null){
            if(current.data == o){
                current.prev.next = current.next;
                current.next.prev = current.prev;
                current = null;
                this.size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void addFirst(E e) {
        Node newNode = new Node(e);
        newNode.next = this.head;
        newNode.prev = null;

        if(this.head == null){
            this.head = newNode;
            this.tail = newNode;
        }


        this.head.prev = newNode;

        this.head = newNode;

        this.size++;
    }

    @Override
    public void addLast(E e) {
        this.add(e);
    }

    @Override
    public E element() {
        if(this.head == null)
            throw new NoSuchElementException();
        else
            return this.head.data;
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if(this.tail == null)
            throw new NoSuchElementException();
        else
            return this.tail.data;
    }

    @Override
    public E pollFirst() {
        if(this.head == null)
            throw new NoSuchElementException();
        else {
            E res = this.head.data;
            this.head = this.head.next;
            this.head.prev = null;



            this.size--;
            return res;
        }
    }

    @Override
    public E pollLast() {
        if(this.tail == null)
            throw new NoSuchElementException();
        else {
            E res = this.tail.data;
            this.tail = this.tail.prev;
            this.tail.next = null;


            this.size--;
            return res;
        }
    }

    @Override
    public E poll() {
        return pollFirst();
    }


    @Override
    public E remove() {
        return null;
    }

/// ///////////////


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
    public boolean offer(E e) {
        return false;
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
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
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
    public boolean isEmpty() {
        return false;
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
}
