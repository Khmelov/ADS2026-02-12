package by.it.group510901.parkhomenko.lesson09;

import java.util.*;
//действия сразу с группой элементов: добавить все, удалить все, оставить только нужные

@SuppressWarnings("unchecked")


public class ListC<E> implements List<E> {


    private Object[] data = new Object[10];
    private int size = 0;

    // Если массив заполнен - создаём вдвое больший и переписываем в него элементы.
    // Вызывается перед каждым добавлением, чтобы для нового элемента всегда было место.
    private void grow() {
        if (size == data.length) {
            Object[] bigger = new Object[size * 2];
            for (int i = 0; i < size; i++) bigger[i] = data[i];
            data = bigger;
        }
    }


    // Превращает список в текст вида "[5, 7, 3]" - точно как у ArrayList (тест сравнивает эти строки).
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append(']').toString();
    }

    // Добавляет элемент e в конец списка.
    @Override
    public boolean add(E e) {
        grow();
        data[size++] = e;
        return true;
    }

    // Удаляет элемент с номером index и возвращает его.
    @Override
    public E remove(int index) {
        E old = (E) data[index];
        for (int i = index; i < size - 1; i++) data[i] = data[i + 1];
        data[--size] = null;
        return old;
    }

    // Возвращает, сколько элементов сейчас в списке.
    @Override
    public int size() {
        return size;
    }

    // Вставляет element на место index; всё, что было с этого места и правее, сдвигается вправо.
    @Override
    public void add(int index, E element) {
        grow();
        for (int i = size; i > index; i--) data[i] = data[i - 1];
        data[index] = element;
        size++;
    }

    // Удаляет первый элемент, равный o. Возвращает true, если удалил, и false, если такого не было.
    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    // Заменяет элемент на месте index на element и возвращает старый элемент.
    @Override
    public E set(int index, E element) {
        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    // Отвечает, пуст ли список: true, если элементов нет.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Удаляет все элементы: заводим новый пустой склад и обнуляем счётчик.
    @Override
    public void clear() {
        data = new Object[10];
        size = 0;
    }

    // Ищет o слева направо и возвращает номер первого совпадения или -1, если такого нет.
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }

    // Возвращает элемент с номером index.
    @Override
    public E get(int index) {
        return (E) data[index];
    }

    // Отвечает, есть ли o в списке: есть, если indexOf нашёл номер 0 или больше.
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Ищет o справа налево и возвращает номер последнего совпадения или -1, если такого нет.
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--)
            if (Objects.equals(o, data[i])) return i;
        return -1;
    }

    // Отвечает, есть ли в списке ВСЕ элементы из коллекции c.
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)                      // перебираем каждый элемент из c
            if (!contains(o)) return false;     // хоть одного нет в нашем списке - сразу «нет»
        return true;                            // все нашлись - «да»
    }

    // Добавляет в конец списка все элементы из c (по очереди, в том порядке, в каком их отдаёт c).
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) add(e);       // каждый элемент из c добавляем в конец
        return !c.isEmpty();        // true, если что-то добавили (c была не пустой)
    }

    // Вставляет все элементы из c подряд, начиная с места index.
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) add(index++, e);  // вставляем на index, затем index + 1, чтобы следующий встал сразу после
        return !c.isEmpty();            // true, если что-то добавили (c была не пустой)
    }

    // Удаляет из списка все элементы, которые есть в c (оставляет те, которых в c НЕТ).
    @Override
    public boolean removeAll(Collection<?> c) { return filter(c, false); }

    // Оставляет в списке только элементы, которые есть в c (остальные удаляет).
    @Override
    public boolean retainAll(Collection<?> c) { return filter(c, true); }

    // Оставляет элементы, для которых c.contains(x) == keep, за один проход (сжатие массива).
    // Общий помощник для removeAll и retainAll. Возвращает true, если список изменился.
    private boolean filter(Collection<?> c, boolean keep) {
        int j = 0;                                                  // j - куда класть следующий оставшийся элемент
        for (int i = 0; i < size; i++)                              // i - какой элемент сейчас проверяем
            if (c.contains(data[i]) == keep) data[j++] = data[i];   // проходит фильтр - переносим его влево на место j
        for (int i = j; i < size; i++) data[i] = null;              // хвост после оставшихся элементов очищаем
        boolean changed = j != size;                                // осталось меньше, чем было, - значит что-то удалили
        size = j;                                                   // новое количество элементов
        return changed;
    }

    // ---------- Не нужно для этого задания ----------
    // Эти методы требует «договор» List, но задание C их не проверяет.
    // Каждый просто выбрасывает ошибку «операция не поддерживается».
    // В комментарии над каждым написано, что он делал бы в настоящем списке.

    // кусок списка от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }

    // «указка» для хождения по списку вперёд и назад, начиная с места index
    @Override
    public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }

    // то же самое, но с начала списка
    @Override
    public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив типа T
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // скопировать элементы в массив Object
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    // «указка» для перебора элементов по одному (нужна для цикла for-each)
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

}
