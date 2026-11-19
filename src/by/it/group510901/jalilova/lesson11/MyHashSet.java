package by.it.group510901.jalilova.lesson11;
/*              size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)*/

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Узел односвязного списка
    private static class Node<E> {
        final int hash;
        final E element;
        Node<E> next;

        Node(int hash, E element, Node<E> next) {
            this.hash = hash;
            this.element = element;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    /*В ячейке table[index] лежит голова односвязного списка узлов.
     У всех узлов этого списка одинаковый индекс index = hash & (length-1).
     Их хеши могут быть как одинаковыми (редко), так и разными (обычно).
     Узлы внутри списка различаются через hash и equals*/

    // Вспомогательный метод: "размазываем" hashCode, чтобы младшие биты были равномернее
    private static int spread(int h) {
        return h ^ (h >>> 16);
    }

    // Индекс корзины; hashCode может быть отрицательным — берём маску
    private int indexFor(int hash) {
        return hash & (table.length - 1);
    } // результат всегда в 0..15

    @Override
    public boolean add(E e) {
        int hash = spread(e == null ? 0 : e.hashCode());
        int index = indexFor(hash);

        for (Node<E> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && (n.element == e || (e != null && e.equals(n.element)))) {
                return false;
            }
        }

        // создаём новый узел, у которого next указывает на старую голову. Если она была
        table[index] = new Node<>(hash, e, table[index]);
        size++;

        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = spread(o == null ? 0 : o.hashCode());
        int index = indexFor(hash);

        Node<E> prev = null;
        Node<E> cur = table[index];

        while (cur != null) {
            if (cur.hash == hash && (cur.element == o || (o != null && o.equals(cur.element)))) {
                if (prev == null) {
                    table[index] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int hash = spread(o == null ? 0 : o.hashCode());
        int index = indexFor(hash);

        for (Node<E> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && (n.element == o || (o != null && o.equals(n.element)))) {
                return true;
            }
        }
        return false;
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
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Расширение массива вдвое и перераспределение элементов
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = table;
        table = new Node[old.length * 2];

        for (Node<E> head : old) {
            Node<E> cur = head;
            while (cur != null) {
                Node<E> next = cur.next;
                int index = indexFor(cur.hash);
                cur.next = table[index];
                table[index] = cur;
                cur = next;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            for (Node<E> n = head; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(n.element);
                first = false;
            }
        }
        return sb.append("]").toString();
    }





    // Остальные методы Set — минимально необходимые

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> nextNode = advance(null);

            private Node<E> advance(Node<E> current) {
                if (current != null && current.next != null) return current.next;
                while (bucket < table.length) {
                    Node<E> n = table[bucket++];
                    if (n != null) return n;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                E e = nextNode.element;
                nextNode = advance(nextNode);
                return e;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> head : table)
            for (Node<E> n = head; n != null; n = n.next)
                arr[i++] = n.element;
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Object[] src = toArray();
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(src, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (Node<E> head : table) {
            Node<E> prev = null, cur = head;
            while (cur != null) {
                if (!c.contains(cur.element)) {
                    if (prev == null) table[indexFor(cur.hash)] = cur.next;
                    else prev.next = cur.next;
                    size--;
                    changed = true;
                    cur = cur.next;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }
}