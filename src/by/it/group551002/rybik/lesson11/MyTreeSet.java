package by.it.group551002.rybik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.*;


public class MyTreeSet<E extends Comparable<E>> implements Set<E>  {


    private int capacity = 20;
    private int size = 0;
    E[] array = (E[]) new Comparable[capacity];


    private void resize(int newCapacity){
        if(newCapacity == 0){
            array = (E[]) new Comparable[20];
            this.capacity = 20;
        } else {
            int newestCapacity = capacity * 2;
            array = Arrays.copyOf(array, newCapacity);
            capacity = newestCapacity;
        }
    }

    private int binSearch(E target) {
        int left = 0;
        int right = size;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (array[mid].compareTo(target) < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public String toString(){
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++){
            sb.append(array[i]);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        Arrays.fill(this.array, null);
        resize(0);
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = binSearch(e);
        if( index < size && array[index].compareTo(e) == 0)
            return false;
        else {
            if (size == capacity) {
                resize(capacity * 2);
            }
            System.arraycopy(array, index, array, index + 1, size - index);
            array[index] = e;
            size++;
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binSearch((E) o);
        if( index < size && array[index].compareTo((E) o) == 0){
            System.arraycopy(array, index+1, array, index, size - index - 1);
            size--;
            return true;
        } else
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = binSearch((E) o);
        if( index < size && array[index].compareTo((E) o) == 0){ return true;}
        else return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for(Object element : c){
            if(!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;
        for(Object element : c){
            if(add((E) element)) res = true;
        }

        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean res = false;
        for (int i = 0; i < size; i++){
            if(!c.contains(array[i])){
                res = true;
                remove(array[i]);
                i--;
            }
        }

        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for(Object element : c){
            if(remove((E) element)) res = true;
        }

        return res;
    }


    /// /
    ///
    ///



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


}
