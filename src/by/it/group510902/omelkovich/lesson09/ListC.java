package by.it.group510902.omelkovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private Object[] elements = new Object[10];
    private int size = 0;
    private void resize(){
        Object[] newArr = new Object[elements.length * 3 / 2 + 1];
        for(int i = 0; i < elements.length; i++){
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if(size == 0){
            return "[]";
        }
        String result = "[";
        for(int i = 0; i < size; i++){
            result = result + elements[i];
            if(i < size - 1){
                result = result + ", ";
            }
        }
        return result + "]";
    }

    @Override
    public boolean add(E e) {
        if(size == elements.length){
            resize();
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index= " + index + ", Size= " + size);
        }
        E oldEL = (E) elements[index];
        for(int i = index; i < size - 1; i++){
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return oldEL;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index= " + index + ", Size= " + size);
        }
        if(size == elements.length){
            resize();
        }
        for(int i = size; i > index; i--){
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index >= 0){
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index= " + index + ", Size= " + size);
        }
        E oldEL = (E) elements[index];
        elements[index] = element;
        return oldEL;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++){
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if(o == null){
            for(int i = 0; i < size; i++){
                if(elements[i] == null) return i;
            }
        }
        else{
            for(int i = 0; i < size; i++){
                if(o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index= " + index + ", Size= " + size);
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < size; i++){
            if(o.equals(elements[i])){
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == null){
            for(int i = size - 1; i >= 0; i--){
                if(elements[i] == null) return i;
            }
        }
        else{
            for(int i = size - 1; i >= 0; i--){
                if(o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(c == null){
            throw new NullPointerException("null");
        }
        for(Object element : c){
            if(!contains(element)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c == null){
            throw new NullPointerException("null");
        }
        boolean change = false;
        for(E element : c){
            add(element);
            change = true;
        }
        return change;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(c == null){
            throw new NullPointerException("null");
        }
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index= " + index + ", Size= " + size);
        }
        if(c.isEmpty()){
            return false;
        }
        boolean change = false;
        int i = index;
        for(E element : c){
            add(i, element);
            i++;
            change = true;
        }
        return change;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("null");
        }
        boolean change = false;
        for (int i = 0; i < size; i++) {
            if(c.contains(elements[i])){
                remove(i);
                i--;
                change = true;
            }
        }
        return change;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("null");
        }
        boolean change = false;
        for (int i = 0; i < size; i++) {
            if(!c.contains(elements[i])){
                remove(i);
                i--;
                change = true;
            }
        }
        return change;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
