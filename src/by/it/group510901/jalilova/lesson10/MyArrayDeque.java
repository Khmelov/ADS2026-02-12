package by.it.group510901.jalilova.lesson10;
import java.util.*;
// Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
// и работает на основе приватного массива типа E[]
// БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

/*              toString()
                size()

                add(E element)
                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()*/

public class MyArrayDeque<E> implements Deque<E> {

    private Object[] elements = new Object[16];
    private int size = 0;
    private int head = 0;
    private int tail = 0;

    private void grow (){
        int newCapacity = elements.length == 0 ? 16 : elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, head, newElements, 0, elements.length-head);
        if (head > 0) {
            System.arraycopy(elements, 0, newElements, elements.length-head, head);
        }
        elements = newElements;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            // Вычисляем реальный индекс элемента в массиве
            int index = (head + i) % elements.length;

            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    public boolean add(E element){
        addLast(element);
        return true;
    }

    @Override
    public void addLast(E element){
        if (element == null) {
            throw new NullPointerException(); // Спецификация Deque не разрешает null
        }
        if (size == elements.length) {
            grow(); // Если массив полон, расширяем его
        }
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public void addFirst(E element){
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == elements.length) {
            grow();
        }
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public E poll(){
        if ((E)elements[head]==null){
            return null;
        }
        E temp = (E)elements[head];
        elements[head]=null;
        head = (head + 1) % elements.length;
        size--;
        return temp;
    }

    @Override
    public E pollFirst(){
        return poll();
    }

    @Override
    public E pollLast(){
        if ((E)elements[head]==null){
            return null;
        }
        int prev = (tail - 1 + elements.length) % elements.length;
        E temp = (E)elements[prev];
        elements[prev]=null;
        tail = prev;
        size--;
        return temp;
    }

    @Override
    public E element(){
        if ((E)elements[head]==null){
            throw new NoSuchElementException();
        }
        return (E)elements[head];
    }

    @Override
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if ((E)elements[head]==null){
            throw new NoSuchElementException();
        }
        return (E)elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public int size(){
        return size;
    }




// ЗАГЛУШКИ

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { if (size == 0) throw new NoSuchElementException(); return pollFirst(); }
    @Override public E removeLast() { if (size == 0) throw new NoSuchElementException(); return pollLast(); }
    @Override public E peekFirst() { return size == 0 ? null : (E) elements[head]; }
    @Override public E peekLast() { if (size == 0) return null; return (E) elements[(tail - 1 + elements.length) % elements.length]; }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() { return removeFirst(); }
    @Override public E peek() { return peekFirst(); }
    @Override public void push(E e) { addFirst(e);}
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}
