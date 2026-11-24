package by.it.group551001.maxim_belyak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Множество на основе массива корзин (бакетов) и односвязных списков для коллизий.
 * Из стандартной библиотеки используются только интерфейсы, которые требует
 * сам контракт Set<E>: Set, Collection, Iterator.
 */
public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    /** Узел односвязного списка внутри корзины. */
    private static class Node<E> {
        final E value;
        final int hash;
        Node<E> next;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;

    public MyHashSet() {
        table = newTable(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] newTable(int capacity) {
        return (Node<E>[]) new Node[capacity];
    }

    // ---------- вспомогательные методы ----------

    private static int hashOf(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    private static int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private static boolean same(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    private void resize() {
        Node<E>[] old = table;
        Node<E>[] fresh = newTable(old.length * 2);
        for (int i = 0; i < old.length; i++) {
            Node<E> cur = old[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(cur.hash, fresh.length);
                cur.next = fresh[idx];
                fresh[idx] = cur;
                cur = next;
            }
        }
        table = fresh;
    }

    // ---------- обязательные методы ----------

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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hashOf(o);
        Node<E> cur = table[indexFor(hash, table.length)];
        while (cur != null) {
            if (cur.hash == hash && same(o, cur.value)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = hashOf(e);
        int idx = indexFor(hash, table.length);

        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (cur.hash == hash && same(e, cur.value)) {
                return false; // уже есть
            }
        }

        table[idx] = new Node<>(e, hash, table[idx]); // вставка в голову списка
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hashOf(o);
        int idx = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == hash && same(o, cur.value)) {
                if (prev == null) {
                    table[idx] = cur.next;
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

    // ---------- toString: формат как у стандартных коллекций ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> cur = table[i]; cur != null; cur = cur.next) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(cur.value == this ? "(this Collection)" : String.valueOf(cur.value));
                first = false;
            }
        }
        return sb.append(']').toString();
    }

    // ---------- остальные методы интерфейса Set ----------

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> next = advanceFrom(null);
            private Node<E> last = null;

            // следующий узел после given (или первый узел, если given == null)
            private Node<E> advanceFrom(Node<E> given) {
                if (given != null && given.next != null) {
                    return given.next;
                }
                while (bucket < table.length) {
                    Node<E> head = table[bucket++];
                    if (head != null) {
                        return head;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) {
                    throw new java.util.NoSuchElementException();
                }
                last = next;
                next = advanceFrom(next);
                return last.value;
            }

            @Override
            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(last.value);
                last = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (int b = 0; b < table.length; b++) {
            for (Node<E> cur = table[b]; cur != null; cur = cur.next) {
                result[i++] = cur.value;
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // создать массив нужного типа без java.lang.reflect.Array нельзя
            throw new UnsupportedOperationException("Передайте массив длиной не меньше size()");
        }
        int i = 0;
        for (int b = 0; b < table.length; b++) {
            for (Node<E> cur = table[b]; cur != null; cur = cur.next) {
                a[i++] = (T) cur.value;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> other = (Set<?>) o;
        return other.size() == size && containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int b = 0; b < table.length; b++) {
            for (Node<E> cur = table[b]; cur != null; cur = cur.next) {
                h += cur.hash;
            }
        }
        return h;
    }

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MyHashSet<String> set = new MyHashSet<>();
        System.out.println(set + " empty=" + set.isEmpty());

        System.out.println(set.add("a"));      // true
        System.out.println(set.add("b"));      // true
        System.out.println(set.add("a"));      // false (дубликат)
        System.out.println(set.add(null));     // true (null допустим)
        System.out.println(set);               // например: [null, a, b]
        System.out.println("size=" + set.size());

        System.out.println(set.contains("b")); // true
        System.out.println(set.remove("b"));   // true
        System.out.println(set.contains("b")); // false

        for (int i = 0; i < 100; i++) {
            set.add("x" + i);                  // проверка resize и коллизий
        }
        System.out.println("size=" + set.size());

        set.clear();
        System.out.println(set + " empty=" + set.isEmpty());
    }
}