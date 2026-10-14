package by.it.group551002.rybik.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E element;
        Node<E> collision;
        Node<E> next;
        Node<E> prev;

        Node(E e) {
            this.element = e;
            this.collision = null;
            this.next = null;
            this.prev = null;

        }
    }

    private Node<E> lastAdded = null;
    private int capacity = 15;
    private int size = 0;

    @SuppressWarnings("unchecked")
    private Node<E>[] set = (Node<E>[]) new Node[this.capacity];

    private void resize() {
        int newCapacity = capacity * 2;
        Node[] newSet = (Node[]) new Node[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Node current = set[i];
            while (current != null) {
                Node next = current.collision;

                int newIndex = Math.abs(current.element.hashCode() % newCapacity);
                current.collision = newSet[newIndex];
                newSet[newIndex] = current;

                current = next;
            }
        }

        set = newSet;
        capacity = newCapacity;
    }



    public String toString(){
        if (this.size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Node current = this.lastAdded;
        while (current.prev != null){
            current = current.prev;
        }
        while (current != null){
            sb.append(current.element);
            sb.append(", ");
            current = current.next;
        }

        sb.setLength(sb.length() - 2);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        Arrays.fill(this.set, null);
        this.size = 0;
        this.lastAdded = null;
    }

    @Override
    public boolean isEmpty() {
        return (this.size == 0) ? true : false;
    }

    @Override
    public boolean add(E e) {

        if (size >= 0.75 * capacity) resize();

        int index = Math.abs(e.hashCode() % this.capacity);
        if(this.set[index] == null){
            Node newNode = new Node(e);
            this.set[index] = newNode;
            
            if(this.lastAdded != null){
                newNode.prev = this.lastAdded;
                this.lastAdded.next = newNode;
            }

            this.lastAdded = newNode;


        } else {
            if(this.set[index].element.equals(e)) return false;

            Node current = this.set[index].collision;
            Node prev = this.set[index];
            while (current != null) {

                if(current.element.equals(e)) return false;

                prev = current;
                current = current.collision;
            }
            Node newNode = new Node(e);
            prev.collision = newNode;

            newNode.prev = this.lastAdded;
            this.lastAdded.next = newNode;
            this.lastAdded = newNode;
        }
        this.size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = Math.abs(o.hashCode() % this.capacity);
        Node current = this.set[index];
        Node prev = null;
        while (current != null){
            if(o.equals(current.element)){

                if(current == this.lastAdded){
                    if(current.prev == null){
                        this.lastAdded = null;
                    } else {
                        this.lastAdded = current.prev;
                        this.lastAdded.next = null;
                    }


                } else {
                    if(current.prev != null){
                        current.prev.next = current.next;
                    }
                    current.next.prev = current.prev;
                }


                if(prev == null) this.set[index] = current.collision;
                else prev.collision = current.collision;

                current = null;
                this.size--;
                return true;
            }
            prev = current;
            current = current.collision;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = Math.abs(o.hashCode() % this.capacity);
        Node current = this.set[index];
        Node prev = null;
        while (current != null){
            if(o.equals(current.element)){
                return true;
            }
            prev = current;
            current = current.collision;
        }
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object element : c){
            if(!this.contains(element)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;
        for(Object element : c){
            if(this.add((E) element)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(this.lastAdded == null) return false;

        boolean res = false;
        Node current = this.lastAdded;
        Node next = current.prev;

        while (next != null){
            if(!c.contains(current.element)) {
                this.remove(current.element);
                res = true;
            }
            current = next;
            next = next.prev;

        }
        if(!c.contains(current.element)) {
            this.remove(current.element);
            res = true;
        }

        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for(Object element : c){
            if (this.remove(element)) res = true;
        }
        return res;
    }

/// /
///
///
///

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
}
