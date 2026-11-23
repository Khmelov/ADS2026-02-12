package by.it.group510902.kudan.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    //узел цепочки, next ведет к следующему элементу из той же корзины (коллизия)
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    //массив корзин, номер корзины считается по хеш коду элемента
    private Node<E>[] table;

    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    //номер корзины для элемента, знак хеша убираем чтобы остаток не вышел отрицательным
    private int index(Object o, int n) {
        return o == null ? 0 : (o.hashCode() & 0x7fffffff) % n;
    }

    //делаем таблицу вдвое больше и раскладываем все узлы заново
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] big = (Node<E>[]) new Node[table.length * 2];

        for (int i = 0; i < table.length; i++) {
            Node<E> x = table[i];

            while (x != null) {
                //следующий узел запоминаем заранее, потому что next сейчас перезапишем
                Node<E> next = x.next;
                int j = index(x.value, big.length);
                x.next = big[j];
                big[j] = x;
                x = next;
            }
        }

        table = big;
    }

    @Override
    public String toString() {
        //порядок любой, идем по корзинам и по цепочкам
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < table.length; i++) {
            for (Node<E> x = table[i]; x != null; x = x.next) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(x.value);
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
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        //такой элемент уже есть, множество не меняется
        if (contains(element)) {
            return false;
        }

        //новый узел ставим в начало цепочки своей корзины
        int i = index(element, table.length);
        Node<E> x = new Node<>(element);
        x.next = table[i];
        table[i] = x;
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

        //идем по цепочке и помним предыдущий узел чтобы вырезать нужный
        for (Node<E> x = table[i]; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                if (prev == null) {
                    table[i] = x.next;
                } else {
                    prev.next = x.next;
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
        //смотрим только цепочку своей корзины
        for (Node<E> x = table[index(element, table.length)]; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}