package by.it.group510902.konchatov.lesson12;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color = RED;
        Node(int key, String value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size;

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

    private boolean isRed(Node n) { return n != null && n.color == RED; }

    private Node min(Node n) { while (n.left != null) n = n.left; return n; }
    private Node max(Node n) { while (n.right != null) n = n.right; return n; }

    private Node find(Node n) {
        if (n.right != null) return min(n.right);
        Node p = n.parent;
        while (p != null && n == p.right) { n = p; p = p.parent; }
        return p;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
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
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
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

    @Override public int size() { return size; }
    @Override public void clear() { root = null; size = 0; }
    @Override public boolean isEmpty() { return size == 0; }
    
    private Node find(int key) {
        Node n = root;
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }


    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }
   

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap m = new MyRbMap();
        head(root, toKey, m);
        return m;
    }

    private void head(Node n, int k, MyRbMap m) {
        if (n == null) return;
        if (n.key < k) { head(n.left, k, m); m.put(n.key, n.value); head(n.right, k, m); }
        else head(n.left, k, m);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap m = new MyRbMap();
        tail(root, fromKey, m);
        return m;
    }

    private void tail(Node n, int k, MyRbMap m) {
        if (n == null) return;
        if (n.key >= k) { tail(n.left, k, m); m.put(n.key, n.value); tail(n.right, k, m); }
        else tail(n.right, k, m);
    }

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
    @Override
    public String put(Integer key, String value) {
        Node y = null, x = root;
        while (x != null) {
            y = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else { String old = x.value; x.value = value; return old; }
        }
        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) root = z;
        else if (key < y.key) y.left = z;
        else y.right = z;
        size++;
        upd(z);
        return null;
    }

    
    private void upd(Node z) {
        while (isRed(z.parent)) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (isRed(y)) {
                    z.parent.color = BLACK; y.color = BLACK;
                    z.parent.parent.color = RED; z = z.parent.parent;
                } else {
                    if (z == z.parent.right) { z = z.parent; rotateLeft(z); }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (isRed(y)) {
                    z.parent.color = BLACK; y.color = BLACK;
                    z.parent.parent.color = RED; z = z.parent.parent;
                } else {
                    if (z == z.parent.left) { z = z.parent; rotateRight(z); }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }


    private void delete(Node x, Node parent) {
        while (x != root && !isRed(x)) {
            if (x == (parent == null ? null : parent.left)) {
                Node w = parent.right;
                if (isRed(w)) {
                    w.color = BLACK; parent.color = RED;
                    rotateLeft(parent); w = parent.right;
                }
                if (!isRed(w.left) && !isRed(w.right)) {
                    w.color = RED; x = parent; parent = x.parent;
                } else {
                    if (!isRed(w.right)) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED; rotateRight(w); w = parent.right;
                    }
                    w.color = parent.color; parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(parent); x = root; parent = null;
                }
            } else {
                Node w = parent.left;
                if (isRed(w)) {
                    w.color = BLACK; parent.color = RED;
                    rotateRight(parent); w = parent.left;
                }
                if (!isRed(w.right) && !isRed(w.left)) {
                    w.color = RED; x = parent; parent = x.parent;
                } else {
                    if (!isRed(w.left)) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED; rotateLeft(w); w = parent.left;
                    }
                    w.color = parent.color; parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(parent); x = root; parent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }



    

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = find((Integer) key);
        if (z == null) return null;
        String removed = z.value;

        Node y = z;
        boolean yColor = y.color;
        Node x, xParent;
        if (z.left == null) {
            x = z.right; xParent = z.parent; transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left; xParent = z.parent; transplant(z, z.left);
        } else {
            y = min(z.right); yColor = y.color; x = y.right;
            if (y.parent == z) { xParent = y; if (x != null) x.parent = y; }
            else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right; y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left; y.left.parent = y;
            y.color = z.color;
        }
        if (yColor == BLACK) delete(x, xParent);
        size--;
        return removed;
    }



    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return new AbstractSet<Entry<Integer, String>>() {
            @Override public Iterator<Entry<Integer, String>> iterator() {
                return new Iterator<Entry<Integer, String>>() {
                    Node next = root == null ? null : min(root);
                    @Override public boolean hasNext() { return next != null; }
                    @Override public Entry<Integer, String> next() {
                        Node c = next; next = find(c);
                        return new AbstractMap.SimpleEntry<>(c.key, c.value);
                    }
                };
            }
            @Override public int size() { return size; }
        };
    }



    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer a, Integer b) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
} 