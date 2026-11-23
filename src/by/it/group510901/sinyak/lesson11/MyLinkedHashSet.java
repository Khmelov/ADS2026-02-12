package by.it.group510901.sinyak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E>, Iterable<E> {

    private static class Node<E> {

        private E data;

        private Node<E> left;
        private Node<E> right;

        private Node<E> next; //ссылка на следующий элемент в списке при коллизиях

        Node () {}

        Node (E d) {
            this.data = d;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private double increaseKoef = 2;
    private Node<E> head = null;
    private Node<E> tail = null;


    MyLinkedHashSet() {
        table = new Node[16];
    }

    MyLinkedHashSet(int s) {
        table = new Node[s];
    }

    //Вспомогательные методы

    private int hashFunc(Object key) {
        if(key == null) return 0;

        String str = key.toString();
        int hash = 7057;
        for(var ch : str.toCharArray()) {
            int chCode = (int) ch;
            hash = hash * 41 + chCode;
        }

        return hash;
    }

    private int getIndex(Object o) {
        if (o == null) return 0;
        return (hashFunc(o) & 0x7FFFFFFF) % table.length;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    public class MyIterator implements Iterator<E> {

        private MyLinkedHashSet.Node<E> currNode = head;

        @Override
        public boolean hasNext() {

            if (currNode != null) {
                return true;
            }
            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            Node returnNode = currNode;
            currNode = currNode.right;

            return (E) returnNode.data;
        }
    }

    private void resize() {

        Node[] oldTable = table;
        table = new Node [(int) (oldTable.length * increaseKoef)];

        Node currNode = head;

        while(currNode != null) {

            currNode.next = null;

            int newIndx = getIndex(currNode.data);

            if(table[newIndx] == null) { table[newIndx] = currNode; }
            else {
                currNode.next = table[newIndx];
                table[newIndx] = currNode;
            }
            currNode = currNode.right;
        }
    }

    //Обязательные к реализации методы

    @Override
    public String toString() {
        String res;
        Iterator<E> it = iterator();
        if(!it.hasNext()) {
            return "[]";
        }

        res = "[";
        while (it.hasNext()) {
            res += it.next();
            if (it.hasNext()) res += ", ";
            else {
                res += "]";
            }
        }
        return res;
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
    public boolean contains(Object o) {

        int index = getIndex(o);
        Node currNode = table[index];

        while (currNode != null) {
            if(o == null && currNode.data == null) return true;
            if(o.equals(currNode.data)) return true;
            currNode = currNode.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {

        if (contains(e)) return false;
        if (size >= table.length * 0.75) resize();

        int index = getIndex(e);
        Node newNode = new Node(e);

        newNode.next = table[index];
        table[index] = newNode;

        if(head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.right = newNode;
            newNode.left = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {

        int index = getIndex(o);

        Node currNode = table[index];
        Node prevNode = null;

        while (currNode != null) {
            if (o.equals(currNode.data)) {
                break;
            }
            prevNode = currNode;
            currNode = currNode.next;
        }

        if(currNode == null) return false;

        if (prevNode == null) {
            table[index] = currNode.next;
        } else {
            prevNode.next = currNode.next;
        }

        Node left = currNode.left;
        Node right = currNode.right;

        if(right == null) {
            tail = left;
        }
        else {
            right.left = left;
        }

        if(left == null) {
            head = right;
        }
        else {
            left.right = right;
        }

        currNode.next = null;
        currNode.left = null;
        currNode.right = null;
        currNode.data = null;

        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for(var el : c) {
            if(!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        if(c == null) throw new NullPointerException();

        boolean modified = false;
        for(var el : c) {
            if(add(el)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        if(c == null) throw new NullPointerException();

        boolean retain = false;
        Node currNode = head;

        while (currNode != null) {

            Node<E> nextNode = currNode.right;

            if (!c.contains(currNode.data)) {
                remove(currNode.data);
                retain = true;
            }
            currNode = nextNode;
        }
        return retain;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        if (c == null) throw new NullPointerException();

        boolean modified = false;

        if (size > c.size()) {
            for (Object el : c) {
                if (remove(el)) {
                    modified = true;
                }
            }
        } else {
            Node<E> currNode = head;
            while (currNode != null) {
                Node<E> nextNode = currNode.right;
                if (c.contains(currNode.data)) {
                    remove(currNode.data);
                    modified = true;
                }
                currNode = nextNode;
            }
        }

        return modified;
    }

    @Override
    public void clear() {

        Node currNode = head;
        while(currNode != null) {
            Node<E> nextNode = currNode.right;
            currNode.data = null;
            currNode.next = null;
            currNode.left = null;
            currNode.right = null;
            currNode = nextNode;
        }

        for(int i = 0; i < table.length; i++) table[i] = null;

        head = null;
        tail = null;
        size = 0;
    }

    // Методы которые просто нужно унаследовать но не обязательно реализовывать
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
