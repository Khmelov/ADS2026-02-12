package by.it.group551002.efimchikov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] data;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyArrayDeque(){
        this.data=(E[])new Object[16];
        this.size=0;
        this.capacity=16;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < this.size; i++){
            if (i > 0) sb.append(", ");
            sb.append(this.data[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size(){
        return this.size;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        E[] old=this.data;
        E[] newData=(E[])new Object[capacity*2];
        for(int i=0;i<size;i++){
            newData[i]=old[i];
        }
        this.data=newData;
        capacity*=2;
    }

    @Override
    public boolean add(E element){
        if(this.size==this.capacity){
            this.resize();
        }
        this.data[this.size]=element;
        this.size++;
        return true;
    }

    @Override
    public void addFirst(E element){
        if(this.size==this.capacity){
            this.resize();
        }
        for(int i=size;i>0;i--){
            this.data[i]=this.data[i-1];
        }
        this.data[0]=element;
        size++;
    }

    @Override
    public void addLast(E element){
        if(this.size==this.capacity){
            this.resize();
        }
        this.data[this.size]=element;
        this.size++;
    }

    @Override
    public E element(){
        if(this.size>0)
            return this.data[0];
        return null;
    }

    @Override
    public E getFirst(){
        return this.element();
    }

    @Override
    public E getLast(){
        return this.data[size-1];
    }

    @Override
    public E poll(){
        if(this.size<=0)
            return null;
        E out=this.data[0];
        for(int i=0;i<size;i++)
            this.data[i]=this.data[i+1];
        this.size--;
        return out;
    }

    @Override
    public E pollFirst(){
        return this.poll();
    }

    @Override
    public E pollLast(){
        if(this.size<=0)
            return null;
        E out=this.data[this.size-1];
        this.data[this.size-1]=null;
        this.size--;
        return out;
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
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
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
    public boolean isEmpty() {
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
    public Iterator<E> descendingIterator() {
        return null;
    }
}

