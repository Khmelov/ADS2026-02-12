package by.it.group551001.maxim_belyak.lesson11;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Множество с сохранением порядка добавления.
 * Корзины хранят односвязные списки для коллизий (поле bucketNext),
 * а порядок добавления поддерживается отдельной двусвязной цепочкой
 * (поля before/after), что даёт удаление за O(1).
 * Из стандартной библиотеки используются только интерфейсы Set, Collection, Iterator.
 */
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        final E value;
        final int hash;
        Node<E> bucketNext;   // следующий в корзине (коллизии)
        Node<E> before;       // предыдущий по порядку добавления
        Node<E> after;        // следующий по порядку добавления

        Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    private Node<E>[] table;
    private Node<E> head;   // самый старый элемент
    private Node<E> tail;   // самый новый элемент
    private int size;

    public MyLinkedHashSet() {
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
        Node<E>[] fresh = newTable(table.length * 2);
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            int idx = indexFor(cur.hash, fresh.length);
            cur.bucketNext = fresh[idx];
            fresh[idx] = cur;
        }
        table = fresh;
    }

    // ---------- базовые методы ----------

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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hashOf(o);
        for (Node<E> cur = table[indexFor(hash, table.length)]; cur != null; cur = cur.bucketNext) {
            if (cur.hash == hash && same(o, cur.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = hashOf(e);
        int idx = indexFor(hash, table.length);

        for (Node<E> cur = table[idx]; cur != null; cur = cur.bucketNext) {
            if (cur.hash == hash && same(e, cur.value)) {
                return false;
            }
        }

        Node<E> node = new Node<>(e, hash);

        // в корзину
        node.bucketNext = table[idx];
        table[idx] = node;

        // в конец цепочки порядка добавления
        node.before = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;

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
                // из корзины
                if (prev == null) {
                    table[idx] = cur.bucketNext;
                } else {
                    prev.bucketNext = cur.bucketNext;
                }
                // из цепочки порядка
                if (cur.before == null) {
                    head = cur.after;
                } else {
                    cur.before.after = cur.after;
                }
                if (cur.after == null) {
                    tail = cur.before;
                } else {
                    cur.after.before = cur.before;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.bucketNext;
        }
        return false;
    }

    // ---------- toString: порядок добавления ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            if (cur != head) {
                sb.append(", ");
            }
            sb.append(cur.value == this ? "(this Collection)" : String.valueOf(cur.value));
        }
        return sb.append(']').toString();
    }

    // ---------- групповые операции ----------

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
        // идём по себе, а не по c: безопасно даже при c == this
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
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

    // ---------- остальные методы интерфейса Set ----------

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;
            private Node<E> last = null;

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
                next = next.after;
                return last.value;
            }

            @Override
            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(last.value);
                last = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            result[i++] = cur.value;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            throw new UnsupportedOperationException("Передайте массив длиной не меньше size()");
        }
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            a[i++] = (T) cur.value;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        for (Node<E> cur = head; cur != null; cur = cur.after) {
            h += cur.hash;
        }
        return h;
    }

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        set.add("c");
        set.add("a");
        set.add("b");
        set.add("a");        // дубликат, порядок не меняется
        set.add(null);
        System.out.println(set);                 // [c, a, b, null]

        set.remove("a");
        set.add("a");                            // теперь "a" в конце
        System.out.println(set);                 // [c, b, null, a]

        MyLinkedHashSet<String> other = new MyLinkedHashSet<>();
        other.add("b");
        other.add("a");
        other.add("z");

        System.out.println(set.containsAll(other)); // false
        System.out.println(set.addAll(other));      // true
        System.out.println(set);                    // [c, b, null, a, z]

        System.out.println(set.removeAll(other));   // true
        System.out.println(set);                    // [c, null]

        set.addAll(other);
        System.out.println(set.retainAll(other));   // true
        System.out.println(set);                    // [b, a, z] (в порядке добавления в set)

        for (int i = 0; i < 100; i++) {
            set.add("x" + i);                       // проверка resize
        }
        System.out.println(set.size());             // 103
        System.out.println(set.contains("x57"));    // true

        set.clear();
        System.out.println(set + " empty=" + set.isEmpty());
    }
}