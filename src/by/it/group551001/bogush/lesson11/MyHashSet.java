package by.it.group551001.bogush.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    // Узел односвязного списка (цепочка коллизий)
    private static class Node<E> {
        E value;
        int hash;
        Node<E> next;

        Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table; // массив "корзин"
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
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

    // номер корзины; & 0x7FFFFFFF убирает знак, чтобы индекс не был отрицательным
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

    // Находит узел с таким значением или возвращает null
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

    // Увеличивает таблицу вдвое и раскладывает узлы по новым корзинам
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        Node<E>[] newTable = (Node<E>[]) new Node[oldTable.length * 2];

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                Node<E> next = current.next; // запоминаем, пока не переписали ссылку
                int index = indexFor(current.hash, newTable.length);
                current.next = newTable[index];
                newTable[index] = current;
                current = next;
            }
        }
        table = newTable;
    }

    // ------------------------------------------------------------------
    // Обязательные методы
    // ------------------------------------------------------------------

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (findNode(element) != null) {
            return false; // уже есть, дубликаты не нужны
        }
        // load factor 0.75: если заполнено больше, расширяем
        if (size + 1 > table.length * 3 / 4) {
            resize();
        }
        int hash = hashOf(element);
        int index = indexFor(hash, table.length);
        table[index] = new Node<>(element, hash, table[index]); // вставка в начало цепочки
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
                if (previous == null) {
                    table[index] = current.next; // удаляем первый в цепочке
                } else {
                    previous.next = current.next;
                }
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
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(current.value);
                current = current.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Простые бонусные методы
    // ------------------------------------------------------------------

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

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}