package by.it.group510902.kudan.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    //узел двусвязного списка, знает и предыдущий и следующий узел
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    //первый и последний узлы списка
    private Node<E> first;
    private Node<E> last;

    private int size;

    //вырезаем узел из списка, соседи начинают ссылаться друг на друга
    private E unlink(Node<E> x) {
        if (x.prev == null) {
            first = x.next;
        } else {
            x.prev.next = x.next;
        }

        if (x.next == null) {
            last = x.prev;
        } else {
            x.next.prev = x.prev;
        }

        size--;
        return x.value;
    }

    @Override
    public String toString() {
        //идем от первого узла к последнему и собираем строку вида [1, 2, 3]
        StringBuilder sb = new StringBuilder("[");

        for (Node<E> x = first; x != null; x = x.next) {
            sb.append(x.value);
            if (x.next != null) {
                sb.append(", ");
            }
        }

        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        //add это то же самое что addLast
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        //идем к нужному узлу с того конца который ближе
        Node<E> x;
        if (index < size / 2) {
            x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
        } else {
            x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
        }

        return unlink(x);
    }

    @Override
    public boolean remove(Object element) {
        //ищем первый узел с таким значением и вырезаем его
        for (Node<E> x = first; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                unlink(x);
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
    public void addFirst(E element) {
        //новый узел ставим перед первым
        Node<E> x = new Node<>(element);
        x.next = first;

        if (first == null) {
            last = x;
        } else {
            first.prev = x;
        }

        first = x;
        size++;
    }

    @Override
    public void addLast(E element) {
        //новый узел ставим после последнего
        Node<E> x = new Node<>(element);
        x.prev = last;

        if (last == null) {
            first = x;
        } else {
            last.next = x;
        }

        last = x;
        size++;
    }

    @Override
    public E element() {
        //element это то же самое что getFirst
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    @Override
    public E poll() {
        //poll это то же самое что pollFirst
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return first == null ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return last == null ? null : unlink(last);
    }

    //дальше методы которых нет в задании, простые сделаны, остальные заглушки
    @Override
    public boolean offerFirst(E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E removeFirst() {
        //getFirst сам выбросит исключение если список пустой
        E x = getFirst();
        pollFirst();
        return x;
    }

    @Override
    public E removeLast() {
        E x = getLast();
        pollLast();
        return x;
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.value;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.value;
    }

    @Override
    public boolean offer(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object element) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (element == null ? x.value == null : element.equals(x.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        return remove(element);
    }

    public boolean removeLastOccurrence(Object o) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public Iterator<E> iterator() { return null; }
    public Iterator<E> descendingIterator() { return null; }
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
}