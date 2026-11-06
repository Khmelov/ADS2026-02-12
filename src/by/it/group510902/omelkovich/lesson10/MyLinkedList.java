package by.it.group510902.omelkovich.lesson10;

import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E>{

    private static class Node<E>{
        E value;
        Node<E> prev;
        Node<E> next;
        Node(E value){
            this.value = value;
        }
    }
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for(Node<E> n = head; n != null; n = n.next){
            if(n != head) sb.append(", ");
            sb.append(n.value);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {return size;}

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        Node<E> n = new Node<>(element);
        if(head == null){
            head = tail = n;
        }
        else{
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> n = new Node<>(element);
        if(tail == null){
            head = tail = n;
        }
        else{
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
        size++;
    }

    private E unlink(Node<E> n){
        if(n.prev == null){
            head = n.next;
        }
        else{
            n.prev.next = n.next;
        }
        if(n.next == null){
            tail = n.prev;
        }
        else{
            n.next.prev = n.prev;
        }
        E value = n.value;
        n.value = null;
        n.prev = null;
        n.next = null;
        size--;
        return value;
    }

    @Override
    public E remove() {
        if(head == null) throw new NoSuchElementException();
        return unlink(head);
    }

    public E remove(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> n;
        if(index < size / 2){
            n = head;
            for(int i = 0; i < index; i++){
                n = n.next;
            }
        }
        else{
            n = tail;
            for(int i = size - 1; i > index; i--){
                n = n.prev;
            }
        }
        return unlink(n);
    }

    @Override
    public boolean remove(Object o) {
        for(Node<E> n = head; n != null; n = n.next){
            if(o == null ? n.value == null : o.equals(n.value)){
                unlink(n);
                return true;
            }
        }
        return false;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if(head == null) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if(tail == null) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if(head == null) return null;
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if(tail == null) return null;
        return unlink(tail);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

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
