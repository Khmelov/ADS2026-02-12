package by.it.group510902.klimantovich.lesson01.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
public class MyTreeSet<E> implements Set<E> {
    private Object[] elements; // внутренний массив для хранения элементов в отсортированном виде
    private int size; // счетчик уникальных элементов
    public MyTreeSet() {
        this.elements = new Object[10]; // стартовая базовая емкость массива
        this.size = 0;
    }
    private void grow() {
        Object[] newElements = new Object[elements.length * 2]; // удваиваю размер массива при заполнении
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> k = (Comparable<? super E>) key;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = k.compareTo((E) elements[mid]); // сравниваю элементы для поиска нужного индекса
            if (cmp > 0) low = mid + 1;
            else if (cmp < 0) high = mid - 1;
            else return mid; // возвращаю индекс если точное совпадение
        }
        return -(low + 1); // если не нашли, возвращаю позицию гипотетической вставки со знаком минус
    }
    //Задание на уровень C
    //    Создайте class MyTreeSet<E>, который реализует интерфейс Set<E>
    //    и работает на основе отсортированного массива (любым способом)
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //    Метод toString() должен выводить элементы в порядке их возрастания
    //    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    //    быть такими же как в методе toString() обычной коллекции
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // собираю элементы, они уже идут по возрастанию
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public int size() {
        return size; // возвращаю текущее количество элементов
    }
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null; // зануляю массив для очистки памяти
        size = 0;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверка множества на пустоту
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // TreeSet стандартно не принимает null элементы
        int index = binarySearch(e);
        if (index >= 0) return false; // элемент уже есть во множестве, дубликаты не вставляю
        int insertIndex = -(index + 1); // расшифровываю индекс вставки из отрицательного значения
        if (size == elements.length) grow();
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex); // сдвигаю хвост вправо
        elements[insertIndex] = e; // вставляю новый объект на свое место для сохранения сортировки
        size++;
        return true;
    }
    @Override
    public boolean remove(Object o) {
        if (o == null) throw new NullPointerException();
        int index = binarySearch(o);
        if (index < 0) return false; // элемента нет во множестве, удалять нечего
        System.arraycopy(elements, index + 1, elements, index, size - index - 1); // сдвигаю правый хвост влево
        size--;
        elements[size] = null; // зануляю последнюю ячейку
        return true;
    }
    @Override
    public boolean contains(Object o) {
        if (o == null) throw new NullPointerException();
        return binarySearch(o) >= 0; // если бинарный поиск нашел индекс >= 0 то элемент есть
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного элемента из коллекции нет то false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true; // поочередно добавляю новые элементы из коллекции
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) modified = true; // удаляю элементы входящие в коллекцию c
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]); // удаляю всё кроме тех элементов которые перечислены в c
                modified = true;
            } else {
                i++; // иду к следующему элементу если текущий оставили
            }
        }
        return modified;
    }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}
