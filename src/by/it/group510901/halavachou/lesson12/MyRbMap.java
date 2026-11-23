package by.it.group510901.halavachou.lesson12;
import java.util.*;


public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true, BLACK = false;
    private static class Node {
        Integer k; String v; Node l, r; boolean c;
        Node(Integer k, String v, boolean c) { this.k = k; this.v = v; this.c = c; }
    }
    private Node root;
    private int size;

    private boolean isRed(Node x) { return x != null && x.c == RED; }
    private Node rotL(Node h) {
        Node x = h.r;
        h.r = x.l;
        x.l = h;
        x.c = h.c;
        h.c = RED;
        return x; }

    private Node rotR(Node h) {
        Node x = h.l;
        h.l = x.r;
        x.r = h;
        x.c = h.c;
        h.c = RED;
        return x; }

    private void flip(Node h) {
        h.c = !h.c;
        if (h.l != null) h.l.c = !h.l.c;
        if (h.r != null) h.r.c = !h.r.c; }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean containsKey(Object k) { return k instanceof Integer && get(k) != null; }

    @Override
    public boolean containsValue(Object val) {
        return findVal(root, val);
    }
    private boolean findVal(Node x, Object v) {
        if (x == null) return false;
        return (Objects.equals(v, x.v)) || findVal(x.l, v) || findVal(x.r, v);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer k)) return null;
        Node x = root;
        while (x != null) {
            int cmp = k.compareTo(x.k);
            if (cmp < 0) x = x.l;
            else if (cmp > 0) x = x.r;
            else return x.v;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        String old = get(key);
        root = put(root, key, value);
        root.c = BLACK;
        if (old == null) size++;
        return old;
    }

    private Node put(Node h, Integer k, String v) {
        if (h == null) return new Node(k, v, RED);
        int cmp = k.compareTo(h.k);
        if (cmp < 0) h.l = put(h.l, k, v);
        else if (cmp > 0) h.r = put(h.r, k, v);
        else h.v = v;
        return balance(h);
    }

    private Node moveRedLeft(Node h) {
        flip(h);
        if (isRed(h.r.l)) { h.r = rotR(h.r); h = rotL(h); flip(h); }
        return h;
    }

    private Node moveRedRight(Node h) {
        flip(h);
        if (isRed(h.l.l)) { h = rotR(h); flip(h); }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.r)) h = rotL(h);
        if (isRed(h.l) && isRed(h.l.l)) h = rotR(h);
        if (isRed(h.l) && isRed(h.r)) flip(h);
        return h;
    }

    private Node min(Node x) { return x.l == null ? x : min(x.l); }

    private Node deleteMin(Node h) {
        if (h.l == null) return null;
        if (!isRed(h.l) && !isRed(h.l.l)) h = moveRedLeft(h);
        h.l = deleteMin(h.l);
        return balance(h);
    }

    private Node delete(Node h, Integer k) {
        if (k.compareTo(h.k) < 0) {
            if (!isRed(h.l) && !isRed(h.l.l)) h = moveRedLeft(h);
            h.l = delete(h.l, k);
        } else {
            if (isRed(h.l)) h = rotR(h);
            if (k.compareTo(h.k) == 0 && h.r == null) return null;
            if (!isRed(h.r) && !isRed(h.r.l)) h = moveRedRight(h);
            if (k.compareTo(h.k) == 0) {
                Node x = min(h.r);
                h.k = x.k; h.v = x.v;
                h.r = deleteMin(h.r);
            } else h.r = delete(h.r, k);
        }
        return balance(h);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || !containsKey(key)) return null;
        String val = get(key);
        if (!isRed(root.l) && !isRed(root.r)) root.c = RED;
        root = delete(root, (Integer) key);
        if (root != null) root.c = BLACK;
        size--;
        return val;
    }

    @Override public void clear() { root = null; size = 0; }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root; while (x.l != null) x = x.l;
        return x.k;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root; while (x.r != null) x = x.r;
        return x.k;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillMap(root, toKey, sub, true);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillMap(root, fromKey, sub, false);
        return sub;
    }

    private void fillMap(Node x, Integer bound, MyRbMap sub, boolean isHead) {
        if (x == null) return;
        fillMap(x.l, bound, sub, isHead);
        if (isHead == (x.k.compareTo(bound) < 0)) sub.put(x.k, x.v);
        fillMap(x.r, bound, sub, isHead);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        return sb.append("}").toString();
    }

    private void toString(Node n, StringBuilder sb) {
        if (n == null) return;
        toString(n.l, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.k).append("=").append(n.v);
        toString(n.r, sb);
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer f, Integer t) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}