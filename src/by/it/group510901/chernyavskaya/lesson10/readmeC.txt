import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements = (E[]) new Comparable[11];
    private int size = 0;

    private void swap(int i, int j) {
        E temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private boolean isLess(int i, int j) {
        return ((Comparable<? super E>) elements[i]).compareTo(elements[j]) < 0;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (!isLess(i, parent)) break;
            swap(i, parent);
            i = parent;
        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;

            if (right < size && isLess(right, left)) smallest = right;
            if (!isLess(smallest, i)) break;

            swap(i, smallest);
            i = smallest;
        }
    }

    private void resize() {
        E[] newArr = (E[]) new Comparable[elements.length * 2];
        for (int i = 0; i < size; i++) newArr[i] = elements[i];
        elements = newArr;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        String res = "[";
        for (int i = 0; i < size; i++) {
            res += elements[i] + (i < size - 1 ? ", " : "");
        }
        return res + "]";
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) resize();

        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;

        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return true;
        }
        return false;
    }

    private void removeAt(int i) {
        size--;
        elements[i] = elements[size];
        elements[size] = null;
        if (i < size) {
            siftDown(i);
            siftUp(i);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            while (contains(item)) {
                remove(item);
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
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    // =========================================================================
    // Заглушки для интерфейса Queue
    // =========================================================================
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}