package by.it.group510902.konchatov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right;
        int height = 1;
        Node(int key, String value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size;

    private int h(Node n) { return n == null ? 0 : n.height; }
    private void upd(Node n) { n.height = 1 + Math.max(h(n.left), h(n.right)); }

    private Node rotR(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        upd(y); upd(x);
        return x;
    }

    private Node rotL(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        upd(x); upd(y);
        return y;
    }

    private Node balance(Node n) {
        upd(n);
        int bf = h(n.left) - h(n.right);
        if (bf > 1) {
            if (h(n.left.left) < h(n.left.right)) n.left = rotL(n.left);
            return rotR(n);
        }
        if (bf < -1) {
            if (h(n.right.right) < h(n.right.left)) n.right = rotR(n.right);
            return rotL(n);
        }
        return n;
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = new String[1];
        root = insert(root, key, value, old);
        if (old[0] == null) size++;
        return old[0];
    }

    private Node insert(Node n, int key, String value, String[] old) {
        if (n == null) return new Node(key, value);
        if (key < n.key) n.left = insert(n.left, key, value, old);
        else if (key > n.key) n.right = insert(n.right, key, value, old);
        else { old[0] = n.value; n.value = value; return n; }
        return balance(n);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String[] removed = new String[1];
        root = delete(root, (Integer) key, removed);
        if (removed[0] != null) size--;
        return removed[0];
    }

    private Node delete(Node n, int key, String[] removed) {
        if (n == null) return null;
        if (key < n.key) n.left = delete(n.left, key, removed);
        else if (key > n.key) n.right = delete(n.right, key, removed);
        else {
            removed[0] = n.value;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node min = n.right;
            while (min.left != null) min = min.left;
            n.key = min.key;
            n.value = min.value;
            String[] mus = new String[1];
            n.right = delete(n.right, min.key, mus);
        }
        return balance(n);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override public int size() { return size; }
    @Override public void clear() { root = null; size = 0; }
    @Override public boolean isEmpty() { return size == 0; }

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

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}