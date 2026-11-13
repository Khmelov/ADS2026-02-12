package by.it.group510902.konchatov.lesson12;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right;
        Node(int key, String value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        fill(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        return sb.append('}').toString();
    }
        private void fill(Node n, StringBuilder sb) {
        if (n == null) return;
        fill(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        fill(n.right, sb);
    }



    private Node min(Node n) { while (n.left != null) n = n.left; return n; }
    private Node max(Node n) { while (n.right != null) n = n.right; return n; }
   

    
    @Override public int size() { return size; }
    @Override public void clear() { root = null; size = 0; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return max(root).key;
    }


    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap m = new MySplayMap();
        head(root, toKey, m);
        return m;
    }

    private void head(Node n, int k, MySplayMap m) {
        if (n == null) return;
        if (n.key < k) { head(n.left, k, m); m.put(n.key, n.value); head(n.right, k, m); }
        else head(n.left, k, m);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        tail(root, fromKey, m);
        return m;
    }

    private void tail(Node n, int k, MySplayMap m) {
        if (n == null) return;
        if (n.key >= k) { tail(n.left, k, m); m.put(n.key, n.value); tail(n.right, k, m); }
        else tail(n.right, k, m);
    }


    @Override
    public String put(Integer key, String value) {
        if (root == null) { root = new Node(key, value); size++; return null; }
        splay(key);
        if (key == root.key) { String old = root.value; root.value = value; return old; }
        Node n = new Node(key, value);
        if (key < root.key) {
            n.left = root.left; n.right = root;
            root.left = null;
        } else {
            n.right = root.right; n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        splay((Integer) key);
        if (root != null && root.key == (Integer) key) return root.value;
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object v) {
        if (n == null) return false;
        if (v == null ? n.value == null : v.equals(n.value)) return true;
        return containsValue(n.left, v) || containsValue(n.right, v);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        splay(k);
        if (root == null || root.key != k) return null;
        String removed = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            Node left = root.left;
            root = left;
            splay(k); 
            root.right = right;
        }
        size--;
        return removed;
    }



    @Override
    public Integer lowerKey(Integer key) {
        Integer best = null;
        Node n = root;
        while (n != null) {
            if (n.key < key) { best = n.key; n = n.right; }
            else n = n.left;
        }
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        Integer best = null;
        Node n = root;
        while (n != null) {
            if (n.key <= key) { best = n.key; n = n.right; }
            else n = n.left;
        }
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Integer best = null;
        Node n = root;
        while (n != null) {
            if (n.key >= key) { best = n.key; n = n.left; }
            else n = n.right;
        }
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        Integer best = null;
        Node n = root;
        while (n != null) {
            if (n.key > key) { best = n.key; n = n.left; }
            else n = n.right;
        }
        return best;
    }



    

     private Node succ(Node n) {
        if (n.right != null) return min(n.right);
        Node p = null, c = root;
        while (c != null) {
            if (n.key < c.key) { p = c; c = c.left; }
            else if (n.key > c.key) c = c.right;
            else break;
        }
        return p;
    }

     private void splay(int key) {
        if (root == null) return;
        Node dummy = new Node(0, null);
        Node l = dummy, r = dummy;
        Node t = root;
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
        t.left = dummy.right;
        t.right = dummy.left;
        root = t;
    }



    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return new AbstractSet<Entry<Integer, String>>() {
            @Override public Iterator<Entry<Integer, String>> iterator() {
                return new Iterator<Entry<Integer, String>>() {
                    Node next = root == null ? null : min(root);
                    @Override public boolean hasNext() { return next != null; }
                    @Override public Entry<Integer, String> next() {
                        Node c = next; next = succ(c);
                        return new AbstractMap.SimpleEntry<>(c.key, c.value);
                    }
                };
            }
            @Override public int size() { return size; }
        };
    }


    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer a, Integer b) { throw new UnsupportedOperationException(); }
@Override public NavigableMap<Integer, String> subMap(Integer a, boolean b, Integer c, boolean d) { throw new UnsupportedOperationException(); }
@Override public NavigableMap<Integer, String> headMap(Integer k, boolean b) { throw new UnsupportedOperationException(); }
@Override public NavigableMap<Integer, String> tailMap(Integer k, boolean b) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lowerEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}