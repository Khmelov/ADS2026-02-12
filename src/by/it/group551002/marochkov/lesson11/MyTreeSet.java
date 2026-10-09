package by.it.group551002.marochkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private E[] data = (E[]) new Object[10];
    private int size;

    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }


    private void grow() {
        E[] newData = (E[]) new Object[data.length * 3 / 2 + 1];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private int lowerBound(Object o){
        int lo = 0;
        int hi = size;

        while(lo < hi){
            int mid = (lo + hi) / 2;
            if(compare(data[mid],o) < 0){
                lo = mid + 1;
            }else{
                hi = mid;
            }
        }

        return lo;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public boolean contains(Object o){
        if(o == null) return false;

        int i = lowerBound(o);
        if(i < size && compare(data[i],o) == 0){
            return true;
        }
        return false;
    }

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
    public boolean add(E element){
        if(element == null) throw new NullPointerException();
        int pos = lowerBound(element);
        if(pos < size && compare(data[pos],element) == 0){
            return false;
        }

        if(size == data.length) grow();
        for(int i = size; i > pos; i--){
            data[i] = data[i-1];
        }
        data[pos] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o){
        if(o == null) return false;

        int pos = lowerBound(o);
        if(pos == size || compare(data[pos],o) != 0){
            return false;
        }
        for(int i = pos; i < size - 1;i++){
            data[i] = data[i+1];

        }
        data[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public void clear(){
        for(int i = 0;i < size;i++){
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        for(int i = 0;i < size; i++){
            str.append(data[i]);
            if(i < size - 1){
                str.append(", ");
            }
        }

        str.append("]");
        return str.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c){
        for(Object x : c){
            if(!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean changed = false;
        for (E x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c){
        int writeIndex = 0;
        boolean isModified = false;
        for(int readIndex = 0;readIndex < size;readIndex++){
            if(!c.contains(data[readIndex])){
                data[writeIndex++] = data[readIndex];
            }else {
                isModified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            data[i] = null;
        }

        size = writeIndex;

        return isModified;
    }



    @Override
    public boolean retainAll(Collection<?> c){
        int writeIndex = 0;
        boolean isModified = false;
        for(int readIndex = 0;readIndex < size;readIndex++){
            if(c.contains(data[readIndex])){
                data[writeIndex++] = data[readIndex];
            }else {
                isModified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) {
            data[i] = null;
        }

        size = writeIndex;

        return isModified;
    }



































}
