package by.it.group551004.karpovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {
    private static class Node {
        int key;
        String val;
        Node l, r;
        boolean red;
        Node(int key, String val, boolean red) {
            this.key = key;
            this.val = val;
            this.red = red;
        }
    }

    private Node root;
    private int size;
    private String old;

    private boolean isRed(Node n) {
        return n != null && n.red;
    }

    private Node rotL(Node h) {
        Node x = h.r;
        h.r = x.l;
        x.l = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private Node rotR(Node h) {
        Node x = h.l;
        h.l = x.r;
        x.r = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private void flip(Node h) {
        h.red = !h.red;
        h.l.red = !h.l.red;
        h.r.red = !h.r.red;
    }

    private Node fix(Node h) {
        if (isRed(h.r) && !isRed(h.l)) h = rotL(h);
        if (isRed(h.l) && isRed(h.l.l)) h = rotR(h);
        if (isRed(h.l) && isRed(h.r)) flip(h);
        return h;
    }

    private Node moveRedLeft(Node h) {
        flip(h);
        if (isRed(h.r.l)) {
            h.r = rotR(h.r);
            h = rotL(h);
            flip(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flip(h);
        if (isRed(h.l.l)) {
            h = rotR(h);
            flip(h);
        }
        return h;
    }

    private Node insert(Node h, int k, String v) {
        if (h == null) {
            size++;
            return new Node(k, v, true);
        }
        if (k < h.key) h.l = insert(h.l, k, v);
        else if (k > h.key) h.r = insert(h.r, k, v);
        else {
            old = h.val;
            h.val = v;
        }
        if (isRed(h.r) && !isRed(h.l)) h = rotL(h);
        if (isRed(h.l) && isRed(h.l.l)) h = rotR(h);
        if (isRed(h.l) && isRed(h.r)) flip(h);
        return h;
    }

    private Node min(Node n) {
        while (n.l != null) n = n.l;
        return n;
    }

    private Node deleteMin(Node h) {
        if (h.l == null) return null;
        if (!isRed(h.l) && !isRed(h.l.l)) h = moveRedLeft(h);
        h.l = deleteMin(h.l);
        return fix(h);
    }

    private Node delete(Node h, int k) {
        if (k < h.key) {
            if (!isRed(h.l) && !isRed(h.l.l)) h = moveRedLeft(h);
            h.l = delete(h.l, k);
        } else {
            if (isRed(h.l)) h = rotR(h);
            if (k == h.key && h.r == null) return null;
            if (!isRed(h.r) && !isRed(h.r.l)) h = moveRedRight(h);
            if (k == h.key) {
                Node m = min(h.r);
                h.key = m.key;
                h.val = m.val;
                h.r = deleteMin(h.r);
            } else {
                h.r = delete(h.r, k);
            }
        }
        return fix(h);
    }

    private Node find(int k) {
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.l;
            else if (k > n.key) n = n.r;
            else return n;
        }
        return null;
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

    private void fill(Node n, MyRbMap dst, Integer lo, Integer hi) {
        if (n == null) return;
        fill(n.l, dst, lo, hi);
        if ((lo == null || n.key >= lo) && (hi == null || n.key < hi)) dst.put(n.key, n.val);
        fill(n.r, dst, lo, hi);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        build(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        old = null;
        root = insert(root, key, value);
        root.red = false;
        return old;
    }

    @Override
    public String remove(Object key) {
        int k = (Integer) key;
        Node n = find(k);
        if (n == null) return null;
        String res = n.val;
        if (!isRed(root.l) && !isRed(root.r)) root.red = true;
        root = delete(root, k);
        size--;
        if (root != null) root.red = false;
        return res;
    }

    @Override
    public String get(Object key) {
        Node n = find((Integer) key);
        return n == null ? null : n.val;
    }

    @Override
    public boolean containsKey(Object key) {
        return find((Integer) key) != null;
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
        MyRbMap m = new MyRbMap();
        fill(root, m, null, toKey);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap m = new MyRbMap();
        fill(root, m, fromKey, null);
        return m;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap m = new MyRbMap();
        fill(root, m, fromKey, toKey);
        return m;
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node n = root;
        while (n.r != null) n = n.r;
        return n.key;
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