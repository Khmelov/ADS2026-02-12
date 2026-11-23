package by.it.group551001.telipko.lesson10;

import java.util.*;


public class MyLinkedList<E> implements Deque<E> {

    public static void main(String[] args) {
        MyLinkedList<String> l = new MyLinkedList<>();
        l.add("A");
        l.addFirst("Z");
        l.addLast("B");
        System.out.println(l);
        l.remove(1);
        System.out.println(l);
        l.remove("Z");
        System.out.println(l);
    }

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;


    private void linkFirst(E e) {
        Node<E> oldFirst = first;
        Node<E> node = new Node<>(null, e, oldFirst);
        first = node;
        if (oldFirst == null) {
            last = node;
        } else {
            oldFirst.prev = node;
        }
        size++;
    }

    private void linkLast(E e) {
        Node<E> oldLast = last;
        Node<E> node = new Node<>(oldLast, e, null);
        last = node;
        if (oldLast == null) {
            first = node;
        } else {
            oldLast.next = node;
        }
        size++;
    }

    private E unlink(Node<E> node) {
        E item = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return item;
    }

    private Node<E> nodeAt(int index) {
        if (index < size / 2) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    private static boolean same(Object a, Object b) {
        return Objects.equals(a, b);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = first; x != null; x = x.next) {
            sb.append(x.item);
            if (x.next != null) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
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
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        linkFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public void push(E e) {
        linkFirst(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            linkLast(e);
            changed = true;
        }
        return changed;
    }


    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.item;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.item;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E pollFirst() {
        return first == null ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return last == null ? null : unlink(last);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return unlink(first);
    }

    @Override
    public E removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return unlink(last);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (same(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = last; x != null; x = x.prev) {
            if (same(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> x = first;
        while (x != null) {
            Node<E> next = x.next;
            if (c.contains(x.item)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> x = first;
        while (x != null) {
            Node<E> next = x.next;
            if (!c.contains(x.item)) {
                unlink(x);
                changed = true;
            }
            x = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<E> x = first;
        while (x != null) {
            Node<E> next = x.next;
            x.item = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        first = null;
        last = null;
        size = 0;
    }


    @Override
    public boolean contains(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (same(o, x.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = first;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) {
                    throw new NoSuchElementException();
                }
                E item = cur.item;
                cur = cur.next;
                return item;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> cur = last;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) {
                    throw new NoSuchElementException();
                }
                E item = cur.item;
                cur = cur.prev;
                return item;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            a[i++] = (T) x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
