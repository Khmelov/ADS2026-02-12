package by.it.group510901.kostykovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E>{
            E elem;
            Node<E> next;
            Node<E> prev;

            Node(E elem, Node<E> next, Node<E> prev){
                this.elem = elem;
                this.next = next;
                this.prev = prev;
            }
    }
    private Node<E> head;
    private Node<E> tail;
    private int currentSize;

    @Override
    public String toString(){
        String response = "[";
        if(currentSize == 0){
            return "[]";
        }else {
            Node<E> current = head;
                while(current != null){
                response += current.elem;
                if(current.next != null){
                    response += ", ";
                }
                current = current.next;
            }
            response += "]";
        }
        return response;
    }
    @Override
    public boolean add(E element){
        Node<E> newNode;
        if(currentSize == 0){
            newNode = new Node(element, null, null);
            head = newNode;
            tail = newNode;
            currentSize++;
            return true;
        }
        newNode = new Node(element,null, tail);
        tail.next = newNode;
        tail = newNode;
        currentSize++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    public E remove(int index){
        if(index < 0 || index >= currentSize){
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head;
        for(int i = 0; i < index; i++){
            current = current.next;
        }
        E removed = current.elem;
        if(current.prev != null && current.next != null ) {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            currentSize--;
        }else if(current.prev == null){
             head = current.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
             currentSize--;

        }else if(current.next == null){
            tail = current.prev;
            if(tail != null){
                tail.next = null;
            }else{
                head = null;
            }
            currentSize--;
        }
        return removed;
    }
    @Override
    public boolean remove(Object element){
        Node<E> curr = head;
        int counter = 0;
        while(curr != null){
            if(curr.elem != null && curr.elem.equals(element)) {
                remove(counter);
                return true;
            }
            counter++;
            curr = curr.next;

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
    public int size(){
        return currentSize;
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

    @Override
    public void addFirst(E element){
        if(currentSize == 0){
           Node<E> newNode = new Node(element, null, null);
            head = newNode;
            tail = newNode;
        }else {
           Node<E> newNode = new Node(element, head, null);
            head.prev = newNode;
            head = newNode;
        }
        currentSize++;
    }
    @Override
    public void addLast(E element){
        if(currentSize == 0){
            Node<E> newNode = new Node(element, null, null);
            head = newNode;
            tail = newNode;
        }else{
            Node<E> newNode = new Node(element, null, tail);
            tail.next = newNode;
            tail = newNode;
        }
        currentSize++;
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
    public E element(){
        if(currentSize == 0){
            throw new NoSuchElementException();
        }else {
            return head.elem;
        }
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
    public E getFirst(){
        return element();
    }
    @Override
    public E getLast(){
        if(currentSize == 0){
            throw new NoSuchElementException();
        }else {
            return tail.elem;
        }
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
    public E poll(){
        if(currentSize == 0){
            return null;
        }
        E removed = head.elem;

        head = head.next;
        if(head != null) {
            head.prev = null;
        }else{
            tail = null;
        }
        currentSize--;
        return removed;
    }
    @Override
    public E pollFirst(){
        return poll();
    }
    @Override
    public E pollLast(){
        if(currentSize == 0){
            return null;
        }
        E removed = tail.elem;
        tail = tail.prev;
        if(tail != null){
            tail.next = null;
        }else{
            head = null;
        }
        currentSize--;
        return removed;
    }
}
