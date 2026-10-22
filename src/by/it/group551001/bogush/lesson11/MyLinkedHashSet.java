package by.it.group551001.bogush.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    // Узел сразу в двух структурах:
    //   next          -- цепочка коллизий внутри корзины (односвязная)
    //   before, after -- порядок добавления (двусвязный список по всем узлам)
    private static class Node<E> {
        E value;
        int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> first; // самый старый добавленный
    private Node<E> last;  // самый новый добавленный
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        first = null;
        last = null;
        size = 0;
    }

    // ------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------

    private int hashOf(Object o) {
        if (o == null) {
            return 0;
        } else {
            return o.hashCode();
        }
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    private boolean sameValue(Object o, E value) {
        if (o == null) {
            return value == null;
        } else {
            return o.equals(value);
        }
    }

    private Node<E> findNode(Object o) {
        int hash = hashOf(o);
        Node<E> current = table[indexFor(hash, table.length)];
        while (current != null) {
            if (current.hash == hash && sameValue(o, current.value)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Расширяет таблицу. Список порядка не трогаем: он от корзин не зависит.
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        Node<E>[] newTable = (Node<E>[]) new Node[oldTable.length * 2];

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                Node<E> nextInChain = current.next;
                int index = indexFor(current.hash, newTable.length);
                current.next = newTable[index];
                newTable[index] = current;
                current = nextInChain;
            }
        }
        table = newTable;
    }

    // Добавляет узел в конец списка порядка
    private void linkLast(Node<E> node) {
        if (last == null) {
            first = node;
            last = node;
        } else {
            node.before = last;
            last.after = node;
            last = node;
        }
    }

    // Вырезает узел из списка порядка
    private void unlinkOrder(Node<E> node) {
        if (node.before == null) {
            first = node.after;
        } else {
            node.before.after = node.after;
        }

        if (node.after == null) {
            last = node.before;
        } else {
            node.after.before = node.before;
        }

        node.before = null;
        node.after = null;
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = first;
        while (current != null) {
            sb.append(current.value);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (findNode(element) != null) {
            return false; // повторное добавление порядок не меняет
        }
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        int hash = hashOf(element);
        int index = indexFor(hash, table.length);
        Node<E> node = new Node<>(element, hash, table[index]);
        table[index] = node;
        linkLast(node);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hashOf(o);
        int index = indexFor(hash, table.length);
        Node<E> previous = null;
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && sameValue(o, current.value)) {
                // 1) вырезаем из цепочки корзины
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                // 2) вырезаем из списка порядка
                unlinkOrder(current);
                current.next = null;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return findNode(o) != null;
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
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == this) {
            boolean hadElements = size > 0;
            clear();
            return hadElements;
        }
        boolean modified = false;
        Node<E> current = first;
        while (current != null) {
            Node<E> nextInOrder = current.after; // запоминаем до удаления
            if (c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = nextInOrder;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        Node<E> current = first;
        while (current != null) {
            Node<E> nextInOrder = current.after;
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = nextInOrder;
        }
        return modified;
    }

    // ------------------------------------------------------------------
    // Остальное из интерфейса -- не обязательно, заглушки
    // ------------------------------------------------------------------

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}