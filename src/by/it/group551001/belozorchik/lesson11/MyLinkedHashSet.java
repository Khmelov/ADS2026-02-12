package by.it.group551001.belozorchik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final int hash;
        final E item;
        Node<E> nextInBucket; //cсылка на след в той же корзинке

        //
        Node<E> prevAdded;
        Node<E> nextAdded;

        Node(int hash, E item, Node<E> nextInBucket) {
            this.hash = hash;
            this.item = item;
            this.nextInBucket = nextInBucket;
        }
    }

    //массив корзинок
    private Node<E>[] table;
    private int size = 0;
    private static final int def_capacity = 16;

    //указ на начало и конец списка добавленных эл
    private Node<E> headAdded;
    private Node<E> tailAdded;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[def_capacity];
        headAdded = null;
        tailAdded = null;
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
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            headAdded = null;
            tailAdded = null;
            size = 0;
        }
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
            current = current.nextInBucket;
        }

        return false; //эл не найден
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
            current = current.nextInBucket;
        }
        //если не найден - создаем новый узел в начале
        Node<E> newNode = new Node<>(hash, e, table[index]);
        table[index] = newNode;

        if (headAdded == null) {
            //если это самый первый эл
            headAdded = newNode;
            tailAdded = newNode;
        } else {
            //в конец цепочки добавленных эл
            tailAdded.nextAdded = newNode;
            newNode.prevAdded = tailAdded;
            tailAdded = newNode; //сдвигаем хвост на новый эл
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = Math.abs(hash) % table.length;

        Node<E> current = table[index];
        Node<E> prevInBucket = null;    //

        while (current != null) {
            if (current.hash == hash && (current.item == o || (o != null && o.equals(current.item)))) {
                //если удаляемый - голова
                if (prevInBucket == null) {
                    table[index] = current.nextInBucket;
                } else {
                    //в ином случае
                    prevInBucket.nextInBucket = current.nextInBucket;
                }
                //обновляем ссылку у пред эл в списке
                if (current.prevAdded != null) {
                    current.prevAdded.nextAdded = current.nextAdded;
                } else {
                    //если удаляемый был первым
                    headAdded = current.nextAdded;
                }

                //обновляем ссылку у след
                if (current.nextAdded != null) {
                    current.nextAdded.prevAdded = current.prevAdded;
                } else {
                    //если удаляемый был последним
                    tailAdded = current.prevAdded;
                }
                size--;
                return true;    //удалили
            }
            //смещаем указатели
            prevInBucket = current;
            current = current.nextInBucket;
        }
        //эл не найден(
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Node<E> current = headAdded;
        //просто проходим по списку от начала до конца
        while (current != null) {
            sb.append(current.item == this ? "(this Collection)" : current.item);

            current = current.nextAdded; //к след добавленному эл

            if (current != null) {
                sb.append(", ");
            }
        }

        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
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
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        //проход по всему списку коллекции
        Node<E> current = headAdded;
        while (current != null) {
            //сохран след узел заранее, так как текущий может быть удален
            Node<E> nextAddedNode = current.nextAdded;
            //если переданная коллекция не содержит этот эл - удаляем
            if (!c.contains(current.item)) {
                remove(current.item);
                modified = true;
            }
            current = nextAddedNode;
        }
        return modified;
    }

    //заглушки для остальных методов
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
