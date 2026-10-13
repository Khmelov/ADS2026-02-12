package by.it.group510901.chernyavskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> bucketNext; // Ссылка для коллизий в хеш-таблице
        Node<E> before, after; // Ссылки для сохранения порядка добавления

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets = (Node<E>[]) new Node[16];
    private int size = 0;
    private Node<E> head = null; // Первый добавленный элемент
    private Node<E> tail = null; // Последний добавленный элемент

    private int getIndex(Object o) {
        return o == null ? 0 : Math.abs(o.hashCode() % buckets.length);
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
    public boolean contains(Object o) {
        int idx = getIndex(o);
        for (Node<E> n = buckets[idx]; n != null; n = n.bucketNext) {
            if (o == null ? n.data == null : o.equals(n.data)) return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int idx = getIndex(e);
        for (Node<E> n = buckets[idx]; n != null; n = n.bucketNext) {
            if (e == null ? n.data == null : e.equals(n.data)) return false;
        }

        Node<E> newNode = new Node<>(e);

        // Добавление в цепочку хеш-таблицы
        newNode.bucketNext = buckets[idx];
        buckets[idx] = newNode;

        // Добавление в конец списка порядка вставки
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = getIndex(o);
        Node<E> prev = null;
        Node<E> target = null;

        for (Node<E> n = buckets[idx]; n != null; prev = n, n = n.bucketNext) {
            if (o == null ? n.data == null : o.equals(n.data)) {
                target = n;
                if (prev == null) buckets[idx] = n.bucketNext;
                else prev.bucketNext = n.bucketNext;
                break;
            }
        }

        if (target == null) return false;

        // Удаление из списка порядка вставки
        if (target.before != null) target.before.after = target.after;
        else head = target.after;

        if (target.after != null) target.after.before = target.before;
        else tail = target.before;

        size--;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> n = head; n != null; n = n.after) {
            if (!first) sb.append(", ");
            sb.append(n.data);
            first = false;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> n = head;
        while (n != null) {
            Node<E> next = n.after;
            if (!c.contains(n.data)) {
                remove(n.data);
                modified = true;
            }
            n = next;
        }
        return modified;
    }

    // --- Остальные методы интерфейса Set (заглушки) ---
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}