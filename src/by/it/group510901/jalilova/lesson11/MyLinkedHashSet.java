package by.it.group510901.jalilova.lesson11;
/*              toString()
                size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)

                containsAll(Collection)
                addAll(Collection)
                removeAll(Collection)
                retainAll(Collection)*/
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    //   next цепочка коллизий внутри корзины
    //   before after общий двусвязный список порядка добавления
    private static class Node<E> {
        final int hash;
        final E element;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(int hash, E element, Node<E> next) {
            this.hash = hash;
            this.element = element;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private Node<E> head;   // первый добавленный
    private Node<E> tail;   // последний добавленный

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    private static int spread(int h) {
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash) {
        return hash & (table.length - 1);
    }


    @Override
    public boolean add(E e) {
        int hash = spread(e == null ? 0 : e.hashCode());
        int index = indexFor(hash);

        // ищем дубликат в корзине (как в HashSet)
        for (Node<E> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && (n.element == e || (e != null && e.equals(n.element)))) {
                return false;
            }
        }

        // новый узел вставляем в голову цепочки коллизий
        Node<E> newNode = new Node<>(hash, e, table[index]);
        table[index] = newNode;

        // и одновременно — в ХВОСТ списка порядка
        linkLast(newNode);

        size++;

        if (size > table.length * 3 / 4) {
            resize();
        }
        return true;
    }

    // вставка узла в конец двусвязного списка порядка — O(1)
    private void linkLast(Node<E> node) {
        Node<E> t = tail;
        tail = node;
        if (t == null) {
            head = node;
        } else {
            t.after = node;
            node.before = t;
        }
    }

    // удаление узла из списка порядка — O(1)
    private void unlink(Node<E> node) {
        Node<E> b = node.before;
        Node<E> a = node.after;

        if (b == null) head = a;
        else { b.after = a; node.before = null; }

        if (a == null) tail = b;
        else { a.before = b; node.after = null; }
    }

    @Override
    public boolean remove(Object o) {
        int hash = spread(o == null ? 0 : o.hashCode());
        int index = indexFor(hash);

        Node<E> prev = null;
        Node<E> cur = table[index];
        while (cur != null) {
            if (cur.hash == hash && (cur.element == o || (o != null && o.equals(cur.element)))) {
                // вырезаем из цепочки коллизий
                if (prev == null) table[index] = cur.next;
                else prev.next = cur.next;

                // вырезаем из списка порядка
                unlink(cur);

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
        // обнуляем всё: и таблицу, и список порядка
        for (int i = 0; i < table.length; i++) table[i] = null;
        // помогаем GC, разрывая связи
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            cur.before = cur.after = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    // toString

    // Единственный метод, где используется порядок: идём по after от head
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> n = head;
        boolean first = true;
        while (n != null) {
            if (!first) sb.append(", ");
            sb.append(n.element);
            first = false;
            n = n.after;
        }
        return sb.append("]").toString();
    }

    // bulk-операции

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        // идём по списку порядка — так удаление через unlink работает корректно
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;   // сохраняем ДО удаления
            if (c.contains(cur.element)) {
                remove(cur.element);    // remove сам вырежет из обеих структур
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
            Node<E> next = cur.after;
            if (!c.contains(cur.element)) {
                remove(cur.element);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    // resize

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] old = table;
        table = new Node[old.length * 2];

        // перестраиваем ТОЛЬКО цепочки коллизий (next)
        // список порядка (before/after) НЕ трогаем — он не зависит от table
        for (Node<E> headBucket : old) {
            Node<E> cur = headBucket;
            while (cur != null) {
                Node<E> next = cur.next;
                int index = indexFor(cur.hash);
                cur.next = table[index];
                table[index] = cur;
                cur = next;
            }
        }
    }

    // iterator

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> nextNode = head;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) throw new java.util.NoSuchElementException();
                E e = nextNode.element;
                nextNode = nextNode.after;   // идём по порядку добавления
                return e;
            }
        };
    }

    // toArray

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> n = head; n != null; n = n.after) arr[i++] = n.element;
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        int i = 0;
        for (Node<E> n = head; n != null; n = n.after) a[i++] = (T) n.element;
        if (a.length > size) a[size] = null;
        return a;
    }
}