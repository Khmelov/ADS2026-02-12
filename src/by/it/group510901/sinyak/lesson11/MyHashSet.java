package by.it.group510901.sinyak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E>, Iterable<E> {

    private Node[] buckets;
    private int size = 0;

    private static class Node<E> {
        private Object Val;
        private Node<E> next = null;
    }

    MyHashSet() {
        buckets = new Node[16];
    }

    MyHashSet(int s) {
        buckets = new Node[s];
    }


    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    public class MyIterator implements Iterator<E> {
        private int bucketInd = 0;
        private Node<E> currNode = null;

        @Override
        public boolean hasNext() {

            if (currNode != null && currNode.next != null) return true;

            for (int i = (currNode == null) ? bucketInd : bucketInd + 1; i < buckets.length; i++) {
                if (buckets[i] != null) return true;
            }

            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            if (currNode != null && currNode.next != null) {
                currNode = currNode.next;
            } else {
                for (int i = (currNode == null) ? bucketInd : bucketInd + 1; i < buckets.length; i++) {
                    if (buckets[i] != null) {
                        bucketInd = i;
                        currNode = buckets[i];
                        break;
                    }
                }
            }

            return (E) currNode.Val;
        }
    }

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

    private int getIndex(Object o) {
        if (o == null) return 0;
        return (hashFunc(o) & 0x7FFFFFFF) % buckets.length;
    }

    private void increaseSize() {
        int incrKoef = 2;

        Node[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * incrKoef];

        for(int i = 0; i < oldBuckets.length; i++) {

            Node<E> currNode = oldBuckets[i];

            while (currNode != null) {

                Node<E> newNode = new Node<E>();
                newNode.Val = currNode.Val;

                int index = getIndex(currNode.Val);

                newNode.next = buckets[index];
                buckets[index] = newNode;

                currNode = currNode.next;
            }
        }
    }


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

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(Object o) {
        if (contains(o)) return false;

        if (size >= buckets.length * 0.75f) {
            increaseSize();
        }

        int index;

        if (o == null) index = 0;
        else {
            index = getIndex(o);
        }

        Node<E> curr = new Node<E>();
        curr.Val = o;
        curr.next = buckets[index];
        buckets[index] = curr;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = (o == null) ? 0 : getIndex(o);

        Node<E> dummy = new Node<E>();
        dummy.next = buckets[index];
        Node<E> curr = dummy;

        while (curr.next != null) {
            boolean isMatch = false;

            if (o == null) {
                if (curr.next.Val == null) {
                    isMatch = true;
                }
            } else {
                if (curr.next.Val != null && curr.next.Val.equals(o)) {
                    isMatch = true;
                }
            }

            if (isMatch) {
                curr.next = curr.next.next;
                buckets[index] = dummy.next;
                size--;
                return true;
            }

            curr = curr.next;
        }

        return false;
    }


    @Override
    public void clear() {

        for (int i = 0; i < buckets.length; i++) {
            Node curr = buckets[i];
            while (curr != null) {

                Node next = curr.next;
                curr.Val = null;
                curr.next = null;

                curr = next;
            }
            buckets[i] = null;
        }
        size = 0;
    }


    @Override
    public boolean contains(Object o) {

        int index = getIndex(o);
        Node<E> curr = buckets[index];

        while (curr != null) {
            if (o == null) {
                if (curr.Val == null) {
                    return true;
                }
            } else {
                if (o.equals(curr.Val)) {
                    return true;
                }
            }
            curr = curr.next;
        }
        return false;
    }

    ////////////////////////////////////////
    // Необязательные методы

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