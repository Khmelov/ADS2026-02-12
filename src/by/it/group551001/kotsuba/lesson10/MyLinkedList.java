package by.it.group551001.kotsuba.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }
    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = first;
        while(curr != null){
            sb.append(curr.data);
            if(curr.next != null) sb.append(", ");
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E element){
        addLast(element);
        return true;
    }
    @Override
    public void addFirst(E element){
        Node<E> f = first;
        Node<E> newNode = new Node<>(element,null,f);
        first = newNode;
        if(f==null) last = newNode;
        else f.prev = newNode;
        size++;
    }
    @Override
    public void addLast(E element){
        Node<E> l = last;
        Node<E> newNode = new Node<>(element,last,null);
        last = newNode;
        if(l==null) first = newNode;
        else l.next = newNode;
        size++;
    }
    @Override
    public E element (){
        return getFirst();
    }
    @Override
    public E getFirst(){
        if(first==null) return null;
        return first.data;
    }
    @Override
    public E getLast(){
        if(last==null) return null;
        return last.data;
    }
    @Override
    public E poll(){
        return pollFirst();
    }
    @Override
    public E pollFirst(){
        if(first==null) return null;
        E element = first.data;
        Node<E> next = first.next;
        first.data = null;
        first.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return element;
    }
    @Override
    public E pollLast(){
        if(last==null) return null;
        E element = last.data;
        Node<E> prev = last.prev;
        last.data = null;
        last.prev = null;
        last = prev;
        if(prev==null) first=null;
        else prev.next = null;
        size--;
        return element;
    }

    public E remove(int index){
        if(index<0 || index>=size) return null;
        Node<E> curr;
        if(index<size/2){
            curr = first;
            for(int i=0;i<index;i++){
                curr = curr.next;
            }
        } else{
            curr = last;
            for(int i = size-1;i> index;i--){
                curr = curr.prev;
            }
        }
        return unlink(curr);
    }
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.data)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    private E unlink(Node<E> x) {
        E element = x.data;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
        return element;
    }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { return pollFirst(); }
    @Override public E removeLast() { return pollLast(); }
    @Override public E peekFirst() { return getFirst(); }
    @Override public E peekLast() { return getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }
    @Override public boolean contains(Object o) { return false; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
}

