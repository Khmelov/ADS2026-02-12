package by.it.group551001.kotsuba.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    private int getIndex(Object o) {
        if (o == null) return 0;
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0;

        for (Node<E> head : oldTable) {
            Node<E> curr = head;
            while (curr != null) {
                add(curr.data);
                curr = curr.next;
            }
        }
    }

    @Override
    public int size(){
        return size;
    }
    @Override
    public boolean isEmpty(){
        return size==0;
    }
    @Override
    public void clear(){
        for(int i=0;i<table.length;i++){
            table[i] = null;
        }
        size=0;
    }
    @Override
    public boolean add(E element){
        int index = getIndex(element);
        Node<E> curr = table[index];
        while(curr != null){
            if(element == null){
                if(curr.data == null) return false;
            } else {
                if(element.equals(curr.data)) return false;
            }
            curr = curr.next;
        }
        table[index] = new Node<>(element,table[index]);
        size++;
        if(size >= table.length*0.75) resize();
        return true;
    }
    @Override
    public boolean remove(Object o){
        int index = getIndex(o);
        Node<E> curr = table[index];
        Node<E> prev = null;
        while(curr!=null){
            boolean matches = (o==null)? (curr.data == null) : o.equals(curr.data);
            if(matches){
                if(prev==null){
                    table[index]=curr.next;
                } else prev.next = curr.next;
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
            boolean matches = (o==null)? (curr.data == null) : o.equals(curr.data);
            if(matches) return true;
            curr = curr.next;
        }
        return false;
    }
    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean f = true;
        for(Node<E> head : table){
            Node<E> curr = head;
            while(curr!=null){
                if(!f) sb.append(", ");
                sb.append(curr.data);
                f=false;
                curr = curr.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public Iterator<E> iterator() { return null; }
}

