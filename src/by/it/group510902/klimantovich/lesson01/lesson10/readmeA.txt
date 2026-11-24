package by.it.group510902.klimantovich.lesson01.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements; // массив для циклического хранения элементов дека
    private int head; // указатель на первый элемент дека
    private int tail; // указатель на позицию за последним элементом
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[16]; // стартовая емкость по дефолту степень двойки
        this.head = 0;
        this.tail = 0;
    }
    @SuppressWarnings("unchecked")
    private void grow() {
        int p = head;
        int r = elements.length;
        int n = r - p; // сколько элементов до конца старого массива
        int newCapacity = r << 1; // удваиваю размер через битовый сдвиг влево
        E[] a = (E[]) new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, n); // копирую хвост от head
        System.arraycopy(elements, 0, a, n, p); // дописываю начало до head
        elements = a;
        head = 0; // сбрасываю head в начало нового массива
        tail = r; // tail встает в конец старых данных
    }
    //Задание на уровень А
    //    Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
    //    и работает на основе приватного массива типа E[]
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int i = head;
        while (i != tail) {
            sb.append(elements[i]);
            i = (i + 1) & (elements.length - 1); // циклический сдвиг индекса
            if (i != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size() {
        return (tail - head) & (elements.length - 1); // считаю размер с учетом кольцевой структуры
    }
    @Override
    public boolean add(E e) {
        addLast(e); // обычный add просто добавляет в конец очереди
        return true;
    }
    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException(); // стандартно дек не хранит null
        head = (head - 1) & (elements.length - 1); // циклический сдвиг head влево
        elements[head] = e;
        if (head == tail) {
            grow(); // если указатели встретились то расширяю массив
        }
    }
    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        elements[tail] = e; // кладу в текущий хвост
        tail = (tail + 1) & (elements.length - 1); // сдвигаю tail вправо по кольцу
        if (tail == head) {
            grow();
        }
    }
    @Override
    public E element() {
        return getFirst(); // element просто возвращает первый объект или кидает эксепшн
    }
    @Override
    public E getFirst() {
        E result = elements[head];
        if (result == null) throw new java.util.NoSuchElementException(); // если дек пуст кидаю ошибку
        return result;
    }
    @Override
    public E getLast() {
        int t = (tail - 1) & (elements.length - 1); // вычисляю индекс последнего реального элемента
        E result = elements[t];
        if (result == null) throw new java.util.NoSuchElementException();
        return result;
    }
    @Override
    public E poll() {
        return pollFirst(); // poll по умолчанию забирает элемент с начала
    }
    @Override
    public E pollFirst() {
        int h = head;
        E result = elements[h];
        if (result == null) return null; // если дек пустой возвращаю null без ошибки
        elements[h] = null; // зануляю ячейку для сборщика мусора
        head = (h + 1) & (elements.length - 1); // сдвигаю голову вправо по кольцу
        return result;
    }
    @Override
    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1); // сдвигаю указатель хвоста влево
        E result = elements[t];
        if (result == null) return null;
        elements[t] = null;
        tail = t; // обновляю позицию хвоста
        return result;
    }
    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override
    public boolean offerLast(E e) { addLast(e); return true; }
    @Override
    public E removeFirst() { return getFirst(); }
    @Override
    public E removeLast() { return getLast(); }
    @Override
    public E peekFirst() { return elements[head]; }
    @Override
    public E peekLast() { return elements[(tail - 1) & (elements.length - 1)]; }
    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }
    @Override
    public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean offer(E e) { return offerLast(e); }
    @Override
    public E remove() { return removeFirst(); }
    @Override
    public E peek() { return peekFirst(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public void clear() { head = 0; tail = 0; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean contains(Object o) { return false; }
    @Override
    public boolean isEmpty() { return head == tail; }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Iterator<E> descendingIterator() { return null; }
    @Override
    public boolean remove(Object o) { return false; }
    @Override
    public void push(E e) { addFirst(e); }
    @Override
    public E pop() { return removeFirst(); }
}
