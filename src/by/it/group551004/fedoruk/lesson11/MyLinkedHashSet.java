package by.it.group551004.fedoruk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;       // Ссылка для коллизий в бакете (односвязный список)
        Node<E> orderPrev;  // Предыдущий элемент в порядке добавления
        Node<E> orderNext;  // Следующий элемент в порядке добавления

        Node(E item) {
            this.item = item;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private Node<E> headOrder; // Первый добавленный элемент
    private Node<E> tailOrder; // Последний добавленный элемент

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        headOrder = null;
        tailOrder = null;
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7FFFFFFF) % table.length;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        headOrder = null;
        tailOrder = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) {
            return false;
        }
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        Node<E> newNode = new Node<>(element);
        int index = getIndex(element);

        // Добавление в бакет (коллизии через односвязный список в начале)
        newNode.next = table[index];
        table[index] = newNode;

        // Добавление в конец цепочки порядка добавления
        if (headOrder == null) {
            headOrder = tailOrder = newNode;
        } else {
            tailOrder.orderNext = newNode;
            newNode.orderPrev = tailOrder;
            tailOrder = newNode;
        }

        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        // Перераспределение узлов по новым бакетам с сохранением существующего порядка
        Node<E> current = headOrder;
        while (current != null) {
            current.next = null;
            int index = getIndex(current.item);
            current.next = table[index];
            table[index] = current;
            current = current.orderNext;
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                // Удаление из бакета (односвязный список)
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаление из цепочки порядка добавления
                Node<E> p = current.orderPrev;
                Node<E> n = current.orderNext;

                if (p == null) {
                    headOrder = n;
                } else {
                    p.orderNext = n;
                    current.orderPrev = null;
                }

                if (n == null) {
                    tailOrder = p;
                } else {
                    n.orderPrev = p;
                    current.orderNext = null;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = headOrder;
        while (current != null) {
            sb.append(current.item);
            if (current.orderNext != null) {
                sb.append(", ");
            }
            current = current.orderNext;
        }
        sb.append("]");
        return sb.toString();
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
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = headOrder;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.orderNext;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.item);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        Node<E> current = headOrder;
        while (current != null) {
            result[i++] = current.item;
            current = current.orderNext;
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
        Object[] result = a;
        Node<E> current = headOrder;
        while (current != null) {
            result[i++] = (T) current.item;
            current = current.orderNext;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}