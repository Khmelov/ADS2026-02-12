package by.it.group551002.marochkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E>{
        E value;
        Node<E> next;

        Node(E value,Node<E> next){
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size;

    private int indexFor(Object o){
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % table.length;
    }


    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public boolean contains(Object o){
        int i = indexFor(o);

        Node<E> curr = table[i];
        while(curr != null){
            if(o.equals(curr.value)) return true;
            curr = curr.next;
        }
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
    public boolean add(E element){
        if(contains(element)) return false;

        int i = indexFor(element);
        Node<E> newNode = new Node<>(element,table[i]);
        table[i] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o){
        int i = indexFor(o);
        Node<E> curr = table[i];
        Node<E> prev = null;
        while (curr != null){
            if(o.equals(curr.value)){
                if(prev == null){
                    table[i] = curr.next;

                }else {
                    prev.next = curr.next;
                }
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;

        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }


    @Override
    public void clear(){
        for(int i = 0;i < table.length;i++){
            table[i] = null;
        }
        size = 0;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        int count = 0;

        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                if (count > 0) {
                    str.append(", ");
                }
                str.append(curr.value);
                count++;
                curr = curr.next;
            }
        }

        str.append("]");
        return str.toString();
    }





}
