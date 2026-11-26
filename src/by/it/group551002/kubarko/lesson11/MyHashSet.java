package by.it.group551002.kubarko.lesson11;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node() {
            this(null, null);
        }

        Node(T d) {
            this(d, null);
        }

        Node(T d, Node<T> n) {
            data = d;
            next = n;
        }
    }

    public static final int initialCapacity = 16;
    private int mSize = 0;
    private Node<E>[] mData = (Node<E>[]) new Node[initialCapacity];
    private int mThreshold = (int) (0.75*initialCapacity);

    private void ensureCapacity(){
        if(mSize <= mThreshold){
            return;
        }

        Node<E>[] newData = (Node<E>[]) new Node[mData.length * 2];
        for(int i = 0; i < mData.length; i++)
        {
            Node<E> curr = mData[i];
            while(curr != null)
            {
                Node<E> next = curr.next;

                int hash = (curr.data == null) ? 0 : curr.data.hashCode();
                int index = (hash & 0x7fffffff) % newData.length;

                curr.next = newData[index];
                newData[index] = curr;

                curr = next;
            }

        }
        mData = newData;
        mThreshold = (int)(mData.length * 0.75);

    }

    @Override
    public int size(){
        return mSize;
    }

    @Override
    public boolean isEmpty(){
        return mSize == 0;
    }

    @Override
    public boolean add(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;
        Node<E> item = new Node<E>((E) o);
        if (mData[index] == null)
            mData[index] = item;
        else {
            Node<E> curr = mData[index];
            while (true) {
                if (curr.data == null ? o == null : curr.data.equals(o)) {
                    return false;
                }

                if (curr.next == null) {
                    curr.next = item;
                    break;
                }

                curr = curr.next;
            }

        }
        mSize++;
        ensureCapacity();
        return true;
    }

    @Override
    public boolean remove(Object o ) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;

        if (mData[index] == null)
            return false;
        else {
            Node<E> curr = mData[index];
            Node<E> prev = null;

            while (curr != null) {
                if (curr.data == null ? o == null : curr.data.equals(o)) {
                    if (prev == null) {
                        mData[index] = curr.next;
                    } else {
                        prev.next = curr.next;
                    }
                    mSize--;
                    return true;
                }
                prev = curr;
                curr = curr.next;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        int count = 0;
        for(int i = 0; i < mData.length; i++)
        {
            Node<E> curr = mData[i];
            while(curr != null)
            {
                sb.append(curr.data);
                count++;
                if(count < mSize) sb.append(", ");
                else {
                    sb.append("]");
                    return sb.toString();
                }
                curr = curr.next;
            }

        }
        sb.append("]");
        return sb.toString();

    }

    @Override
    public void clear() {
        for (int i = 0; i < mData.length; i++) {
            mData[i] = null;
        }
        mSize = 0;
    }
    @Override
    public boolean contains(Object o ){
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;

        if (mData[index] == null)
            return false;
        else {
            Node<E> curr = mData[index];
            while (curr != null) {
                if (curr.data == null ? o == null : curr.data.equals(o)) {
                    return true;
                }
                curr = curr.next;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}
