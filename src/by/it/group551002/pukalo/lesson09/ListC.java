package by.it.group551002.pukalo.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListC<E> implements List<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private final Node<E> dummy = new Node<>(null); // фиктивный головной узел
    private int size = 0;

    // ===================== ОБЯЗАТЕЛЬНЫЕ =====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = dummy.next;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> node = new Node<>(e);
        Node<E> cur = dummy;
        while (cur.next != null) cur = cur.next;
        cur.next = node;
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        Node<E> prev = nodeAt(index - 1); // dummy при index == 0
        Node<E> node = new Node<>(element);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> prev = nodeAt(index - 1);
        Node<E> target = prev.next;
        prev.next = target.next;
        E value = target.value;
        target.next = null;
        target.value = null;
        size--;
        return value;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> prev = dummy;
        while (prev.next != null) {
            if (Objects.equals(prev.next.value, o)) {
                Node<E> target = prev.next;
                prev.next = target.next;
                target.next = null;
                target.value = null;
                size--;
                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
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
        Node<E> cur = dummy.next;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.next = null;
            cur.value = null;
            cur = next;
        }
        dummy.next = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> cur = dummy.next; cur != null; cur = cur.next, i++) {
            if (Objects.equals(cur.value, o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = 0, found = -1;
        for (Node<E> cur = dummy.next; cur != null; cur = cur.next, i++) {
            if (Objects.equals(cur.value, o)) found = i;
        }
        return found;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // ===================== ОБЯЗАТЕЛЬНЫЕ (Collection-операции) =====================

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        boolean changed = false;
        int i = index;
        for (E e : c) {
            add(i++, e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> prev = dummy;
        while (prev.next != null) {
            if (c.contains(prev.next.value)) {
                Node<E> target = prev.next;
                prev.next = target.next;
                target.next = null;
                target.value = null;
                size--;
                changed = true;
            } else {
                prev = prev.next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> prev = dummy;
        while (prev.next != null) {
            if (!c.contains(prev.next.value)) {
                Node<E> target = prev.next;
                prev.next = target.next;
                target.next = null;
                target.value = null;
                size--;
                changed = true;
            } else {
                prev = prev.next;
            }
        }
        return changed;
    }

    // ===================== ВСПОМОГАТЕЛЬНЫЕ =====================

    // Возвращает узел по индексу. Для index == -1 вернёт dummy.
    private Node<E> nodeAt(int index) {
        Node<E> cur = dummy;
        for (int i = 0; i <= index; i++) cur = cur.next;
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // ===================== ОПЦИОНАЛЬНЫЕ =====================

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = dummy.next; cur != null; cur = cur.next)
            arr[i++] = cur.value;
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> cur = dummy.next; cur != null; cur = cur.next)
            a[i++] = (T) cur.value;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListC<E> sub = new ListC<>();
        Node<E> cur = nodeAt(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.value);
            cur = cur.next;
        }
        return sub;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = dummy.next;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                E v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() { return listIterator(0); }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);
        return new ListIterator<E>() {
            Node<E> prev = nodeAt(index - 1); // узел перед текущей позицией
            Node<E> cur  = prev.next;         // текущий узел
            int idx = index;
            Node<E> last = null;

            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                last = cur;
                E v = cur.value;
                prev = cur;
                cur = cur.next;
                idx++;
                return v;
            }
            @Override public boolean hasPrevious() { return prev != dummy; }
            @Override public E previous() {
                if (prev == dummy) throw new NoSuchElementException();
                last = prev;
                cur = prev;
                prev = nodeAt(idx - 2); // O(n), но просто
                idx--;
                return cur.value;
            }
            @Override public int nextIndex() { return idx; }
            @Override public int previousIndex() { return idx - 1; }
            @Override public void remove() {
                if (last == null) throw new IllegalStateException();
                // проще всего — удалить по индексу
                // но last может быть уже удалён — используем ListC.this.remove
                ListC.this.remove(idx - (last == cur ? 0 : 1));
                last = null;
                // пересобираем указатели
                prev = nodeAt(idx - 1);
                cur = prev.next;
            }
            @Override public void set(E e) {
                if (last == null) throw new IllegalStateException();
                last.value = e;
            }
            @Override public void add(E e) {
                Node<E> node = new Node<>(e);
                node.next = cur;
                prev.next = node;
                prev = node;
                idx++;
                size++;
                last = null;
            }
        };
    }
}