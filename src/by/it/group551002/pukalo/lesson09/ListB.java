package by.it.group551002.pukalo.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;
        Node(E value) { this.value = value; }
    }

    private Node<E> head;   // первый узел
    private Node<E> tail;   // последний узел
    private int size = 0;

    // ===================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =====================

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
        Node<E> node = new Node<>(e);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if (index == size) { add(element); return; }

        Node<E> node = new Node<>(element);
        if (index == 0) {
            node.next = head;
            head.prev = node;
            head = node;
        } else {
            Node<E> cur = getNode(index);
            Node<E> prev = cur.prev;
            prev.next = node;
            node.prev = prev;
            node.next = cur;
            cur.prev = node;
        }
        size++;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> cur = getNode(index);
        E value = cur.value;
        unlink(cur);
        return value;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
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
        Node<E> cur = getNode(index);
        E old = cur.value;
        cur.value = element;
        return old;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).value;
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
    public void clear() {
        // разрываем связи, чтобы GC мог собрать
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.next = null;
            cur.prev = null;
            cur.value = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) return i;
            cur = cur.next;
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        Node<E> cur = tail;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) return i;
            cur = cur.prev;
            i--;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // ===================== ВСПОМОГАТЕЛЬНЫЕ =====================

    private Node<E> getNode(int index) {
        // оптимизация: идём с того конца, который ближе
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
        if (prev == null) head = next; else prev.next = next;
        if (next == null) tail = prev; else next.prev = prev;
        node.next = null;
        node.prev = null;
        node.value = null;
        size--;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // ===================== ОПЦИОНАЛЬНЫЕ =====================

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
        for (E e : c) { add(e); changed = true; }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        boolean changed = false;
        int i = index;
        for (E e : c) { add(i++, e); changed = true; }
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

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next)
            arr[i++] = cur.value;
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next)
            a[i++] = (T) cur.value;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // простая реализация — возвращаем копию
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListB<E> sub = new ListB<>();
        Node<E> cur = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.value);
            cur = cur.next;
        }
        return sub;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                E v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() { return listIterator(0); }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);
        return new ListIterator<E>() {
            Node<E> cur = (index == size) ? null : getNode(index);
            Node<E> last = null;
            int idx = index;

            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                return null;
            }
            @Override public boolean hasPrevious() { return idx > 0; }
            @Override public E previous() {
                return null;
            }
            @Override public int nextIndex() { return idx; }
            @Override public int previousIndex() { return idx - 1; }
            @Override public void remove() {
                if (last == null) throw new IllegalStateException();
                unlink(last);
                last = null;
                idx--;
            }
            @Override public void set(E e) {
                if (last == null) throw new IllegalStateException();
                last.value = e;
            }
            @Override public void add(E e) {
                ListB.this.add(idx, e);
                idx++;
                last = null;
            }
        };
    }
}