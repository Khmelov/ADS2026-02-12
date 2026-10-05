package by.it.group551002.marochkov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] data = (E[]) new Object[10];
    private int size;

    private int compare(E a, E b){
        return ((Comparable<E>)a).compareTo(b);
    }

    private void grow() {
        E[] newData = (E[]) new Object[data.length * 3 / 2 + 1];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private void swap(int i,int j){
        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private void siftUp(int i){

        while(i > 0){
            int parent = (i - 1) / 2;
            if(compare(data[i],data[parent]) < 0){
                swap(i,parent);
                i = parent;
            }else {
                break;
            }
        }
    }

    private void siftDown(int i){
        while((2*i + 1) < size){
            int child = 2*i + 1;
            if(child + 1 < size && compare(data[child],data[child + 1]) > 0){
                child++;
            }

            if(compare(data[child],data[i]) < 0){
                swap(child,i);
                i = child;
            }else{
                break;
            }
        }
    }

    private void heapify(){
        for (int i = size / 2 - 1;i >= 0;i--){
            siftDown(i);
        }
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
    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public void clear(){
        for (int i = 0; i < size; i++){
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean offer(E element){
        if (element == null) throw new NullPointerException();
        if(size == data.length){
            grow();
        }

        data[size] = element;
        size++;
        siftUp(size-1);

        return true;
    }


    @Override
    public boolean add(E element){
        return offer(element);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E peek(){
        if(data[0] != null){
            return data[0];
        }else {
            return null;
        }
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public E poll(){
        if (size == 0) return null;
        E result = data[0];

        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        siftDown(0);
        return result;
    }

    @Override
    public E remove(){
        if(size == 0) throw new NoSuchElementException();

        return poll();
    }


    @Override
    public boolean contains(Object o){
        for(int i = 0;i < size; i++){
           if(o.equals(data[i])) return true;
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
    public boolean containsAll(Collection<?> c){
        for(Object x : c){
            if(!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c){
        for(E x : c){
            add(x);
        }
        return true;
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
        if(size != writeIndex) {
            size = writeIndex;
            heapify();
        }
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
        if(size != writeIndex) {
            size = writeIndex;
            heapify();
        }
        return isModified;
    }

















}
