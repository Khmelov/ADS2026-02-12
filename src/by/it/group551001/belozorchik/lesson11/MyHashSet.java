package by.it.group551001.belozorchik.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet <E> implements Set<E> {

    //внутр класс узла для односвязного списка
    private static class Node<E> {
        final int hash;
        final E item;
        Node<E> next;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }
    //массив <корзин>
    private Node<E>[] table;
    private int size = 0;
    //нач емкость массива по умолчанию
    private static final int def_capacity = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[def_capacity];
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
        if (size > 0) {
            for (int i = 0; i < table.length; ++i) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public boolean add(E e) {
        //обработка null
        int hash = (e == null) ? 0 : e.hashCode();
        //вычисляем индекс в масс
        int index = Math.abs(hash) % table.length;
        //проверка - есть ли эл в этой корзинке
        Node<E> current = table[index];
        while (current != null) {
            //если хеш совпал и элементы равны (через equals), то элемент уже есть
            if (current.hash == hash && (current.item == e || (e != null && e.equals(current.item)))) {
                return false;   //повторку не добавим
            }
            current = current.next;
        }
        //если не найден - создаем новый узел в начале
        Node<E> newNode = new Node<>(hash, e, table[index]);
        table[index] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = Math.abs(hash) % table.length;

        Node<E> current = table[index];
        Node<E> prev = null;    //

        while (current != null) {
            if (current.hash == hash && (current.item == o || (o != null && o.equals(current.item)))) {
                //если удаляемый - голова
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    //в ином случае
                    prev.next = current.next;
                }
                size--;
                return true;    //удалили
            }
            //смещаем указатели
            prev = current;
            current = current.next;
        }
        //эл не найден(
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = Math.abs(hash) % table.length;

        Node<E> current = table[index];
        while (current != null) {
            //если хеш совпал и объекты равны
            if (current.hash == hash && (current.item == o || (o != null && o.equals(current.item)))) {
                return true;
            }
            current = current.next;
        }

        return false; //эл не найден
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        //проход по всем корзинам массива
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            //проход по списку в каждой корзине
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                //если элемент ссылается сам на себя
                sb.append(current.item == this ? "(this Collection)" : current.item);
                first = false;
                current = current.next;
            }
        }

        sb.append(']');
        return sb.toString();
    }

    //заглушки остальных методов
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
