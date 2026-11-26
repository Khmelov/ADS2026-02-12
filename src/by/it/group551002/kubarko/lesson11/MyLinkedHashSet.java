package by.it.group551002.kubarko.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> orderNext;

        Node() {
            this(null, null);
        }

        Node(T d) {
            this(d, null);
        }

        Node(T d, Node<T> n) {
            data = d;
            next = n;
            orderNext = null;
        }
    }

    public static final int initialCapacity = 16;
    private int mSize = 0;

    @SuppressWarnings("unchecked")
    private Node<E>[] mData = (Node<E>[]) new Node[initialCapacity];

    private int mThreshold = (int) (0.75 * initialCapacity);
    private Node<E> firstAdded = null;
    private Node<E> lastAdded = null;

    private void ensureCapacity() {
        if (mSize <= mThreshold) {
            return;
        }

        @SuppressWarnings("unchecked")
        Node<E>[] newData = (Node<E>[]) new Node[mData.length * 2];

        for (int i = 0; i < mData.length; i++) {
            Node<E> curr = mData[i];

            while (curr != null) {
                Node<E> next = curr.next;

                int hash = (curr.data == null) ? 0 : curr.data.hashCode();
                int index = (hash & 0x7fffffff) % newData.length;

                curr.next = newData[index];
                newData[index] = curr;

                curr = next;
            }
        }

        mData = newData;
        mThreshold = (int) (mData.length * 0.75);
    }

    @Override
    public int size() {
        return mSize;
    }

    @Override
    public boolean isEmpty() {
        return mSize == 0;
    }

    @Override
    public boolean add(E o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;
        Node<E> item = new Node<>(o);

        if (mData[index] == null) {
            mData[index] = item;
        } else {
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

        if (lastAdded == null) {
            firstAdded = item;
        } else {
            lastAdded.orderNext = item;
        }

        lastAdded = item;

        mSize++;
        ensureCapacity();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;

        Node<E> curr = mData[index];
        Node<E> prev = null;

        while (curr != null) {
            if (curr.data == null ? o == null : curr.data.equals(o)) {
                if (prev == null) {
                    mData[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }

                Node<E> orderCurr = firstAdded;
                Node<E> orderPrev = null;

                while (orderCurr != null) {
                    if (orderCurr == curr) {
                        if (orderPrev == null) {
                            firstAdded = orderCurr.orderNext;
                        } else {
                            orderPrev.orderNext = orderCurr.orderNext;
                        }

                        if (lastAdded == orderCurr) {
                            lastAdded = orderPrev;
                        }

                        break;
                    }

                    orderPrev = orderCurr;
                    orderCurr = orderCurr.orderNext;
                }

                curr.next = null;
                curr.orderNext = null;
                mSize--;
                return true;
            }

            prev = curr;
            curr = curr.next;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = firstAdded;

        while (curr != null) {
            sb.append(curr.data);

            if (curr.orderNext != null) {
                sb.append(", ");
            }

            curr = curr.orderNext;
        }

        return sb.append("]").toString();
    }

    @Override
    public void clear() {
        for (int i = 0; i < mData.length; i++) {
            mData[i] = null;
        }

        firstAdded = null;
        lastAdded = null;
        mSize = 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = (hash & 0x7fffffff) % mData.length;

        Node<E> curr = mData[index];

        while (curr != null) {
            if (curr.data == null ? o == null : curr.data.equals(o)) {
                return true;
            }

            curr = curr.next;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;

        for (E o : c) {
            if (add(o)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == this) {
            boolean changed = !isEmpty();
            clear();
            return changed;
        }

        boolean changed = false;

        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == this) {
            return false;
        }

        boolean changed = false;

        for (int i = 0; i < mData.length; i++) {
            Node<E> curr = mData[i];

            while (curr != null) {
                Node<E> next = curr.next;

                if (!c.contains(curr.data)) {
                    remove(curr.data);
                    changed = true;
                }

                curr = next;
            }
        }

        return changed;
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