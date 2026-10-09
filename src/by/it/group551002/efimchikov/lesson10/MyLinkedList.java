package by.it.group551002.efimchikov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        this.head=null;
        this.tail=null;
        this.size=0;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> iter=head;
        for(int i=0;i<size;i++){
            if(i>0) sb.append(", ");
            sb.append(iter.value.toString());
            iter=iter.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size(){
        return this.size;
    }

    @Override
    public boolean add(E element){
        Node<E> newNode = new Node<>(element);
        if(tail!=null)
            tail.next=newNode;
        newNode.prev=tail;
        tail=newNode;
        size++;
        if(head==null)
            head=tail;
        return true;
    }

    private E unlink(Node<E> node){
        E value=node.value;
        Node<E>p=node.prev;
        Node<E>n=node.next;
        if(p==null)head=n;else{p.next=n;node.prev=null;}
        if(n==null)tail=p;else{n.prev=p;node.next=null;}

        node.value=null;
        size--;
        return value;
    }

    @Override
    public boolean remove(Object element){
        Node<E> iter=head;
        while(iter!=null){
            if(element==null ? iter.value==null : element.equals(iter.value)) {
                unlink(iter);
                return true;
            }
            iter=iter.next;
        }
        return false;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);

        Node<E> cur = head;
        for (int j = 0; j < index; j++) cur = cur.next;
        return unlink(cur);
    }

    @Override
    public void addLast(E element){
        add(element);
    }

    @Override
    public void addFirst(E element){
        Node<E> newNode= new Node<E>(element);
        if(size==0){
            head=tail=newNode;
        }else {
            newNode.next = head;
            head.prev=newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public E element(){
        return head.value;
    }

    @Override
    public E getFirst(){
        return head.value;
    }

    @Override
    public E getLast(){
        return tail.value;
    }

    @Override
    public E poll(){
        if(size==0) return null;
        E out=head.value;
        head=head.next;
        head.prev=null;
        size--;
        return out;
    }

    @Override
    public E pollFirst(){
        return poll();
    }

    @Override
    public E pollLast(){
        if(size==0) return null;
        E out=tail.value;
        tail=tail.prev;
        tail.next=null;
        size--;
        return out;
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
    public E remove() {
        return null;
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
    public void clear() {}

    @Override
    public void push(E e) {}

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
