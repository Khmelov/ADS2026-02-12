package by.it.group551003.popko.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
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
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        checkIndex(index);
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return head.item;
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        return unlink(tail);
    }

    private Node<E> nodeAt(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private E unlink(Node<E> node) {
        E value = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return value;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            addLast(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean offer(E e) {
        addLast(e);
        return true;
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
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : head.item;
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : tail.item;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        Node<E> current = head;
        while (current != null) {
            hash = 31 * hash + (current.item == null ? 0 : current.item.hashCode());
            current = current.next;
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque)) return false;
        Deque<?> other = (Deque<?>) o;
        if (other.size() != size) return false;
        Iterator<?> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object a = it1.next();
            Object b = it2.next();
            if (a == null ? b != null : !a.equals(b)) return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
                if (!hasNext()) throw new NoSuchElementException();
                E value = current.item;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = head;
        int i = 0;
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
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            Node<E> current = head;
            int i = 0;
            while (current != null) {
                result[i++] = (T) current.item;
                current = current.next;
            }
            return result;
        }
        Node<E> current = head;
        int i = 0;
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
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.item)) {
                unlink(current);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.item)) {
                unlink(current);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.item = null;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
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
                if (!hasNext()) throw new NoSuchElementException();
                E value = current.item;
                current = current.prev;
                return value;
            }
        };
    }
}