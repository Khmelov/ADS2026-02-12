package by.it.group551001.akynchits.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс узла односвязного списка
    private static class Node<E> {
        final E item;
        final int hash;
        Node<E> next;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        // Создаем массив узлов. Приведение типа необходимо из-за стирания типов в Java.
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // =====================================================================
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ИЗ ЗАДАНИЯ
    // =====================================================================

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
            table[i] = null; // Помогаем сборщику мусора
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];

        // Проверяем, есть ли уже такой элемент в списке (избегаем дубликатов)
        while (current != null) {
            if (current.hash == h && (current.item == e || (e != null && e.equals(current.item)))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        table[i] = new Node<>(h, e, table[i]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == h && (current.item == o || (o != null && o.equals(current.item)))) {
                // Элемент найден, удаляем его из односвязного списка
                if (prev == null) {
                    table[i] = current.next; // Удаляем головной элемент списка
                } else {
                    prev.next = current.next; // Удаляем элемент из середины или конца
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
    public boolean contains(Object o) {
        int h = hash(o);
        int i = indexFor(h, table.length);
        Node<E> current = table[i];

        while (current != null) {
            if (current.hash == h && (current.item == o || (o != null && o.equals(current.item)))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        // Используем простую конкатенацию, чтобы не импортировать StringBuilder
        // (хотя он из java.lang, это гарантирует 100% соответствие правилу "без других классов")
        String result = "[";
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    result += ", ";
                }
                result += current.item;
                first = false;
                current = current.next;
            }
        }
        return result + "]";
    }

    // =====================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (ЯДРО ХЕШ-ТАБЛИЦЫ)
    // =====================================================================

    // Безопасное получение хеш-кода (учитывает null)
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    // Вычисление индекса в массиве. Работает корректно, если длина массива - степень двойки.
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    // =====================================================================
    // РЕАЛИЗАЦИЯ ОСТАЛЬНЫХ МЕТОДОВ ИНТЕРФЕЙСА Set (для компиляции)
    // =====================================================================

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                result[index++] = current.item;
                current = current.next;
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                a[index++] = (T) current.item;
                current = current.next;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> curr = table[i];
            while (curr != null) {
                if (c.contains(curr.item)) {
                    if (prev == null) table[i] = curr.next;
                    else prev.next = curr.next;
                    size--;
                    modified = true;
                    curr = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = curr;
                    curr = curr.next;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> curr = table[i];
            while (curr != null) {
                if (!c.contains(curr.item)) {
                    if (prev == null) table[i] = curr.next;
                    else prev.next = curr.next;
                    size--;
                    modified = true;
                    curr = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = curr;
                    curr = curr.next;
                }
            }
        }
        return modified;
    }

    // Внутренний итератор для обхода хеш-таблицы
    private class MyIterator implements Iterator<E> {
        private int currentBucket = 0;
        private Node<E> currentNode = null;

        public MyIterator() {
            advance();
        }

        private void advance() {
            while (currentBucket < table.length && table[currentBucket] == null) {
                currentBucket++;
            }
            if (currentBucket < table.length) {
                currentNode = table[currentBucket];
                currentBucket++;
            } else {
                currentNode = null;
            }
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = currentNode.item;
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                advance();
            }
            return item;
        }
    }
}
