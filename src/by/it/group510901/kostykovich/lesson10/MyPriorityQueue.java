package by.it.group510901.kostykovich.lesson10;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MyPriorityQueue<E> implements Queue<E> {

    private interface ComparatorInterface<E>{
        int compare(E obj1, E obj2);
    }

    private static class MyComparator<E> implements ComparatorInterface<E>{
        @Override
       public int compare(E obj1, E obj2){
           return  ((Comparable<E>) obj1).compareTo(obj2);
       };
    }
    private Object[] heap = new Object[10];
    private MyComparator<E> comparator = new MyComparator<>();
    private int currentSize;
    public int parent(int i){
        return (i - 1)/2;
    }
    public int leftChild(int i){
        return (i*2) + 1;
    }
    public int rightChild(int i){
        return (i*2)+2;
    }

    public MyPriorityQueue() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString(){
        if(currentSize == 0){
            return "[]";
        }else {
            String response = "[";
            for (int i = 0; i < this.size(); i++) {
                response += heap[i];
                if (i < currentSize - 1) {
                    response += ", ";
                }
            }
            response += "]";
            return response;
        }

    }


    @Override
    public int size(){
        return currentSize;
    }
    @Override
    public void clear(){
        if(currentSize == 0){
            return;
        }
        for(int i = 0; i < currentSize; i++){
            heap[i] = null;
        }
        currentSize = 0;
    }

    @Override
    public Spliterator<E> spliterator() {
        return Queue.super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return Queue.super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return Queue.super.parallelStream();
    }

    @Override
    public boolean add(E element){
        if(currentSize == heap.length){
            Object[] newHeap = new Object[heap.length*2];
            for(int i = 0; i<currentSize; i++){
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
        heap[currentSize] = element;
        currentSize++;
        siftUp(currentSize-1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E remove(){
        if(currentSize == 0){
            throw new RuntimeException();
        }
        E removed = (E) heap[0];
        heap[0] = heap[currentSize-1];
        heap[currentSize-1] = null;
        currentSize--;
        siftDown(0);
        return removed;
    }
    @Override
    public boolean isEmpty(){
        return currentSize == 0;
    }
    @Override
    public E peek(){
        if(currentSize == 0){
            return null;
        }else{
            return (E) heap[0];
        }
    }
    @Override
    public boolean contains(Object element){
        if(element == null){
           for(int i = 0; i < currentSize; i++){
                if(heap[i] == null){
                    return true;
                }
            }
        }else {
            for (int i = 0; i < currentSize; i++) {
                if (heap[i].equals(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Queue.super.forEach(action);
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
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return Queue.super.toArray(generator);
    }

    @Override
    public E element(){
        if(currentSize == 0){
            throw new NoSuchElementException();
        }else{
            return (E) heap[0];
        }
    }
    @Override
    public boolean offer(E element){
        return add(element);
    }
    @Override
    public E poll(){
        if(currentSize == 0){
            return null;
        }
        E removed = (E) heap[0];
        heap[0] = heap[currentSize-1];
        heap[currentSize-1] = null;
        currentSize--;
        siftDown(0);
        return removed;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        for(E object : c){
            add(object);
        }
        return true;
    }
    @Override
    public boolean containsAll(Collection<?> c){
        for(Object object : c){
            if(!contains(object)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean removeAll(Collection<?> c){
        int newSize = 0;
        for(int i = 0; i < currentSize; i++){
            if(!c.contains(heap[i])){
                heap[newSize] = heap[i];
                newSize++;
            }
        }
        for(int i = newSize; i < currentSize; i++){
            heap[i] = null;
        }
        int oldSize = currentSize;
        currentSize = newSize;
        for(int i = currentSize-1; i >= 0; i--){
            siftDown(i);
        }
        if(oldSize == newSize){
            return false;
        }
        return true;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return Queue.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < currentSize; i++) {
            if (c.contains(heap[i])) {
                heap[newSize] = heap[i];
                newSize++;
            }
        }
        for (int i = newSize; i < currentSize; i++) {
            heap[i] = null;
        }
        int oldSize = currentSize;
        currentSize = newSize;
        for(int i = currentSize -1; i>=0;i--){
            siftDown(i);
        }
        if(newSize == oldSize){
            return false;
        }
        return true;
    }
    private void siftUp(int index){
        if(currentSize == 0 || index < 0 || index > heap.length){
            throw new IndexOutOfBoundsException();
        }
        while(index > 0 && comparator.compare((E) heap[index], (E) heap[parent(index)])<0){
            E temp = (E) heap[index];
            heap[index] = heap[parent(index)];
            heap[parent(index)] = temp;
            index = parent(index);
        }
    }
    private void siftDown(int index){
        if(leftChild(index) >= currentSize){
            return;
        }


        E smallest = (E) heap[leftChild(index)];
        int smallestIndex = leftChild(index);
        if(rightChild(index) < currentSize){
            E right = (E) heap[rightChild(index)];
            if(comparator.compare(right, smallest) < 0){
                smallest = right;
                smallestIndex = rightChild(index);
            }
        }
        if(comparator.compare((E)heap[index], smallest)>0){
           E temp = (E) heap[index];
           heap[index] = smallest;
           heap[smallestIndex] = temp;
           index = smallestIndex;
           siftDown(index);
        }
    }


}
