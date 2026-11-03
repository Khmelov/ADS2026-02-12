package by.it.group510902.klimantovich.lesson01.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {
    private E[] elements; // массив для хранения бинарной кучи
    private int size; // счетчик текущего количества элементов в очереди
    public MyPriorityQueue() {
        this.elements = (E[]) new Object[11]; // выделяю стартовое место на 11 элементов
        this.size = 0;
    }
    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2]; // удваиваю размер массива при переполнении
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
    private void siftUp(int i, E e) {
        Comparable<? super E> key = (Comparable<? super E>) e;
        while (i > 0) {
            int parent = (i - 1) >>> 1; // вычисляю индекс родительского узла
            Object p = elements[parent];
            if (key.compareTo((E) p) >= 0) break; // если текущий элемент больше или равен родителю то стоп
            elements[i] = (E) p; // сдвигаю родителя вниз
            i = parent;
        }
        elements[i] = e; // ставлю элемент на итоговую правильную позицию
    }
    private void siftDown(int i, E e) {
        Comparable<? super E> key = (Comparable<? super E>) e;
        int half = size >>> 1; // элементы после half не имеют дочерних узлов
        while (i < half) {
            int child = (i << 1) + 1; // левый дочерний узел
            Object c = elements[child];
            int right = child + 1; // правый дочерний узел
            if (right < size && ((Comparable<? super E>) c).compareTo((E) elements[right]) > 0) {
                child = right; // выбираю наименьшего из двух детей
                c = elements[child];
            }
            if (key.compareTo((E) c) <= 0) break; // если элемент меньше или равен ребенку то стоп
            elements[i] = (E) c; // поднимаю ребенка наверх
            i = child;
        }
        elements[i] = e; // фиксирую элемент в куче
    }
    //Задание на уровень C
    //    Создайте class MyPriorityQueue<E>, который реализует интерфейс Queue<E>
    //    и работает на основе кучи, построенной на приватном массиве типа E[]
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // собираю элементы кучи в строку по порядку их в массиве
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size() {
        return size; // возвращаю размер очереди
    }
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null; // зануляю все ссылки в куче для GC
        size = 0;
    }
    @Override
    public boolean add(E e) {
        return offer(e); // метод add просто вызывает offer
    }
    @Override
    public E remove() {
        E result = poll();
        if (result == null) throw new java.util.NoSuchElementException(); // если пустая кидаю ошибку
        return result;
    }
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return true; // ищу совпадение перебором массива кучи
        }
        return false;
    }
    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException(); // куча не должна принимать null значения
        if (size >= elements.length) grow(); // если массив полный расширяю его
        if (size == 0) elements[0] = e; // если первый элемент то просто кладу в корень
        else siftUp(size, e); // просеиваю добавленный элемент наверх на свое место
        size++;
        return true;
    }
    @Override
    public E poll() {
        if (size == 0) return null; // если куча пустая возвращаю null
        int s = --size;
        E result = elements[0]; // забираю корень кучи (минимальный элемент)
        E x = elements[s]; // беру последний элемент
        elements[s] = null; // стираю последнюю ячейку
        if (s != 0) siftDown(0, x); // просеиваю бывший хвост вниз с самого верха
        return result;
    }
    @Override
    public E peek() {
        return (size == 0) ? null : elements[0]; // просто смотрю на корень кучи без удаления
    }
    @Override
    public E element() {
        E result = peek();
        if (result == null) throw new java.util.NoSuchElementException();
        return result;
    }
    @Override
    public boolean booleanValue() { return false; }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверка на пустоту кучи
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного элемента нет то false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) modified = true; // добавляю все элементы коллекции в кучу
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                removeAt(i); // удаляю все элементы входящие в коллекцию c
                modified = true;
            }
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                removeAt(i); // удаляю всё кроме элементов из коллекции c
                modified = true;
            }
        }
        return modified;
    }
    private void removeAt(int i) {
        int s = --size;
        if (s == i) elements[i] = null; // если удаляю последний элемент просто стираю ячейку
        else {
            E moved = elements[s];
            elements[s] = null;
            siftDown(i, moved); // просеиваю элемент вниз
            if (elements[i] == moved) siftUp(i, moved); // если он не сдвинулся вниз то просеиваю вверх
        }
    }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i); // удаляю конкретный объект если нашла
                return true;
            }
        }
        return false;
    }
}
