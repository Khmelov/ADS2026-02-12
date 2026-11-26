package by.it.group510901.chernyavskaya.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

//АВЛ-дерево

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;
        Node(Integer k, String v) { key = k; value = v; height = 1; }
    }

    private Node root;
    private int size;
    private String oldValue;

    private int h(Node n) { return n == null ? 0 : n.height; }
    private int bf(Node n) { return n == null ? 0 : h(n.left) - h(n.right); } //баланс-фактор

    private Node fix(Node n) {
        n.height = 1 + Math.max(h(n.left), h(n.right));
        int b = bf(n);
        if (b > 1) {
            if (bf(n.left) < 0) n.left = rotL(n.left);
            return rotR(n);
        }
        if (b < -1) {
            if (bf(n.right) > 0) n.right = rotR(n.right);
            return rotL(n);
        }
        return n;
    }

    private Node rotR(Node y) {
        Node x = y.left; y.left = x.right; x.right = y;
        y.height = 1 + Math.max(h(y.left), h(y.right));
        x.height = 1 + Math.max(h(x.left), h(x.right));
        return x;
    }

    private Node rotL(Node x) {
        Node y = x.right; x.right = y.left; y.left = x;
        x.height = 1 + Math.max(h(x.left), h(x.right));
        y.height = 1 + Math.max(h(y.left), h(y.right));
        return y;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node n = root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        oldValue = null;
        root = put(root, key, value);
        return oldValue;
    }

    private Node put(Node n, Integer k, String v) {
        if (n == null) { size++; return new Node(k, v); }
        int cmp = k.compareTo(n.key);
        if (cmp < 0) n.left = put(n.left, k, v);
        else if (cmp > 0) n.right = put(n.right, k, v);
        else { oldValue = n.value; n.value = v; }
        return fix(n);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String val = get(key);
        if (val != null) {
            root = delete(root, (Integer) key);
            size--;
        }
        return val;
    }

    private Node delete(Node n, Integer k) {
        if (n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) n.left = delete(n.left, k);
        else if (cmp > 0) n.right = delete(n.right, k);
        else {
            if (n.left == null || n.right == null) {
                return n.left != null ? n.left : n.right;
            }
            Node min = n.right;
            while (min.left != null) min = min.left;
            n.key = min.key;
            n.value = min.value;
            n.right = delete(n.right, min.key);
        }
        return fix(n);
    }

    @Override public void clear() { root = null; size = 0; }

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

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}