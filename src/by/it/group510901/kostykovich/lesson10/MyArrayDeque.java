package by.it.group510901.kostykovich.lesson10;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elems = new Object[16];
    private int head;
    private int tail;
    private int currentSize;
    @Override
    public String toString(){
        String response = "[";
        if(this.size() == 0){
            return "[]";
        }else {
            int current = head;
            for (int i=0; i < this.size(); i++) {
                response += elems[current];
                current = (current + 1) % elems.length;
                if(i < this.size()-1){
                    response += ", ";
                }
            }
            response += "]";
        }
        return response;
    }
    @Override
    public int size(){
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Deque.super.forEach(action);
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
        return Deque.super.toArray(generator);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public Deque<E> reversed() {
        return Deque.super.reversed();
    }

    @Override
    public boolean add(E element){
        if(currentSize >= elems.length){
            Object[] newElems = new Object[elems.length * 2];
            int i = head;
            for(int j = 0; j < currentSize; j++){
                newElems[j] = elems[i];
                i = (i+1)%elems.length;
            }
            elems = newElems;
            head = 0;
            tail = currentSize;
            newElems[tail] = element;
            tail = (tail+1)%elems.length;
            currentSize++;
            return true;
        }else{
            elems[tail] = element;
            tail = (tail+1)%elems.length;
            currentSize++;
            return true;
        }

    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public void addFirst(E element){
        if(currentSize >= elems.length){
            Object[] newElems = new Object[elems.length * 2];
            int i = head;
            for(int j = 0; j < currentSize; j++){
                newElems[j] = elems[i];
                i = (i+1)%elems.length;
            }
            elems = newElems;
            tail = currentSize;
            head = elems.length -1 ;
            newElems[head] = element;
            currentSize++;
        }else{
            elems[(head-1+elems.length)%elems.length] = element;
            head = (head-1+elems.length)%elems.length;
            currentSize++;
        }
    }
    @Override
    public void addLast(E element){
        add(element);
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E element(){
        if(currentSize == 0){
            throw new NoSuchElementException();
        }else {
            return (E) elems[head];
        }
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return Deque.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Spliterator<E> spliterator() {
        return Deque.super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return Deque.super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return Deque.super.parallelStream();
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if(currentSize == 0){
            throw new NoSuchElementException();
        }
        return (E) elems[(tail-1+elems.length)%elems.length];
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public E pollFirst(){
        if(currentSize == 0){
            return null;
        }
        E removed = (E) elems[head];
        elems[head] = null;

        head = (head+1)%elems.length;
        currentSize--;
        return removed;
    }
    @Override
    public E poll(){
        return pollFirst();
    }
    @Override
    public E pollLast(){
        if(currentSize == 0){
            return null;
        }
        int currentTail = (tail-1+elems.length)%elems.length;
        E removed = (E) elems[currentTail];
        elems[currentTail] = null;
        tail = currentTail;
        currentSize--;
        return removed;
    }
}
