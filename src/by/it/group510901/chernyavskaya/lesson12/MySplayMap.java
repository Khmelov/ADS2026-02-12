package by.it.group510901.chernyavskaya.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

//Splay-дерево(Самоподстраивающееся)

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        Node(Integer k, String v) { key = k; value = v; }
    }

    private Node root;
    private int size;

    private Node splay(Node h, Integer key) {
        if (h == null) return null;
        int cmp1 = key.compareTo(h.key);
        if (cmp1 < 0) {
            if (h.left == null) return h;
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) { h.left.left = splay(h.left.left, key); h = rotR(h); }
            else if (cmp2 > 0) { h.left.right = splay(h.left.right, key); if (h.left.right != null) h.left = rotL(h.left); }
            return h.left == null ? h : rotR(h);
        } else if (cmp1 > 0) {
            if (h.right == null) return h;
            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) { h.right.left = splay(h.right.left, key); if (h.right.left != null) h.right = rotR(h.right); }
            else if (cmp2 > 0) { h.right.right = splay(h.right.right, key); h = rotL(h); }
            return h.right == null ? h : rotL(h);
        }
        return h;
    }

    private Node rotR(Node h) { Node x = h.left; h.left = x.right; x.right = h; return x; }
    private Node rotL(Node h) { Node x = h.right; h.right = x.left; x.left = h; return x; }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean containsKey(Object k) { return k instanceof Integer && get(k) != null; }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        root = splay(root, (Integer) key);
        return (root != null && root.key.equals(key)) ? root.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        if (root == null) { root = new Node(key, value); size++; return null; }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) { String old = root.value; root.value = value; return old; }
        Node n = new Node(key, value);
        if (cmp < 0) { n.right = root; n.left = root.left; root.left = null; }
        else { n.left = root; n.right = root.right; root.right = null; }
        root = n; size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;
        String old = root.value;
        if (root.left == null) root = root.right;
        else { Node r = root.right; root = splay(root.left, lastKey()); root.right = r; }
        size--;
        return old;
    }

    @Override public void clear() { root = null; size = 0; }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root; while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root; while (x.right != null) x = x.right;
        return x.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        root = splay(root, key);
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp > 0) return root.key;
        Node x = root.left; if (x == null) return null;
        while (x.right != null) x = x.right;
        return x.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        root = splay(root, key);
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp >= 0) return root.key;
        Node x = root.left; if (x == null) return null;
        while (x.right != null) x = x.right;
        return x.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        root = splay(root, key);
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp <= 0) return root.key;
        Node x = root.right; if (x == null) return null;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        root = splay(root, key);
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp < 0) return root.key;
        Node x = root.right; if (x == null) return null;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        fillMap(root, toKey, sub, true);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        fillMap(root, fromKey, sub, false);
        return sub;
    }

    private void fillMap(Node x, Integer bound, MySplayMap sub, boolean isHead) {
        if (x == null) return;
        fillMap(x.left, bound, sub, isHead);
        if (isHead ? x.key.compareTo(bound) < 0 : x.key.compareTo(bound) >= 0) sub.put(x.key, x.value);
        fillMap(x.right, bound, sub, isHead);
    }

    @Override
    public boolean containsValue(Object value) {
        return findVal(root, value);
    }

    private boolean findVal(Node x, Object v) {
        if (x == null) return false;
        if (v == null ? x.value == null : v.equals(x.value)) return true;
        return findVal(x.left, v) || findVal(x.right, v);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        return sb.append("}").toString();
    }

    private void toString(Node n, StringBuilder sb) {
        if (n == null) return;
        toString(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        toString(n.right, sb);
    }

    // Заглушки для остальных методов NavigableMap
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer f, Integer t) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lowerEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer f, boolean fi, Integer t, boolean ti) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer k, boolean i) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer k, boolean i) { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}