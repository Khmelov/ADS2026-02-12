package by.it.group551003.kalvinkovskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        String str;
        if (size==0) {
            str = "[]";
        }
        else {
            str = "[";
            Node<E> curr = head;
            str += curr.item;
            curr = curr.next;
            while (curr != null){
                str += ", " + curr.item;
                curr = curr.next;
            }
            str += "]";
        }
        return str;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null)
            throw new NullPointerException("Element cannot be null");

        Node<E> newNode = new Node<>(null, element, head);
        if (head == null)
            tail = newNode;
        else
            head.prev = newNode;
        head = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null)
            throw new NullPointerException("Element cannot be null");

        Node<E> newNode = new Node<>(tail, element, null);
        if (tail == null)
            head = newNode;
        else
            tail.next = newNode;
        tail = newNode;
        size++;
    }

    public E remove(int ind) {
        if (size == 0)
            throw new RuntimeException("Deque is empty");
        if (ind < 0 || ind >= size)
            throw new IndexOutOfBoundsException("Index: " + ind + ", Size: " + size);
        int i = 0;
        Node<E> toRemove = head;
        while (i < ind && toRemove != null) {
            toRemove = toRemove.next;
            i++;
        }
        return unlink(toRemove);
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0)
            throw new RuntimeException("Deque is empty");
        Node<E> curr;
        curr = head;
        while (curr != null) {
            boolean match = (o == null && curr.item == null) || (o != null && o.equals(curr.item));
            if (match) {
                unlink(curr);
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private E unlink(Node<E> curr) {
        E toReturn = curr.item;
        Node<E> next = curr.next;
        Node<E> prev = curr.prev;

        if (prev == null) {
            head = next;
        }
        else {
            prev.next = next;
            curr.prev = null;
        }
        if (next == null) {
            tail = prev;
        }
        else {
            next.prev = prev;
            curr.next = null;
        }
        curr.item = null;
        size--;

        return toReturn;
    }

    @Override
    public E getFirst() {
        if (size==0)
            throw new RuntimeException("Deque is empty");
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new RuntimeException("Deque is empty");
        return tail.item;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0)
            return null;
        E result = head.item;
        head = head.next;
        if (head == null)
            tail = null;
        else
            head.prev = null;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0)
            return null;
        E result = tail.item;
        tail = tail.prev;
        if (tail == null)
            head = null;
        else
            tail.next = null;
        size--;
        return result;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        E res = pollFirst();
        if (res == null) throw new RuntimeException("Deque is empty");
        return res;
    }

    @Override
    public E removeLast() {
        E res = pollLast();
        if (res == null) throw new RuntimeException("Deque is empty");
        return res;
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.item;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.item;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            boolean match = (o == null && current.item == null) || (o != null && o.equals(current.item));
            if (match) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if ((o == null && current.item == null) || (o != null && o.equals(current.item))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> nextNode = current.next;
            current.item = null;
            current.next = null;
            current.prev = null;
            current = nextNode;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            result[i++] = current.item;
            current = current.next;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Object[] res = new Object[size];
            int i = 0;
            Node<E> current = head;
            while (current != null) {
                res[i++] = current.item;
                current = current.next;
            }
            return (T[]) res;
        }
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            a[i++] = (T) current.item;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            addLast(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.item)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.item)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new RuntimeException("No more elements");
                E item = current.item;
                current = current.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new RuntimeException("No more elements");
                E item = current.item;
                current = current.prev;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
