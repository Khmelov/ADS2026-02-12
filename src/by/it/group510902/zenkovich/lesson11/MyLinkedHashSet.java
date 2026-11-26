package by.it.group510902.zenkovich.lesson11;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {


    class Node<E> {
        E value;
        int hash;
        Node<E> nextInBucket;
        Node<E> nextInOrder;

        Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }


    Node<E>[] table;
    int size;
    int def_Capacity = 16;


    Node<E> head;
    Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[def_Capacity];
        size = 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash) {
        return hash & (table.length - 1);
    }


    private boolean equalsValue(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }


    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length << 1];

        for (Node<E> node : oldTable) {
            while (node != null) {
                Node<E> next = node.nextInBucket;
                int idx = indexFor(node.hash);
                node.nextInBucket = table[idx];
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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int idx = indexFor(hash);
        Node<E> current = table[idx];

        while (current != null) {
            if (current.hash == hash && equalsValue(current.value, e)) {
                return false;
            }
            current = current.nextInBucket;
        }


        Node<E> newNode = new Node<>(e, hash);


        newNode.nextInBucket = table[idx];
        table[idx] = newNode;


        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextInOrder = newNode;
            tail = newNode;
        }

        size++;
        if (size > table.length * 0.75) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int idx = indexFor(hash);
        Node<E> current = table[idx];
        Node<E> prevBucketNode = null;
        Node<E> targetNode = null;

        while (current != null) {
            if (current.hash == hash && equalsValue(current.value, o)) {
                targetNode = current;

                if (prevBucketNode == null) {
                    table[idx] = current.nextInBucket;
                } else {
                    prevBucketNode.nextInBucket = current.nextInBucket;
                }
                break;
            }
            prevBucketNode = current;
            current = current.nextInBucket;
        }

        if (targetNode == null) {
            return false;
        }


        if (head == targetNode) {
            head = head.nextInOrder;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<E> prevOrderNode = head;
            while (prevOrderNode != null && prevOrderNode.nextInOrder != targetNode) {
                prevOrderNode = prevOrderNode.nextInOrder;
            }
            if (prevOrderNode != null) {
                prevOrderNode.nextInOrder = targetNode.nextInOrder;
                if (targetNode == tail) {
                    tail = prevOrderNode;
                }
            }
        }

        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hash(o);
        int idx = indexFor(hash);
        Node<E> current = table[idx];

        while (current != null) {
            if (current.hash == hash && equalsValue(current.value, o)) {
                return true;
            }
            current = current.nextInBucket;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.value);
            first = false;
            current = current.nextInOrder;
        }
        sb.append("]");
        return sb.toString();
    }







    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }




    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        Node<E> prevOrderNode = null;

        while (current != null) {
            Node<E> nextInOrder = current.nextInOrder;

            if (!c.contains(current.value)) {

                int idx = indexFor(current.hash);
                Node<E> bucketNode = table[idx];
                Node<E> prevBucketNode = null;
                while (bucketNode != null) {
                    if (bucketNode == current) {
                        if (prevBucketNode == null) {
                            table[idx] = bucketNode.nextInBucket;
                        } else {
                            prevBucketNode.nextInBucket = bucketNode.nextInBucket;
                        }
                        break;
                    }
                    prevBucketNode = bucketNode;
                    bucketNode = bucketNode.nextInBucket;
                }

                if (prevOrderNode == null) {
                    head = nextInOrder;
                    if (head == null) {
                        tail = null;
                    }
                } else {
                    prevOrderNode.nextInOrder = nextInOrder;
                    if (current == tail) {
                        tail = prevOrderNode;
                    }
                }
                size--;
                modified = true;
            } else {
                prevOrderNode = current;
            }
            current = nextInOrder;
        }
        return modified;
    }



    private class MyIterator implements Iterator<E> {
        private Node<E> nextNode = head;
        private Node<E> lastReturned = null;
        private Node<E> prevNode = null;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            lastReturned = nextNode;
            nextNode = nextNode.nextInOrder;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            MyLinkedHashSet.this.remove(lastReturned.value);
            lastReturned = null;
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
        Node<E> current = head;
        while (current != null) {
            result[i++] = current.value;
            current = current.nextInOrder;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            a[i++] = (T) current.value;
            current = current.nextInOrder;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }


}
