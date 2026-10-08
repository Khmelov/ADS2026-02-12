package by.it.group551002.kurzhalov.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e, tail, null);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E old = node.value;
        unlink(node);
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index);

        if (index == size) {
            add(element);
            return;
        }

        Node<E> next = nodeAt(index);
        Node<E> prev = next.prev;
        Node<E> newNode = new Node<>(element, prev, next);
        next.prev = newNode;
        if (prev == null) {
            head = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.prev = cur.next = null;
            cur.value = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                return index;
            }
            cur = cur.next;
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        Node<E> cur = tail;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                return index;
            }
            cur = cur.prev;
            index--;
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index);
        boolean changed = false;
        int i = index;
        for (E e : c) {
            add(i++, e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.value)) {
                unlink(cur);
                changed = true;
            }
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
            if (!c.contains(cur.value)) {
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListB<E> sub = new ListB<>();
        Node<E> cur = nodeAt(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.value);
            cur = cur.next;
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index);

        return new ListIterator<E>() {
            private Node<E> nextNode = (index == size) ? null : nodeAt(index);
            private Node<E> lastReturned = null;
            private int nextIndex = index;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = nextNode.next;
                nextIndex++;
                return lastReturned.value;
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                if (nextNode == null) {
                    nextNode = tail;
                } else {
                    nextNode = nextNode.prev;
                }
                lastReturned = nextNode;
                nextIndex--;
                return lastReturned.value;
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                if (lastReturned == nextNode) {
                    nextNode = nextNode.next;
                } else {
                    nextIndex--;
                }
                unlink(lastReturned);
                lastReturned = null;
            }

            @Override
            public void set(E e) {
                if (lastReturned == null) throw new IllegalStateException();
                lastReturned.value = e;
            }

            @Override
            public void add(E e) {
                Node<E> newNode = new Node<>(e,
                        nextNode == null ? tail : nextNode.prev,
                        nextNode);
                if (nextNode == null) {
                    if (tail == null) {
                        head = tail = newNode;
                    } else {
                        tail.next = newNode;
                        tail = newNode;
                    }
                } else {
                    Node<E> prev = nextNode.prev;
                    nextNode.prev = newNode;
                    if (prev == null) {
                        head = newNode;
                    } else {
                        prev.next = newNode;
                    }
                }
                size++;
                nextIndex++;
                lastReturned = null;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            int i = 0;
            for (Node<E> cur = head; cur != null; cur = cur.next) {
                result[i++] = (T) cur.value;
            }
            return result;
        }
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            a[i++] = (T) cur.value;
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            result[i++] = cur.value;
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> nextNode = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = nextNode.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                if (lastReturned == nextNode) {
                    nextNode = nextNode.next;
                }
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //                        Вспомогательные методы                        //
    /////////////////////////////////////////////////////////////////////////

    private Node<E> nodeAt(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index);
        if (index < size / 2) {
            Node<E> cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            Node<E> cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    private void unlink(Node<E> node) {
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
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
    }
}