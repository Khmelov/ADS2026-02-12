package by.it.group551004.podvitelskiymichael.lesson12;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override public Integer getKey() { return key; }
        @Override public String getValue() { return value; }
        @Override public String setValue(String v) { String old = value; value = v; return old; }
    }

    private Node root;
    private int size;

    private void splay(int key) {
        if (root == null) return;
        Node header = new Node(null, null);
        Node l = header, r = header, t = root;
        while (true) {
            if (key < t.key) {
                if (t.left == null) break;
                if (key < t.left.key) {
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) break;
                }
                r.left = t;
                r = t;
                t = t.left;
            } else if (key > t.key) {
                if (t.right == null) break;
                if (key > t.right.key) {
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) break;
                }
                l.right = t;
                l = t;
                t = t.right;
            } else {
                break;
            }
        }
        l.right = t.left;
        r.left = t.right;
        t.left = header.right;
        t.right = header.left;
        root = t;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendTo(root, sb);
        return sb.append('}').toString();
    }

    private void appendTo(Node t, StringBuilder sb) {
        if (t == null) return;
        appendTo(t.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(t.key).append('=').append(t.value);
        appendTo(t.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        splay(key);
        if (root.key.intValue() == key) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(key, value);
        if (key < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object o) {
        if (!(o instanceof Integer) || root == null) return null;
        int key = (Integer) o;
        splay(key);
        if (root.key != key) return null;
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightPart = root.right;
            root = root.left;
            splay(key);
            root.right = rightPart;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object o) {
        if (!(o instanceof Integer) || root == null) return null;
        splay((Integer) o);
        return root.key.equals(o) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object o) {
        if (!(o instanceof Integer) || root == null) return false;
        splay((Integer) o);
        return root.key.equals(o);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node t, Object value) {
        if (t == null) return false;
        if (value == null ? t.value == null : value.equals(t.value)) return true;
        return containsValue(t.left, value) || containsValue(t.right, value);
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

    private MySplayMap range(Integer lo, boolean loInc, Integer hi, boolean hiInc) {
        MySplayMap res = new MySplayMap();
        fillRange(root, lo, loInc, hi, hiInc, res);
        return res;
    }

    private static void fillRange(Node t, Integer lo, boolean loInc, Integer hi, boolean hiInc, MySplayMap res) {
        if (t == null) return;
        boolean goLeft = lo == null || t.key > lo;
        boolean goRight = hi == null || t.key < hi;
        if (goLeft) fillRange(t.left, lo, loInc, hi, hiInc, res);
        boolean geLo = lo == null || t.key > lo || (loInc && t.key.intValue() == lo);
        boolean leHi = hi == null || t.key < hi || (hiInc && t.key.intValue() == hi);
        if (geLo && leHi) res.put(t.key, t.value);
        if (goRight) fillRange(t.right, lo, loInc, hi, hiInc, res);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return range(null, false, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(fromKey, true, null, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(null, false, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(fromKey, inclusive, null, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return range(fromKey, true, toKey, false);
    }

    private Node firstNode() {
        Node t = root;
        if (t == null) return null;
        while (t.left != null) t = t.left;
        return t;
    }

    private Node lastNode() {
        Node t = root;
        if (t == null) return null;
        while (t.right != null) t = t.right;
        return t;
    }

    @Override
    public Integer firstKey() {
        Node n = firstNode();
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = lastNode();
        if (n == null) throw new NoSuchElementException();
        return n.key;
    }

    private Node lowerNode(int key) {
        Node t = root, res = null;
        while (t != null) {
            if (t.key < key) { res = t; t = t.right; } else t = t.left;
        }
        return res;
    }

    private Node floorNode(int key) {
        Node t = root, res = null;
        while (t != null) {
            if (t.key <= key) { res = t; t = t.right; } else t = t.left;
        }
        return res;
    }

    private Node ceilingNode(int key) {
        Node t = root, res = null;
        while (t != null) {
            if (t.key >= key) { res = t; t = t.left; } else t = t.right;
        }
        return res;
    }

    private Node higherNode(int key) {
        Node t = root, res = null;
        while (t != null) {
            if (t.key > key) { res = t; t = t.left; } else t = t.right;
        }
        return res;
    }

    private static Integer keyOf(Node n) {
        return n == null ? null : n.key;
    }

    @Override public Integer lowerKey(Integer key)   { return keyOf(lowerNode(key)); }
    @Override public Integer floorKey(Integer key)   { return keyOf(floorNode(key)); }
    @Override public Integer ceilingKey(Integer key) { return keyOf(ceilingNode(key)); }
    @Override public Integer higherKey(Integer key)  { return keyOf(higherNode(key)); }

    @Override public Map.Entry<Integer, String> lowerEntry(Integer key)   { return lowerNode(key); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key)   { return floorNode(key); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { return ceilingNode(key); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key)  { return higherNode(key); }
    @Override public Map.Entry<Integer, String> firstEntry() { return firstNode(); }
    @Override public Map.Entry<Integer, String> lastEntry()  { return lastNode(); }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Node n = firstNode();
        if (n == null) return null;
        Node copy = new Node(n.key, n.value);
        remove(n.key);
        return copy;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Node n = lastNode();
        if (n == null) return null;
        Node copy = new Node(n.key, n.value);
        remove(n.key);
        return copy;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override public Comparator<? super Integer> comparator() { return null; }

    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet()       { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet()      { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet()                         { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values()                   { throw new UnsupportedOperationException(); }
    @Override public Set<Map.Entry<Integer, String>> entrySet()    { throw new UnsupportedOperationException(); }
}