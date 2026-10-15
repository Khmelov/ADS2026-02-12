package by.it.group551004.karpovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node implements Map.Entry<Integer, String> {
        int key;
        String val;
        Node l, r;
        Node(int key, String val) {
            this.key = key;
            this.val = val;
        }
        public Integer getKey() {
            return key;
        }
        public String getValue() {
            return val;
        }
        public String setValue(String v) {
            String o = val;
            val = v;
            return o;
        }
    }

    private Node root;
    private int size;

    private Node rotR(Node h) {
        Node x = h.l;
        h.l = x.r;
        x.r = h;
        return x;
    }

    private Node rotL(Node h) {
        Node x = h.r;
        h.r = x.l;
        x.l = h;
        return x;
    }

    private Node splay(Node h, int k) {
        if (h == null) return null;
        if (k < h.key) {
            if (h.l == null) return h;
            if (k < h.l.key) {
                h.l.l = splay(h.l.l, k);
                h = rotR(h);
            } else if (k > h.l.key) {
                h.l.r = splay(h.l.r, k);
                if (h.l.r != null) h.l = rotL(h.l);
            }
            if (h.l == null) return h;
            return rotR(h);
        } else if (k > h.key) {
            if (h.r == null) return h;
            if (k < h.r.key) {
                h.r.l = splay(h.r.l, k);
                if (h.r.l != null) h.r = rotR(h.r);
            } else if (k > h.r.key) {
                h.r.r = splay(h.r.r, k);
                h = rotL(h);
            }
            if (h.r == null) return h;
            return rotL(h);
        }
        return h;
    }

    private Node firstNode() {
        if (root == null) return null;
        Node n = root;
        while (n.l != null) n = n.l;
        return n;
    }

    private Node lastNode() {
        if (root == null) return null;
        Node n = root;
        while (n.r != null) n = n.r;
        return n;
    }

    private Node lowerNode(int k) {
        Node r = null, n = root;
        while (n != null) {
            if (k <= n.key) n = n.l;
            else {
                r = n;
                n = n.r;
            }
        }
        return r;
    }

    private Node floorNode(int k) {
        Node r = null, n = root;
        while (n != null) {
            if (k == n.key) return n;
            if (k < n.key) n = n.l;
            else {
                r = n;
                n = n.r;
            }
        }
        return r;
    }

    private Node ceilingNode(int k) {
        Node r = null, n = root;
        while (n != null) {
            if (k == n.key) return n;
            if (k < n.key) {
                r = n;
                n = n.l;
            } else n = n.r;
        }
        return r;
    }

    private Node higherNode(int k) {
        Node r = null, n = root;
        while (n != null) {
            if (k < n.key) {
                r = n;
                n = n.l;
            } else n = n.r;
        }
        return r;
    }

    private void build(Node n, StringBuilder sb) {
        if (n == null) return;
        build(n.l, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.val);
        build(n.r, sb);
    }

    private boolean hasValue(Node n, Object v) {
        if (n == null) return false;
        if (v == null ? n.val == null : v.equals(n.val)) return true;
        return hasValue(n.l, v) || hasValue(n.r, v);
    }

    private void fill(Node n, MySplayMap dst, Integer lo, boolean loInc, Integer hi, boolean hiInc) {
        if (n == null) return;
        fill(n.l, dst, lo, loInc, hi, hiInc);
        boolean okLo = lo == null || n.key > lo || (loInc && n.key == lo);
        boolean okHi = hi == null || n.key < hi || (hiInc && n.key == hi);
        if (okLo && okHi) dst.put(n.key, n.val);
        fill(n.r, dst, lo, loInc, hi, hiInc);
    }

    private MySplayMap range(Integer lo, boolean loInc, Integer hi, boolean hiInc) {
        MySplayMap m = new MySplayMap();
        fill(root, m, lo, loInc, hi, hiInc);
        return m;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        build(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;
        if (root == null) {
            root = new Node(k, value);
            size++;
            return null;
        }
        root = splay(root, k);
        if (k == root.key) {
            String o = root.val;
            root.val = value;
            return o;
        }
        Node n = new Node(k, value);
        if (k < root.key) {
            n.l = root.l;
            n.r = root;
            root.l = null;
        } else {
            n.r = root.r;
            n.l = root;
            root.r = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (root == null) return null;
        int k = (Integer) key;
        root = splay(root, k);
        if (k != root.key) return null;
        String o = root.val;
        if (root.l == null) root = root.r;
        else {
            Node x = root.r;
            root = splay(root.l, k);
            root.r = x;
        }
        size--;
        return o;
    }

    @Override
    public String get(Object key) {
        if (root == null) return null;
        int k = (Integer) key;
        root = splay(root, k);
        return k == root.key ? root.val : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (root == null) return false;
        int k = (Integer) key;
        root = splay(root, k);
        return k == root.key;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return range(null, false, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(fromKey, true, null, false);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return range(fromKey, true, toKey, false);
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public Integer firstKey() {
        Node n = firstNode();
        return n == null ? null : n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = lastNode();
        return n == null ? null : n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = lowerNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = floorNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = ceilingNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return lowerNode(key);
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return floorNode(key);
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return ceilingNode(key);
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return higherNode(key);
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        return firstNode();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        return lastNode();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Node n = firstNode();
        if (n != null) remove(n.key);
        return n;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Node n = lastNode();
        if (n != null) remove(n.key);
        return n;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
