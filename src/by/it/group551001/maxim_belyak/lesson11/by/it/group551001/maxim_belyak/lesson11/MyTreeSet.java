package by.it.group551001.maxim_belyak.lesson11;


import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Множество на основе отсортированного массива.
 * Поиск выполняется бинарным поиском за O(log n), вставка и удаление
 * сдвигают элементы за O(n). Порядок элементов: по возрастанию.
 * Из стандартной библиотеки используются только интерфейсы
 * Set, Collection, Iterator, Comparator.
 */
public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data = new Object[DEFAULT_CAPACITY];
    private int size;
    private final Comparator<? super E> comparator; // null => естественный порядок

    public MyTreeSet() {
        this.comparator = null;
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    // ---------- вспомогательные методы ----------

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((E) a, (E) b);
        }
        return ((Comparable<Object>) a).compareTo(b);
    }

    /** Как Arrays.binarySearch: индекс элемента или -(точка вставки) - 1. */
    private int search(Object key) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare(data[mid], key);
            if (cmp < 0) {
                lo = mid + 1;
            } else if (cmp > 0) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -(lo + 1);
    }

    private void ensureCapacity() {
        if (size == data.length) {
            Object[] bigger = new Object[data.length * 2];
            System.arraycopy(data, 0, bigger, 0, size);
            data = bigger;
        }
    }

    private void removeAt(int index) {
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null; // не удерживаем ссылку
    }

    // ---------- базовые методы ----------

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return search(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            compare(e, e); // проверка типа/null, как в TreeSet
        }
        int pos = search(e);
        if (pos >= 0) {
            return false; // уже есть
        }
        int insertAt = -(pos + 1);
        ensureCapacity();
        System.arraycopy(data, insertAt, data, insertAt + 1, size - insertAt);
        data[insertAt] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int pos = search(o);
        if (pos < 0) {
            return false;
        }
        removeAt(pos);
        return true;
    }

    // ---------- toString: по возрастанию ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(data[i] == this ? "(this Collection)" : String.valueOf(data[i]));
        }
        return sb.append(']').toString();
    }

    // ---------- групповые операции ----------

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
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    /**
     * За один проход: сначала для каждого элемента вычисляется, оставлять ли его
     * (массив при этом не меняется, поэтому безопасно даже при c == this),
     * затем массив уплотняется.
     */
    private boolean batchRemove(Collection<?> c, boolean keepIfContained) {
        boolean[] keep = new boolean[size];
        int kept = 0;
        for (int i = 0; i < size; i++) {
            keep[i] = (c.contains(data[i]) == keepIfContained);
            if (keep[i]) {
                kept++;
            }
        }
        if (kept == size) {
            return false;
        }
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (keep[r]) {
                data[w++] = data[r];
            }
        }
        for (int i = w; i < size; i++) {
            data[i] = null;
        }
        size = w;
        return true;
    }

    // ---------- остальные методы интерфейса Set ----------

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastIndex = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) {
                    throw new java.util.NoSuchElementException();
                }
                lastIndex = cursor++;
                return (E) data[lastIndex];
            }

            @Override
            public void remove() {
                if (lastIndex < 0) {
                    throw new IllegalStateException();
                }
                removeAt(lastIndex);
                cursor = lastIndex;
                lastIndex = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            throw new UnsupportedOperationException("Передайте массив длиной не меньше size()");
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) data[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> other = (Set<?>) o;
        return other.size() == size && containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < size; i++) {
            h += (data[i] == null) ? 0 : data[i].hashCode();
        }
        return h;
    }

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MyTreeSet<Integer> set = new MyTreeSet<>();
        System.out.println(set + " empty=" + set.isEmpty());   // [] empty=true

        set.add(5);
        set.add(1);
        set.add(9);
        set.add(3);
        System.out.println(set.add(5));                        // false (дубликат)
        System.out.println(set);                               // [1, 3, 5, 9]

        System.out.println(set.contains(3));                   // true
        System.out.println(set.remove(3));                     // true
        System.out.println(set.remove(42));                    // false
        System.out.println(set);                               // [1, 5, 9]

        MyTreeSet<Integer> other = new MyTreeSet<>();
        other.add(9);
        other.add(7);
        other.add(1);

        System.out.println(set.containsAll(other));            // false
        System.out.println(set.addAll(other));                 // true
        System.out.println(set);                               // [1, 5, 7, 9]

        System.out.println(set.removeAll(other));              // true
        System.out.println(set);                               // [5]

        set.addAll(other);
        System.out.println(set.retainAll(other));              // true
        System.out.println(set);                               // [1, 7, 9]

        for (int i = 200; i > 0; i--) {
            set.add(i);                                        // проверка роста массива
        }
        System.out.println(set.size());                        // 200
        System.out.println(set.contains(137));                 // true

        set.clear();
        System.out.println(set + " empty=" + set.isEmpty());   // [] empty=true

        MyTreeSet<String> words = new MyTreeSet<>();
        words.add("pear");
        words.add("apple");
        words.add("fig");
        System.out.println(words);                             // [apple, fig, pear]
    }
}