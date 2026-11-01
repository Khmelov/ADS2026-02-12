package by.it.group510902.zenkovich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E>{

    private class Node<E> {
        E value;

        Node <E> next, prev;

        Node(E value) {
            this.value = value;
        }

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
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
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
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

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }


    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

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

        return unlink(current);
    }

    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if ((element == null && current.value == null) ||
                    (element != null && element.equals(current.value))) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private E unlink(Node<E> node) {
        E element = node.value;
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

        node.value = null;
        size--;
        return element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }


    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }







    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }

    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E remove() {
        E e = pollFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E removeFirst() {
        E e = pollFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E removeLast() {
        E e = pollLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public E peekFirst() { return head == null ? null : head.value; }

    @Override
    public E peekLast() { return tail == null ? null : tail.value; }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) {
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
            if ((o == null && current.value == null) ||
                    (o != null && o.equals(current.value))) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.value = null;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = head;
            @Override public boolean hasNext() { return current != null; }
            @Override public E next() {
                if (current == null) throw new NoSuchElementException();
                E v = current.value;
                current = current.next;
                return v;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> current = tail;
            @Override public boolean hasNext() { return current != null; }
            @Override public E next() {
                if (current == null) throw new NoSuchElementException();
                E v = current.value;
                current = current.prev;
                return v;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> cur = head;
        int i = 0;
        while (cur != null) { arr[i++] = cur.value; cur = cur.next; }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        Node<E> cur = head;
        int i = 0;
        while (cur != null) { a[i++] = (T) cur.value; cur = cur.next; }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) { add(e); changed = true; }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.value)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.value)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }

    /* add(E element)
                remove(int)
                remove(E element)
                size()

                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()*/

}
