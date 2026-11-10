package by.it.group510901.kachur.lesson01.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    //  Узел дерева
    private static final class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    //  Splay

    /** Поднимает узел x в корень. */
    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // zig
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if ((x == p.left) == (p == g.left)) {
                // zig-zig
                if (x == p.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else {
                    rotateLeft(g);
                    rotateLeft(p);
                }
            } else {
                // zig-zag
                if (x == p.left) {
                    rotateRight(p);
                    rotateLeft(g);
                } else {
                    rotateLeft(p);
                    rotateRight(g);
                }
            }
        }
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    // Поиск

    /** Возвращает узел с ключом key или null; найденный узел становится корнем. */
    private Node find(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root, last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (last != null) splay(last); // приближаем к корню последний посещённый
        return null;
    }

    //  Обязательные методы

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root, parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        Node node = new Node(key, value);
        node.parent = parent;
        if (cmp < 0) parent.left = node;
        else parent.right = node;
        size++;
        splay(node);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // обход всего дерева
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueHelper(n.left, value) || containsValueHelper(n.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        if (n == null) return null;
        String old = n.value;
        removeRoot();
        return old;
    }

    /** Удаляет корень (после find/splay удаляемый узел — корень). */
    private void removeRoot() {
        Node r = root;
        if (r == null) return;
        size--;
        if (r.left == null && r.right == null) {
            root = null;
            return;
        }
        if (r.left == null) {
            root = r.right;
            root.parent = null;
            return;
        }
        if (r.right == null) {
            root = r.left;
            root.parent = null;
            return;
        }
        // отсоединяем левое поддерево, ищем в нём максимум, splay, присоединяем правое
        Node leftTree = r.left;
        leftTree.parent = null;
        Node rightTree = r.right;
        rightTree.parent = null;

        // splay максимума левого поддерева
        Node maxLeft = leftTree;
        while (maxLeft.right != null) maxLeft = maxLeft.right;
        root = leftTree;
        splay(maxLeft); // maxLeft становится корнем, у него нет правого ребёнка
        root.right = rightTree;
        rightTree.parent = root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ======================= Навигация по ключам =======================

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maxNode(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = minNode(root);
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = maxNode(root);
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = find(key);
        if (n != null) {
            if (n.left != null) {
                Node m = maxNode(n.left);
                splay(m);
                return m.key;
            }
        }
        // если ключа нет — find сделал splay последнего посещённого.
        // Проще: найти наибольший ключ < key полным поиском.
        return predKey(key);
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = find(key);
        if (n != null) return n.key;
        return predKey(key);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = find(key);
        if (n != null) return n.key;
        return succKey(key);
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = find(key);
        if (n != null) {
            if (n.right != null) {
                Node m = minNode(n.right);
                splay(m);
                return m.key;
            }
        }
        return succKey(key);
    }

    /** Наибольший ключ, строго меньший key, или null. */
    private Integer predKey(Integer key) {
        Node cur = root;
        Node best = null;
        while (cur != null) {
            if (key.compareTo(cur.key) > 0) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        if (best != null) { splay(best); return best.key; }
        return null;
    }

    /** Наименьший ключ, строго больший key, или null. */
    private Integer succKey(Integer key) {
        Node cur = root;
        Node best = null;
        while (cur != null) {
            if (key.compareTo(cur.key) < 0) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (best != null) { splay(best); return best.key; }
        return null;
    }

    // ======================= toString =======================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildToString(root, sb, new boolean[]{true});
        sb.append('}');
        return sb.toString();
    }

    // in-order обход через рекурсию
    private void buildToString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        buildToString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        first[0] = false;
        buildToString(n.right, sb, first);
    }

    // ======================= headMap / tailMap =======================

    /** Подмаппинг с ключами < toKey. */
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return new SubMap(null, false, toKey, inclusive);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return new SubMap(fromKey, inclusive, null, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return new SubMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    // ======================= Внутренний подмаппинг =======================

    private final class SubMap implements NavigableMap<Integer, String> {
        final Integer lo; final boolean loInc;
        final Integer hi; final boolean hiInc;

        SubMap(Integer lo, boolean loInc, Integer hi, boolean hiInc) {
            this.lo = lo; this.loInc = loInc;
            this.hi = hi; this.hiInc = hiInc;
        }

        private boolean inRange(Integer k) {
            if (k == null) return false;
            if (lo != null) {
                int c = k.compareTo(lo);
                if (c < 0 || (c == 0 && !loInc)) return false;
            }
            if (hi != null) {
                int c = k.compareTo(hi);
                if (c > 0 || (c == 0 && !hiInc)) return false;
            }
            return true;
        }

        @Override public int size() {
            int c = 0;
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) { it.next(); c++; }
            return c;
        }
        @Override public boolean isEmpty() { return !keyIterator().hasNext(); }
        @Override public void clear() {
            Iterator<Integer> it = keyIterator();
            // собираем ключи, потом удаляем (иначе сломаем итератор)
            MySplayMap.this.size = MySplayMap.this.size; // no-op
            java.util.ArrayList<Integer> keys = new java.util.ArrayList<>();
            while (it.hasNext()) keys.add(it.next());
            for (Integer k : keys) MySplayMap.this.remove(k);
        }

        @Override public boolean containsKey(Object key) {
            if (!(key instanceof Integer)) return false;
            Integer k = (Integer) key;
            return inRange(k) && MySplayMap.this.containsKey(k);
        }
        @Override public boolean containsValue(Object value) {
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) {
                Integer k = it.next();
                String v = MySplayMap.this.get(k);
                if (value == null ? v == null : value.equals(v)) return true;
            }
            return false;
        }
        @Override public String get(Object key) {
            if (!(key instanceof Integer)) return null;
            Integer k = (Integer) key;
            return inRange(k) ? MySplayMap.this.get(k) : null;
        }
        @Override public String put(Integer key, String value) {
            if (!inRange(key)) throw new IllegalArgumentException("key out of range");
            return MySplayMap.this.put(key, value);
        }
        @Override public String remove(Object key) {
            if (!(key instanceof Integer)) return null;
            Integer k = (Integer) key;
            return inRange(k) ? MySplayMap.this.remove(k) : null;
        }

        // keyIterator выдаёт ключи подмаппинга в порядке возрастания
        private Iterator<Integer> keyIterator() {
            // сначала собираем все ключи дерева in-order, затем фильтруем
            java.util.ArrayList<Integer> all = new java.util.ArrayList<>();
            collect(root, all);
            java.util.ArrayList<Integer> filtered = new java.util.ArrayList<>();
            for (Integer k : all) if (inRange(k)) filtered.add(k);
            return filtered.iterator();
        }

        private void collect(Node n, java.util.ArrayList<Integer> out) {
            if (n == null) return;
            collect(n.left, out);
            out.add(n.key);
            collect(n.right, out);
        }

        @Override public Integer firstKey() {
            Iterator<Integer> it = keyIterator();
            if (!it.hasNext()) throw new NoSuchElementException();
            return it.next();
        }
        @Override public Integer lastKey() {
            Iterator<Integer> it = keyIterator();
            Integer last = null;
            while (it.hasNext()) last = it.next();
            if (last == null) throw new NoSuchElementException();
            return last;
        }

        @Override public Integer lowerKey(Integer key) {
            Integer best = null;
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) {
                Integer k = it.next();
                if (k < key) best = k; else break;
            }
            return best;
        }
        @Override public Integer floorKey(Integer key) {
            Integer best = null;
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) {
                Integer k = it.next();
                if (k <= key) best = k; else break;
            }
            return best;
        }
        @Override public Integer ceilingKey(Integer key) {
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) { Integer k = it.next(); if (k >= key) return k; }
            return null;
        }
        @Override public Integer higherKey(Integer key) {
            Iterator<Integer> it = keyIterator();
            while (it.hasNext()) { Integer k = it.next(); if (k > key) return k; }
            return null;
        }

        @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inc) {
            checkInRange(toKey);
            return new SubMap(lo, loInc, toKey, inc);
        }
        @Override public SortedMap<Integer, String> headMap(Integer toKey) { return headMap(toKey, false); }
        @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inc) {
            checkInRange(fromKey);
            return new SubMap(fromKey, inc, hi, hiInc);
        }
        @Override public SortedMap<Integer, String> tailMap(Integer fromKey) { return tailMap(fromKey, true); }
        @Override public NavigableMap<Integer, String> subMap(Integer f, boolean fi, Integer t, boolean ti) {
            checkInRange(f); checkInRange(t);
            return new SubMap(f, fi, t, ti);
        }
        @Override public SortedMap<Integer, String> subMap(Integer f, Integer t) { return subMap(f, true, t, false); }

        private void checkInRange(Integer k) {
            if (lo != null && k.compareTo(lo) < 0) throw new IllegalArgumentException();
            if (hi != null && k.compareTo(hi) > 0) throw new IllegalArgumentException();
        }

        @Override public Comparator<? super Integer> comparator() { return null; }
        @Override public NavigableMap<Integer, String> descendingMap() {
            // простой вариант — обёртка через вложенный класс не требуется по заданию,
            // но интерфейс требует. Реализуем через анонимный SubMap с обратным обходом.
            return new DescendingMap(this);
        }

        @Override public NavigableSet<Integer> navigableKeySet() { return null; }
        @Override public NavigableSet<Integer> descendingKeySet() { return null; }

        @Override public Map.Entry<Integer, String> firstEntry() { return null; }
        @Override public Map.Entry<Integer, String> lastEntry() { return null; }
        @Override public Map.Entry<Integer, String> pollFirstEntry() { return null; }
        @Override public Map.Entry<Integer, String> pollLastEntry() { return null; }
        @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
        @Override public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
        @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
        @Override public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }

        @Override public Set<Integer> keySet() { return null; }
        @Override public Collection<String> values() { return null; }
        @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) {
            for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("{");
            Iterator<Integer> it = keyIterator();
            boolean first = true;
            while (it.hasNext()) {
                Integer k = it.next();
                if (!first) sb.append(", ");
                sb.append(k).append('=').append(MySplayMap.this.get(k));
                first = false;
            }
            sb.append('}');
            return sb.toString();
        }
    }

    /** Простая обёртка для descendingMap. */
    private final class DescendingMap implements NavigableMap<Integer, String> {
        final SubMap base;
        DescendingMap(SubMap base) { this.base = base; }
        @Override public Comparator<? super Integer> comparator() { return java.util.Collections.reverseOrder(); }
        @Override public NavigableMap<Integer, String> descendingMap() { return base; }
        @Override public NavigableSet<Integer> navigableKeySet() { return null; }
        @Override public NavigableSet<Integer> descendingKeySet() { return null; }
        @Override public int size() { return base.size(); }
        @Override public boolean isEmpty() { return base.isEmpty(); }
        @Override public boolean containsKey(Object k) { return base.containsKey(k); }
        @Override public boolean containsValue(Object v) { return base.containsValue(v); }
        @Override public String get(Object k) { return base.get(k); }
        @Override public String put(Integer k, String v) { return base.put(k, v); }
        @Override public String remove(Object k) { return base.remove(k); }
        @Override public void putAll(Map<? extends Integer, ? extends String> m) { base.putAll(m); }
        @Override public void clear() { base.clear(); }
        @Override public Set<Integer> keySet() { return null; }
        @Override public Collection<String> values() { return null; }
        @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
        @Override public Map.Entry<Integer, String> lowerEntry(Integer k) { return null; }
        @Override public Map.Entry<Integer, String> floorEntry(Integer k) { return null; }
        @Override public Map.Entry<Integer, String> ceilingEntry(Integer k) { return null; }
        @Override public Map.Entry<Integer, String> higherEntry(Integer k) { return null; }
        @Override public Map.Entry<Integer, String> firstEntry() { return null; }
        @Override public Map.Entry<Integer, String> lastEntry() { return null; }
        @Override public Map.Entry<Integer, String> pollFirstEntry() { return null; }
        @Override public Map.Entry<Integer, String> pollLastEntry() { return null; }
        @Override public Integer lowerKey(Integer k) { return base.higherKey(k); }
        @Override public Integer floorKey(Integer k) { return base.ceilingKey(k); }
        @Override public Integer ceilingKey(Integer k) { return base.floorKey(k); }
        @Override public Integer higherKey(Integer k) { return base.lowerKey(k); }
        @Override public Integer firstKey() { return base.lastKey(); }
        @Override public Integer lastKey() { return base.firstKey(); }
        @Override public NavigableMap<Integer, String> subMap(Integer f, boolean fi, Integer t, boolean ti) {
            return new DescendingMap(new SubMap(t, ti, f, fi));
        }
        @Override public SortedMap<Integer, String> subMap(Integer f, Integer t) { return subMap(f, true, t, false); }
        @Override public NavigableMap<Integer, String> headMap(Integer t, boolean inc) { return new DescendingMap(new SubMap(null, false, t, inc)); }
        @Override public SortedMap<Integer, String> headMap(Integer t) { return headMap(t, false); }
        @Override public NavigableMap<Integer, String> tailMap(Integer f, boolean inc) { return new DescendingMap(new SubMap(f, inc, null, false)); }
        @Override public SortedMap<Integer, String> tailMap(Integer f) { return tailMap(f, true); }
        @Override public String toString() { return base.toString(); }
    }

    // ======================= Заглушки для остальных методов интерфейса =======================

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return new DescendingMap(new SubMap(null, false, null, false)); }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> firstEntry() { return null; }
    @Override public Map.Entry<Integer, String> lastEntry() { return null; }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Map.Entry<Integer, String> pollLastEntry() { return null; }
}