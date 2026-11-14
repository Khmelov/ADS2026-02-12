package by.it.group551002.rybik.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E>, Comparator<E> {

    private int capacity = 15;
    private Object[] heap = new Object[this.capacity];
    private int size = 0;

    private void siftUp(int i) {
        if (i == 0) return;

        int parent = (i - 1) / 2;

        if( compare((E) heap[parent], (E) heap[i]) < 0 ){
            E temp = (E) heap[parent];
            heap[parent] = heap[i];
            heap[i] = temp;
            siftUp(parent);
        }
        return;
    }

    private void siftDown(int i) {

        int left = 2*i + 1;
        if(left >= size) return;

        int right = left + 1;
        int biggest = left;

        if (right < size && compare((E) heap[left], (E) heap[right]) < 0) {
            biggest = right;
        }
        if( compare( (E) heap[biggest], (E) heap[i]) > 0 ){
            E temp = (E) heap[biggest];
            heap[biggest] = heap[i];
            heap[i] = temp;
            siftDown(biggest);
        }

    }

    @Override
    public int compare(E o1, E o2) {
        return o2.compareTo(o1);
    }



    public String toString(){
        if(size == 0 ) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < size; i++){
            sb.append(heap[i]);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }



    @Override
    public int size() {return this.size;}

    @Override
    public void clear() {
        for(int i = 0; i < size; i++){
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if(size == capacity){
            capacity  *= 2;
            Object[] newHeap = new Object[this.capacity];
            for (int i = 0; i < size; i++){
                newHeap[i] = heap[i];
            }
            newHeap[size] = e;

            heap = newHeap;
        } else {
            heap[size] = e;
        }
        siftUp(size++);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (size == 0)
            throw new NoSuchElementException();
        E element = (E) heap[0];
        heap[0] = heap[size-1] ;
        size--;
        heap[size] = null;
        siftDown(0);


        return element;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++){
            if(Objects.equals(heap[i], o))
                return true;
        }
        return false;
    }

    @Override
    public E poll() {
        return remove();
    }

    @Override
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
    }

    @Override
    public E element() {
        return peek();
    }

    @Override
    public boolean isEmpty() {
        return (size == 0) ? true : false;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection c) {
        boolean res = false;
        for (Object element : c) {
            add((E) element);
            res = true;
        }
        return res;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                heap[i] = heap[size - 1];
                heap[size - 1] = null;
                size--;

                if (i < size) {
                    if (i > 0 && compare((E) heap[(i - 1) / 2], (E) heap[i]) < 0) {
                        siftUp(i);
                    } else {
                        siftDown(i);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        Objects.requireNonNull(c);

        if (c == this) {
            boolean res = size > 0;
            clear();
            return res;
        }

        int oldSize = size;
        int write = 0;

        for (int read = 0; read < oldSize; read++) {
            if (!c.contains(heap[read])) {
                heap[write++] = heap[read];
            }
        }

        if (write == oldSize) {
            return false;
        }

        for (int i = write; i < oldSize; i++) {
            heap[i] = null;
        }

        size = write;

        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        Objects.requireNonNull(c);

        int oldSize = size;
        int write = 0;

        for (int read = 0; read < oldSize; read++) {
            if (c.contains(heap[read])) {
                heap[write++] = heap[read];
            }
        }


        if (write == oldSize) {
            return false;
        }

        for (int i = write; i < oldSize; i++) {
            heap[i] = null;
        }

        size = write;

        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }

        return true;
    }



    /// //
    ///
    ///



    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
