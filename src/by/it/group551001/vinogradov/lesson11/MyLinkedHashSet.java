package by.it.group551001.vinogradov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> onext;

        Node(E value) {
            this.value = value;
        }
    }
    @SuppressWarnings("unchecked")
    private Node<E>[] table = new Node[100];
    private Node<E> first = new Node<E>(null);;
    private Node<E> last;
    private int size = 0;

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }

        int hash = element.hashCode();
        hash ^= hash >>> 16; // Mix high and low bits

        return (hash & 0x7FFFFFFF) % table.length;
    }

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException("TODO: implement this method");
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    public MyLinkedHashSet() {
        for (int i = 0; i < 100; i++) {
            table[i] = new Node<E>(null);
        }
        last = first;
    }
    
    @Override
    public String toString() {
        String s = "[";
        Node<E> curr = first.onext;
        while (curr != null) {
            if (!s.equals("[")) {
                s += ", ";
            }
            s += curr.value;
            curr = curr.onext;
        }
        return s + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < 100; i++) {
            table[i].next = null;

        }
        size = 0;
        last = first;
        first.onext = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx];
        while(curr.next != null) {
            if (Objects.equals(element, curr.next.value))
                return false;
            curr = curr.next;
        }
        curr.next = new Node<E>(element);
        last.onext = curr.next;
        last = curr.next;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx];
        while(curr.next != null) {
            if (Objects.equals(element, curr.next.value)) {
                Node <E> curr2 = first;
                while(curr2.onext != null) {
                    if (Objects.equals(element, curr2.onext.value)) {
                        curr2.onext = curr2.onext.onext;
                        if (curr2.onext == null) {
                            last = curr2;
                        }
                        break;
                    }
                    curr2 = curr2.onext;
                }
                curr.next = curr.next.next;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        int idx = getIndex(element);
        Node<E> curr = table[idx].next;
        while(curr != null) {
            if (Objects.equals(element, curr.value))
                return true;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        boolean f = true;
        for (Object element : collection) {
            f &= contains(element);
        }
        return f;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean f = false;
        for (E element : collection) {
            f |= add(element);
        }
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean f = false;
        for (Object element : collection) {
            f |= remove(element);
        }
        return f;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean f = false;

        for (int i = 0; i < 100; i++) {
            Node<E> curr = table[i];
            while(curr.next != null) {
                if (!collection.contains(curr.next.value)) {
                    Node <E> curr2 = first;
                    while(curr2.onext != null) {
                        if (Objects.equals(curr.next.value, curr2.onext.value)) {
                            curr2.onext = curr2.onext.onext;
                            if (curr2.onext == null) {
                                last = curr2;
                            }
                            break;
                        }
                        curr2 = curr2.onext;
                    }
                    curr.next = curr.next.next;
                    size--;
                    f = true;
                }
                else curr = curr.next;
            }
        }

        return f;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                 Остальные методы интерфейса                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw notImplemented();
    }

    @Override
    public Object[] toArray() {
        throw notImplemented();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw notImplemented();
    }
}
