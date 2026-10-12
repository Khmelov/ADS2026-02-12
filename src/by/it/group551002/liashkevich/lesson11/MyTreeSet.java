package by.it.group551002.liashkevich.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {

    private int initialCapacity = 16;
    private Object[] template = new Object[initialCapacity];
    private int length = 0;
    private final double loadFactor = 0.75;

    @Override
    public String toString() {
        if (template == null)
            return "[]";
        String result = "[";
        boolean comma = false;
        for (int i = 0; i < length; i++) {
            if (comma)
                result += ", ";
            result += String.valueOf(template[i]);
            comma = true;
        }
        return result + ']';
    }
    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < length; i++) {
            if (o.equals(template[i]))
                return true;
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
    public boolean add(E e) {
        if (((double) length / initialCapacity) >= loadFactor) {
            initialCapacity *= 2;
            Object[] newTemplate = new Object[initialCapacity];
            System.arraycopy(template, 0, newTemplate, 0, length);
            template = newTemplate;
        }
        int left = 0;
        int right = length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = ((Comparable<Object>)e).compareTo(template[mid]);
            if (cmp == 0)
                return false;
            else if (cmp < 0)
                right = mid - 1;
            else
                left = mid + 1;
        }
        System.arraycopy(template, left, template, left + 1, length - left);
        template[left] = e;
        ++length;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;
        int mid = 0;
        int left = 0;
        int right = length - 1;
        while (left <= right) {
            mid = left + (right - left) / 2;
            int cmp = ((Comparable<Object>)o).compareTo(template[mid]);
            if (cmp == 0) {
                found = true;
                break;
            }
            else if (cmp < 0)
                right = mid - 1;
            else
                left = mid + 1;
        }
        if (!found)
            return false;
        System.arraycopy(template, mid + 1, template, mid, length - mid - 1);
        --length;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        int expected = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (c.contains(template[i]))
                ++expected;
        }
        return expected == c.size();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int added = 0;
        for (E col : c) {
            this.add(col);
            ++added;
        }
        return added == c.size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = length - 1; i >= 0; i--) {
            if (!c.contains(template[i])) {
                this.remove(template[i]);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = length - 1; i >= 0; i--) {
            if (c.contains(template[i])) {
                this.remove(template[i]);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        length = 0;
    }
}
