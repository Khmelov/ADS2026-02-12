package by.it.group551001.kotsuba.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private Node<E> head;
    private Node<E> tail;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
        head = null;
        tail = null;
    }
    private int getIndex(Object o) {
        if (o == null) return 0;
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        Node<E> curr = head;
        while (curr != null) {
            int index = getIndex(curr.data);
            curr.next = table[index];
            table[index] = curr;
            curr = curr.after;
        }
    }
    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }
    private void unlink(Node<E> node) {
        if (node.before != null) {
            node.before.after = node.after;
        } else {
            head = node.after;
        }

        if (node.after != null) {
            node.after.before = node.before;
        } else {
            tail = node.before;
        }

        node.before = null;
        node.after = null;
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
    public void clear(){
        for(int i=0;i<table.length;i++){
            table[i] = null;
        }
        size=0;
        head =null;
        tail = null;
    }
    @Override
    public boolean add(E element){
        int index = getIndex(element);
        Node<E> curr = table[index];
        while(curr!= null){
            if(element==null){
                if(curr.data==null) return false;
            } else if(element.equals(curr.data)) return false;
            curr = curr.next;
        }
        Node<E> newNode = new Node<>(element,table[index]);
        table[index] = newNode;
        linkLast(newNode);
        size++;

        if(size>=table.length*0.75) resize();
        return true;
    }
    @Override
    public boolean remove(Object o){
        int index = getIndex(o);
        Node<E> curr = table[index];
        Node<E> prev = null;
        while(curr!=null){
            boolean matches = (o == null) ? (curr.data == null) : o.equals(curr.data);
            if(matches){
                if(prev==null) table[index] = curr.next;
                else prev.next = curr.next;
                unlink(curr);
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }
    @Override
    public boolean contains(Object o){
        int index = getIndex(o);
        Node<E> curr = table[index];
        while(curr!=null){
            boolean matches = (o == null) ? (curr.data == null) : o.equals(curr.data);
            if(matches) return true;
            curr = curr.next;
        }
        return false;
    }
    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        boolean f=true;
        while(curr!=null){
            if(!f) sb.append(", ");
            sb.append(curr.data);
            f=false;
            curr = curr.after;
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean containsAll(Collection<?> c){
        for(Object e:c){
            if(!contains(e)) return false;
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean f=false;
        for(E e:c){
            if(add(e)) f=true;
        }
        return f;
    }
    @Override
    public boolean removeAll(Collection<?> c){
        boolean f=false;
        for(Object e:c){
            if(remove(e)) f=true;
        }
        return f;
    }
    @Override
    public boolean retainAll(Collection<?> c){
        boolean f=false;
        Node<E> curr = head;
        while(curr!=null){
            Node<E> nextNode = curr.after;
            if(!c.contains(curr.data)){
                remove(curr.data);
                f=true;
            }
            curr = nextNode;
        }
        return f;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}

