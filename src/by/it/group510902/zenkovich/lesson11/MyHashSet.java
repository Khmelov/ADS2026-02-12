package by.it.group510902.zenkovich.lesson11;


import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {


    class Node <E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next){
            this.value = value;
            this.next = next;
        }
    }


    Node<E>[] table;
    int size;
    int def_Capacity = 10;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[def_Capacity];
        size = 0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public MyHashSet(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        table = (Node<E>[]) new Node[capacity];
        size = 0;
    }

    private int indexFor(Object o) {
        if (o == null) {
            return 0;
        }
        int h = o.hashCode();

        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    private boolean equalsValue(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }


    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length << 1];
        for (Node<E> node : oldTable) {
            while (node != null) {
                Node<E> next = node.next;
                int idx = indexFor(node.value);
                node.next = table[idx];
                table[idx] = node;
                node = next;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int idx = indexFor(e);
        Node<E> current = table[idx];
        while (current != null) {
            if (equalsValue(current.value, e)) {
                return false;
            }
            current = current.next;
        }

        table[idx] = new Node<>(e, table[idx]);
        size++;

        if (size > table.length * 0.75) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o);
        Node<E> current = table[idx];
        Node<E> prev = null;
        while (current != null) {
            if (equalsValue(current.value, o)) {
                if (prev == null) {
                    table[idx] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o);
        Node<E> current = table[idx];
        while (current != null) {
            if (equalsValue(current.value, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    class MyIterator implements Iterator<E> {
        int currentBucket = 0;
        Node<E> currentNode = null;
        Node<E> nextNode = null;

        MyIterator() {
            advance();
        }

        private void advance() {
            if (currentNode != null && currentNode.next != null) {
                nextNode = currentNode.next;
                return;
            }
            while (currentBucket < table.length) {
                if (table[currentBucket] != null) {
                    nextNode = table[currentBucket++];
                    return;
                }
                currentBucket++;
            }
            nextNode = null;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            currentNode = nextNode;
            E val = currentNode.value;
            advance();
            return val;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) {
            result[i++] = e;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            Node<E> prev = null;
            while (current != null) {
                if (!c.contains(current.value)) {
                    if (prev == null) {
                        table[i] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    modified = true;
                } else {
                    prev = current;
                }
                current = current.next;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

}
