package by.it.group551002.marochkov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E>{
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

    private E unlink(Node<E> node) {
        E result = node.value;

        if(node.prev != null){
            node.prev.next = node.next;
        }else {
            head = node.next;
        }

        if(node.next != null){
            node.next.prev = node.prev;
        }else {
            tail = node.prev;
        }

        size--;
        return result;
    }

    private Node<E> getNode(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> curr;
        if(index > size /2){
            curr = tail;
            for(int i = size - 1;i > index;i--){
                curr = curr.prev;
            }
        }else {
            curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        }
        return curr;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        Node<E> curr = head;
        for(int i = 0;i < size; i++){
            str.append(curr.value);
            if(i < size - 1){
                str.append(", ");
            }
            curr = curr.next;

        }

        str.append("]");
        return str.toString();
    }

    @Override
    public int size(){
        return size;
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
        Node<E> newElement = new Node<E>(element);

        if(head == null){
            head = tail = newElement;
        } else {
            newElement.next = head;
            head.prev = newElement;
            head = newElement;
        }

        size++;
    }

    @Override
    public void addLast(E element){
        Node<E> newElement = new Node<E>(element);
        if(head == null){
            head = tail = newElement;
        } else {
            newElement.prev = tail;
            tail.next = newElement;
            tail = newElement;
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
    public boolean add(E element){
        addLast(element);
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

    @Override
    public E getFirst() {
        if(head == null) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if(head == null) throw new NoSuchElementException();
        return tail.value;
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
    public E element(){
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
    public E pollFirst(){
        if(head == null){
            return null;
        }else {
            return unlink(head);
        }
    }

    @Override
    public E pollLast(){
        if(tail == null){
            return null;
        }else {
            return unlink(tail);
        }
    }

    @Override
    public E poll(){
        return pollFirst();
    }


    public E remove(int index){
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return unlink(getNode(index));
    }

    @Override
    public boolean remove(Object o){
        Node<E> curr = head;

        for(int i = 0;i < size;i++){
            if(o == null ? curr.value == null : o.equals(curr.value)){
                unlink(curr);
                return true;
            }
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
}
