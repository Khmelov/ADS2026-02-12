package by.it.group551002.rybik.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E e) {
            this.element = e;
            this.next = null;
        }
    }

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
                Node next = current.next;

                int newIndex = Math.abs(current.element.hashCode() % newCapacity);
                current.next = newSet[newIndex];
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
        for (int i = 0; i < capacity; i++) {
            if ( this.set[i] != null ) {
                sb.append(set[i].element);
                sb.append(", ");
                Node current = set[i].next;
                while (current != null) {
                    sb.append(current.element);
                    sb.append(", ");
                    current = current.next;
                }
            }
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
        } else {
            if(this.set[index].element.equals(e)) return false;

            Node current = this.set[index].next;
            Node prev = this.set[index];
            while (current != null) {

                if(current.element.equals(e)) return false;

                prev = current;
                current = current.next;
            }
            Node newNode = new Node(e);
            prev.next = newNode;
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

                if(prev == null) this.set[index] = current.next;
                else prev.next = current.next;

                current = null;
                this.size--;
                return true;
            }
            prev = current;
            current = current.next;
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
            current = current.next;
        }
        return false;
    }


    /// /
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


}
