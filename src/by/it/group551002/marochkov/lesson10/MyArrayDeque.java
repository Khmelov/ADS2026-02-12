package by.it.group551002.marochkov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] data = (E[]) new Object[10];
    private int head;
    private  int size;

    private int index(int i) {
        return (head + i) % data.length;
    }

    private void grow() {
        E[] newData = (E[]) new Object[data.length * 3 / 2 + 1];
        for (int i = 0; i < size; i++) {
            newData[i] = data[index(i)];
        }
        data = newData;
        head = 0;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        for(int i = 0;i < size; i++){
            str.append(data[index(i)]);
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
    public Iterator<E> descendingIterator() {
        return null;
    }


    @Override
    public void addLast(E element) {
        if (size == data.length) grow();
        data[index(size)] = element;
        size++;
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
    public boolean add(E element) {
        addLast(element);
        return true;
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
    public void addFirst(E element) {
        if (size == data.length) grow();
        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }

    @Override
    public E getFirst(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return data[head];
    }

    @Override
    public E element(){
        return getFirst();
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
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

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
    public E getLast(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }

        return data[(head + size - 1) % data.length];
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
        if(isEmpty()){
            return null;
        }
        E result = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return result;
    }

    @Override
    public E poll(){
        return pollFirst();
    }

    @Override
    public E pollLast(){
        if(isEmpty()){
            return null;
        }
        E result = data[(head + size - 1) % data.length];
        data[(head + size - 1) % data.length] = null;
        size--;
        return result;
    }

}
