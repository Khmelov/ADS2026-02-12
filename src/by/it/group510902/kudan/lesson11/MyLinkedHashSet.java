package by.it.group510902.kudan.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    //узел сразу в двух структурах, next это цепочка корзины, before и after это порядок добавления
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value) {
            this.value = value;
        }
    }

    //массив корзин, номер корзины считается по хеш коду элемента
    private Node<E>[] table;

    //начало и конец списка в порядке добавления
    private Node<E> head;
    private Node<E> tail;

    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    //номер корзины для элемента, знак хеша убираем чтобы остаток не вышел отрицательным
    private int index(Object o, int n) {
        return o == null ? 0 : (o.hashCode() & 0x7fffffff) % n;
    }

    //делаем таблицу вдвое больше, узлы берем из списка порядка и кладем в новые корзины
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] big = (Node<E>[]) new Node[table.length * 2];

        for (Node<E> x = head; x != null; x = x.after) {
            int j = index(x.value, big.length);
            x.next = big[j];
            big[j] = x;
        }

        table = big;
    }

    @Override
    public String toString() {
        //идем по списку порядка от первого добавленного к последнему
        StringBuilder sb = new StringBuilder("[");

        for (Node<E> x = head; x != null; x = x.after) {
            sb.append(x.value);
            if (x.after != null) {
                sb.append(", ");
            }
        }

        return sb.append("]").toString();
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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        //такой элемент уже есть, порядок не меняется
        if (contains(element)) {
            return false;
        }

        //новый узел в начало цепочки корзины
        int i = index(element, table.length);
        Node<E> x = new Node<>(element);
        x.next = table[i];
        table[i] = x;

        //и в конец списка порядка добавления
        if (tail == null) {
            head = x;
        } else {
            tail.after = x;
            x.before = tail;
        }
        tail = x;
        size++;

        //если таблица заполнена больше чем на три четверти, расширяем
        if (size > table.length * 3 / 4) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        int i = index(element, table.length);
        Node<E> prev = null;

        for (Node<E> x = table[i]; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                //вырезаем из цепочки корзины
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
                }

                //вырезаем из списка порядка добавления
                if (x.before == null) {
                    head = x.after;
                } else {
                    x.before.after = x.after;
                }

                if (x.after == null) {
                    tail = x.before;
                } else {
                    x.after.before = x.before;
                }

                size--;
                return true;
            }
            prev = x;
        }

        return false;
    }

    @Override
    public boolean contains(Object element) {
        for (Node<E> x = table[index(element, table.length)]; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        //достаточно найти один элемент которого нет
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;

        for (E e : c) {
            if (add(e)) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;

        //удаляем каждый элемент из c, это быстрее чем искать каждый элемент нашего множества в c
        for (Object o : c) {
            if (remove(o)) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean res = false;
        Node<E> x = head;

        while (x != null) {
            //следующий узел запоминаем заранее, потому что текущий можем удалить
            Node<E> next = x.after;

            if (!c.contains(x.value)) {
                remove(x.value);
                res = true;
            }

            x = next;
        }

        return res;
    }

    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}