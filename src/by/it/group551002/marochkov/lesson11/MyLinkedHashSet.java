package by.it.group551002.marochkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size;
    private Node<E> first;
    private Node<E> last;

    private int indexFor(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % table.length;
    }

    private void linkedLast(Node<E> n){
        if(last == null){
            first = last =n;
            return;
        }
        n.before = last;
        last.after = n;
        last = n;
    }

    private void unlinkOrder(Node<E> n){
        if(n.before != null){
            n.before.after = n.after;

        } else {
            first = n.after;

        }
        if(n.after != null){
            n.after.before = n.before;

        }else{
            last = n.before;
        }

    }

    private boolean same(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
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
    public boolean contains(Object o) {
        Node<E> curr = table[indexFor(o)];
        while (curr != null) {
            if (same(o, curr.value)) return true;
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
    public boolean add(E element) {
        if (contains(element)) return false;

        int i = indexFor(element);
        Node<E> n = new Node<>(element, table[i]);
        table[i] = n;
        linkedLast(n);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexFor(o);
        Node<E> curr = table[i];
        Node<E> prev = null;

        while (curr != null) {
            if (same(o, curr.value)) {

                if (prev == null) {
                    table[i] = curr.next;
                } else {
                    prev.next = curr.next;
                }

                unlinkOrder(curr);
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node<E> curr = first;
        while (curr != null) {
            str.append(curr.value);
            if (curr.after != null) {
                str.append(", ");
            }
            curr = curr.after;
        }
        str.append("]");
        return str.toString();
    }



    @Override
    public boolean containsAll(Collection<?> c){
        for(Object x : c){
            if(!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean changed = false;
        for (E x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c){
        boolean changed = false;
        Node<E> curr = first;
        while (curr != null) {
            Node<E> nextNode = curr.after;
            if (c.contains(curr.value)) {
                remove(curr.value);
                changed = true;
            }
            curr = nextNode;
        }
        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> curr = first;
        while (curr != null) {
            Node<E> nextNode = curr.after;   // запомнить ДО удаления
            if (!c.contains(curr.value)) {
                remove(curr.value);
                changed = true;
            }
            curr = nextNode;
        }
        return changed;
    }























}
