package by.it.group510902.omelkovich.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap = (E[]) new Object[10];
    private int size = 0;

    private int compare(E a, E b){
        return((Comparable<E>) a).compareTo(b);
    }

    private void resize(){
        if(size < heap.length) return;
        E[] bigger = (E[]) new Object[heap.length * 2];
        for(int i = 0; i < size; i++){
             bigger[i] = heap[i];
        }
        heap = bigger;
    }

    private void siftUP(int k, E key){
        while(k > 0){
            int parent = (k - 1) / 2;
            E p = heap[parent];
            if(compare(key, p) >= 0) break; // нашли
            heap[k] = p;
            k = parent;
        }
        heap[k] = key;
    }

    private void siftDOWN(int k, E key){
        int half = size / 2;
        while(k < half){
            int child = 2 * k + 1;
            E c = heap[child];
            int right = child + 1;
            if(right < size && compare(c, heap[right]) > 0){
                child = right;
                c = heap[child];
            }
            if(compare(key, c) <= 0) break; // нашли
            heap[k] = c;
            k = child;
        }
        heap[k] = key;
    }

    private void toheap(){
        for(int i = (size / 2) - 1; i >= 0; i--){
            siftDOWN(i, heap[i]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < size; i++){
            if(i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {return size;}

    @Override
    public void clear() {
        for(int i = 0; i < size; i++){
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if(element == null) throw new NullPointerException();
        resize();
        siftUP(size, element);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if(size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public E poll() {
        if(size == 0) return null;
        E result = heap[0];
        size--;
        E last = heap[size];
        heap[size] = null;
        if(size > 0){
            siftDOWN(0, last);
        }
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if(size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(o == null) return false;
        for(int i = 0; i < size; i++){
            if(o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c){
            if(!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for(E e : c){
            add(e);
            changed = true;
        }
        return changed;
    }

    private boolean bRemove(Collection<?> c, boolean retain){
        if (c == null) throw new NullPointerException();
        int end = size;
        int w = 0;
        for(int i = 0; i < end; i++){
            E e = heap[i];
            boolean remove = c.contains(e) != retain;
            if(!remove){
                heap[w++] = e;
            }
        }
        if(w == end) return false;
        for(int i = w; i < end; i++){
            heap[i] = null;
        }
        size = w;
        toheap();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bRemove(c, true);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bRemove(c, false);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }
}
