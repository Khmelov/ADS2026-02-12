package by.it.group551003.klimuk.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    private Object[] elements;
    private int size = 0;

    public ListA() {
        elements = new Object[10];
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("[");
        for(int i = 0; i < this.size(); i++){
            build.append(this.elements[i]);
            if (i < this.size() - 1) {
                build.append(", ");
            }
        }
        build.append("]");
        return build.toString();
    }

    @Override
    public boolean add(E e) {
        try{
            ensureLeng();
            elements[size] = e;
            size++;
        } catch (Throwable er) {
            return false;
        }
        return true;


    }

    @Override
    public E remove(int index) {
        if(index >= 0 && index < this.size()){
            Object elem = elements[index];
            for (int i = index; i < size - 1 ; i++) {
                elements[i] = elements[i+1];
            }
            elements[--size] = null;
            return (E) elem;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private void ensureLeng(){
        if(size == elements.length){
            int newSize = elements.length + (elements.length >> 1);
            Object[] newArr = new Object[newSize];
            for(int i = 0; i < size; i++){
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    @Override
    public void add(int index, E element) {
        if(index >= 0 && index < this.size() && element != null){
            ensureLeng();
            for (int i = size; i > index ; i--) {
                elements[i] = elements[i-1];
            }
            elements[index] = element;
            size++;
        }
    }

    @Override
    public boolean remove(Object o) {
        if(o != null){
            for(int i = 0; i < size; i++){
                if(o.equals(elements[i])){
                    this.remove(i);
                    return true;
                }
            }
        } else{
            for(int i = 0; i < size; i++){
                if(elements[i] == null){
                    this.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index >= 0 && index < this.size() && element != null){
            E lastElem = (E) elements[index];
            elements[index] = element;
            return lastElem;
        }
        return null;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i = size - 1;i >= 0;i--){
            this.remove(i);
        }
    }

    @Override
    public int indexOf(Object o) {
        if(o != null){
            for(int i = 0; i < size; i++){
                if(o.equals(elements[i])){
                    return i;
                }
            }
        }else{
            for(int i = 0; i < size; i++){
                if(elements[i] == null){
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if(index >= 0 && index < this.size()){
            return (E) elements[index];
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        if(o != null){
            for(int i = 0; i < size; i++){
                if(o.equals(elements[i])){
                    return true;
                }
            }
        }else {
            for(int i = 0; i < size; i++){
                if(elements[i] == null){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o != null){
            for(int i = size - 1; i >= 0; i--){
                if(o.equals(elements[i])){
                    return i;
                }
            }
        } else{
            for(int i = size - 1; i >= 0; i--){
                if(elements[i] == null){
                    return i;
                }
            }
        }
        return -1;
    }


    private void ensureCapacityFor(int additionalSize) {
        while (size + additionalSize > elements.length) {
            int newSize = elements.length + (elements.length >> 1);
            if (newSize < size + additionalSize) {
                newSize = size + additionalSize;
            }
            Object[] newArr = new Object[newSize];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        ensureCapacityFor(c.size());
        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size || c == null || c.isEmpty()) {
            return false;
        }
        ensureCapacityFor(c.size());
        int i = index;
        for (E element : c) {
            add(i++, element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }


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
