package by.it.group551004.brovka.lesson10;

public class MyArrayDeque<E> implements java.util.Deque<E> {
    private int last;
    private E[] arr;

    public MyArrayDeque () {
        last = -1;
        arr = (E[]) new Object[10];
    }


    @Override
    public String toString(){
        String str = "[";
        str += arr[0];
        for (int i = 1; i <= last; i++) str = str + ", " + arr[i];
        str += "]";
        return  str;
    }

    @Override
    public boolean add(E e){
        addLast(e);
        return true;
    }

    @Override
    public void addLast(E e){
        incarr();
        last++;
        arr[last] = e;
    }

    @Override
    public void addFirst(E e){
        incarr();
        last++;
        for(int i = last; i > 0; i--) arr[i] = arr[i - 1];
        arr[0] = e;
    }

    @Override
    public E element(){return getFirst();}

    @Override
    public E getFirst(){
        if (last < 0) return null;
        return arr[0];
    }

    @Override
    public E getLast(){
        if (last < 0) return null;
        return arr[last];
    }

    @Override
    public E poll(){
        return pollFirst();
    }

    @Override
    public E pollLast(){
        if (last < 0) return null;
        last--;
        return arr[last + 1];
    }

    @Override
    public E pollFirst(){
        E e;
        if (last < 0) return null;
        e = arr[0];
        for (int i = 0; i < last; i++) arr[i] = arr[i + 1];
        last--;
        return e;
    }

    public void incarr(){
        if (last + 1 == arr.length) {
            E[] narr = (E[]) new Object[arr.length * 2];
            for (int i = 0; i <= last; i++) narr[i] = arr[i];
            arr = narr;
        }
    }

    @Override
    public int size() {return last+1;}

    @Override
    public boolean offerFirst(E e) {
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        return true;
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
        return true;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return true;
    }

    @Override
    public boolean offer(E e) {
        return true;
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
    public void push(E e) {
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        return true;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return true;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return true;
    }

    @Override
    public void clear() {
    }

    @Override
    public java.util.Iterator<E> iterator() {
        return null;
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }
}
