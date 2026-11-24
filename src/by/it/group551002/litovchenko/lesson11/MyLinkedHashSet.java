package by.it.group551002.litovchenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E>{
    private static class Node<E> {
        E value;
        MyLinkedHashSet.Node<E> next;
        MyLinkedHashSet.Node<E> prev;
        MyLinkedHashSet.Node<E> nextInOrder;


        Node(E value, MyLinkedHashSet.Node<E> next, MyLinkedHashSet.Node<E> prev, MyLinkedHashSet.Node<E> nextInOrder) {
            this.value = value;
            this.next = next;
            this.prev = prev;
            this.nextInOrder = nextInOrder;
        }
    }

    private void unlinkNode(Node<E> node){
        Node<E> prevNode = node.prev;
        Node<E> nextNode = node.nextInOrder;

        if (prevNode != null) {
            prevNode.nextInOrder = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }

        node.prev = null;
        node.nextInOrder = null;
    }

    private int capacity = 16;
    private int size = 0;
    private Node<E> head, tail;

    @SuppressWarnings("unchecked")
    private MyLinkedHashSet.Node<E>[] elements = (MyLinkedHashSet.Node<E>[]) new MyLinkedHashSet.Node[capacity];

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
        int hash = o == null ? 0 : Math.abs(o.hashCode());
        int row = hash % capacity;
        MyLinkedHashSet.Node<E> current = elements[row];
        while (current != null) {
            if (Objects.equals(current.value, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = e == null ? 0 : Math.abs(e.hashCode());
        int row = hash % capacity;

        MyLinkedHashSet.Node<E> current = elements[row];
        while (current != null) {
            if (Objects.equals(current.value, e)) {
                return false;
            }
            current = current.next;
        }

        elements[row] = new MyLinkedHashSet.Node<>(e, elements[row], tail, null);
        if (size == 0) {
            head = elements[row];
        }else {
            tail.nextInOrder = elements[row];
        }

        tail = elements[row];
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = o == null ? 0 : Math.abs(o.hashCode());
        int row = hash % capacity;
        MyLinkedHashSet.Node<E> current = elements[row];

        if (current == null) {
            return false;
        }

        if (Objects.equals(current.value, o)) {
            elements[row] = current.next;
            unlinkNode(current);
            size--;
            return true;
        }

        while (current.next != null) {
            if (Objects.equals(current.next.value, o)) {
                Node<E> target = current.next;
                unlinkNode(target);
                current.next = target.next;
                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            elements[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        MyLinkedHashSet.Node<E> current = head;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.value);
            first = false;
            current = current.nextInOrder;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c){
            if (!contains(element))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = false;
        for (E element : c){
            if (add(element))
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (size == 0) {
                return false;
            }
            clear();
            return true;
        }

        Collection<E> toRemove = new java.util.ArrayList<>();
        MyLinkedHashSet.Node<E> current = head;
        while (current != null) {
            if (!c.contains(current.value)) {
                toRemove.add(current.value);
            }
            current = current.nextInOrder;
        }
        return removeAll(toRemove);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (Object element : c){
            if (remove((E)element))
                flag = true;
        }
        return flag;
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
}