package by.it.group551004.brovka.lesson10;

public class MyPriorityQueue<E> implements java.util.Queue<E> {
    private E[] arr;
    private int size;

    public MyPriorityQueue() {
        arr = (E[]) new Object[10];
        size = 0;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < size; i++) {
            str += arr[i];
            if (i < size - 1) str += ", ";
        }
        return str + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) arr[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? arr[i] == null : element.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();

        if (size == arr.length) {
            E[] newArr = (E[]) new Object[arr.length * 2];
            for (int i = 0; i < size; i++) newArr[i] = arr[i];
            arr = newArr;
        }

        int index = size++;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((Comparable<E>) element).compareTo(arr[parent]) >= 0) break;
            arr[index] = arr[parent];
            index = parent;
        }

        arr[index] = element;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        E result = arr[0];
        E moved = arr[--size];
        arr[size] = null;

        if (size > 0) siftDown(0, moved);

        return result;
    }

    private void siftDown(int index, E element) {
        int half = size / 2;

        while (index < half) {
            int child = index * 2 + 1;
            int right = child + 1;

            if (right < size &&
                    ((Comparable<E>) arr[child]).compareTo(arr[right]) > 0) {
                child = right;
            }

            if (((Comparable<E>) element).compareTo(arr[child]) <= 0) break;

            arr[index] = arr[child];
            index = child;
        }

        arr[index] = element;
    }

    private void siftUp(int index, E element) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (((Comparable<E>) element).compareTo(arr[parent]) >= 0) break;

            arr[index] = arr[parent];
            index = parent;
        }

        arr[index] = element;
    }

    private E removeAt(int index) {
        int last = --size;

        if (last == index) {
            arr[index] = null;
            return null;
        }

        E moved = arr[last];
        arr[last] = null;


        siftDown(index, moved);

        if (arr[index] == moved) {
            siftUp(index, moved);
            if (arr[index] != moved) return moved;
        }

        return null;
    }

    private boolean bulkRemove(java.util.Collection<?> c, boolean retain) {
        int oldSize = size;
        int newSize = 0;

        for (int i = 0; i < oldSize; i++) {
            if (c.contains(arr[i]) == retain) {
                arr[newSize++] = arr[i];
            }
        }

        if (newSize == oldSize) return false;

        for (int i = newSize; i < oldSize; i++) arr[i] = null;
        size = newSize;

        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, arr[i]);
        }

        return true;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return arr[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return arr[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E element : c) {
            add(element);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return bulkRemove(c, false);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return bulkRemove(c, true);
    }

    @Override
    public boolean remove(Object element) {
        return false;
    }

    @Override
    public java.util.Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}