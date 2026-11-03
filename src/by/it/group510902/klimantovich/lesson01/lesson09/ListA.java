package by.it.group510902.klimantovich.lesson01.lesson09;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
public class ListA<E> implements List<E> {
    private Object[] elements; // массив где храню все элементы списка
    private int size; // счетчик реального количества элементов в списке
    public ListA() {
        this.elements = new Object[10]; // базовый размер массива при создании списка
        this.size = 0; // изначально список пустой
    }
    private void grow() {
        Object[] newElements = new Object[elements.length * 2]; // увеличиваю массив в два раза если не хватает места
        System.arraycopy(elements, 0, newElements, 0, elements.length); // переношу старые данные в новый массив
        elements = newElements; // сохраняю ссылку на новый увеличенный массив
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // собираю элементы в строку через StringBuilder
            if (i < size - 1) {
                sb.append(", "); // ставлю запятые между элементами
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow(); // если массив забит то расширяю его
        }
        elements[size] = e; // кладу элемент в конец и двигаю счетчик
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        E removedElement = (E) elements[index]; // запоминаю удаляемый элемент чтобы его вернуть
        int numMoved = size - index - 1; // считаю сколько элементов надо сдвинуть
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved); // сдвигаю хвост влево стирая элемент
        }
        size--; // уменьшаю общий размер списка
        elements[size] = null; // обнуляю освободившуюся ячейку для сборщика мусора
        return removedElement;
    }
    @Override
    public int size() {
        return size; // просто возвращаю текущий размер
    }
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        return (E) elements[index]; // беру элемент по индексу с приведением типов
    }
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i; // отдельно ищу null чтобы не упало
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i; // ищу обычный объект через equals
            }
        }
        return -1;
    }
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o); // нахожу индекс объекта
        if (index >= 0) {
            remove(index); // если нашла то удаляю по индексу
            return true;
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка индекса
        }
        E oldElement = (E) elements[index]; // сохраняю старое значение
        elements[index] = element; // перезаписываю ячейку новым элементом
        return oldElement;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю пустой ли список
    }
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // зануляю массив чтобы очистить память
        }
        size = 0;
    }
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // проверяю есть ли элемент через indexOf
    }
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // проверка границ
        }
        if (size == elements.length) {
            grow(); // если массив полон увеличиваю его
        }
        System.arraycopy(elements, index, elements, index + 1, size - index); // раздвигаю массив вправо для вставки
        elements[index] = element; // вставляю элемент на нужное место
        size++;
    }
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i; // ищу null с конца массива
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i; // ищу объект через equals с конца
            }
        }
        return -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // если хоть одного элемента нет возвращаю false
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true; // добавляю всю коллекцию в конец
        }
        return modified;
    }
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        boolean modified = false;
        for (E e : c) {
            add(index++, e); // добавляю всю коллекцию начиная со специндекса
            modified = true;
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            while (remove(e)) {
                modified = true; // удаляю все элементы из переданной коллекции
            }
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i); // оставляю только те элементы которые есть в коллекции c
                modified = true;
            }
        }
        return modified;
    }
    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override
    public ListIterator<E> listIterator(int index) { return null; }
    @Override
    public ListIterator<E> listIterator() { return null; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size); // превращаю список в массив фиксированной длины
        return result;
    }
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0; // указатель для итератора
            @Override
            public boolean hasNext() {
                return cursor < size; // проверяем есть ли следующий шаг
            }
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) elements[cursor++]; // выдаю текущий элемент и двигаю курсор дальше
            }
        };
    }
}
