package by.it.group551001.kotsuba.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private E[] elements;
    private int size;

    public MyTreeSet() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    private int binarySearch(Object o){
        if(o==null) return -1;
        Comparable<Object> key = (Comparable<Object>) o;
        int low=0;
        int high = size -1;
        while(low<=high){
            int mid = (high - low) / 2 + low;
            int cmp = key.compareTo(elements[mid]);
            if(cmp==0) return mid;
            if(cmp>0) low = mid+1;
            else high = mid-1;

        }
        return -(low+1);
    }
    private void ensureCapacity(){
        if(size>=elements.length){
            E[] newElement = (E[]) new Object[elements.length * 2];
            for(int i=0;i<size;i++){
                newElement[i] = elements[i];
            }
            elements = newElement;
        }
    }
    @Override
    public int size(){
        return size;
    }
    @Override
    public boolean isEmpty(){
        return size==0;
    }
    @Override
    public void clear(){
        for(int i=0;i<size;i++){
            elements[i] = null;
        }
        size =0;
    }
    @Override
    public boolean add(E element){
        if(element==null) return false;
        int index = binarySearch(element);
        if(index>=0) return false;

        int idx = -(index+1);
        ensureCapacity();
        for(int i=size;i>idx;i--){
            elements[i] = elements[i-1];
        }
        elements[idx] = element;
        size++;
        return true;
    }
    @Override
    public boolean remove(Object o){
        int index = binarySearch(o);
        if(index<0) return false;

        for(int i=index;i<size-1;i++){
            elements[i] = elements[i+1];
        }
        elements[size-1] = null;
        size--;
        return true;
    }
    @Override
    public boolean contains(Object o){
        int index = binarySearch(o);
        return index >= 0;
    }
    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for(int i=0;i<size;i++){
            if(i>0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean containsAll(Collection<?> c){
        for(Object e:c){
            if(!contains(e)) return false;
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean f=false;
        for(E e:c){
            if(add(e)) f=true;
        }
        return f;
    }
    @Override
    public boolean removeAll(Collection<?> c){
        boolean f=false;
        for(Object e:c){
            if(remove(e)) f=true;
        }
        return f;
    }
    @Override
    public  boolean retainAll(Collection<?> c){
        boolean f =false;
        for(int i=size-1;i>=0;i--){
            if(!c.contains(elements[i])){
                remove(elements[i]);
                f=true;
            }
        }
        return f;
    }

    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}

