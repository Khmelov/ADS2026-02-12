package by.it.group551004.karpovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        int key;
        String val;
        Node l, r;
        int h = 1;
        Node(int key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;
    private int size;
    private String old;
    private boolean found;

    private int h(Node n) {
        return n == null ? 0 : n.h;
    }

    private void upd(Node n) {
        n.h = Math.max(h(n.l), h(n.r)) + 1;
    }

    private Node rotR(Node y) {
        Node x = y.l;
        y.l = x.r;
        x.r = y;
        upd(y);
        upd(x);
        return x;
    }

    private Node rotL(Node x) {
        Node y = x.r;
        x.r = y.l;
        y.l = x;
        upd(x);
        upd(y);
        return y;
    }

    private Node balance(Node n) {
        upd(n);
        int b = h(n.l) - h(n.r);
        if (b > 1) {
            if (h(n.l.l) < h(n.l.r)) n.l = rotL(n.l);
            return rotR(n);
        }
        if (b < -1) {
            if (h(n.r.r) < h(n.r.l)) n.r = rotR(n.r);
            return rotL(n);
        }
        return n;
    }

    private Node insert(Node n, int k, String v) {
        if (n == null) {
            size++;
            return new Node(k, v);
        }
        if (k < n.key) n.l = insert(n.l, k, v);
        else if (k > n.key) n.r = insert(n.r, k, v);
        else {
            old = n.val;
            n.val = v;
            return n;
        }
        return balance(n);
    }

    private Node min(Node n) {
        while (n.l != null) n = n.l;
        return n;
    }

    private Node deleteMin(Node n) {
        if (n.l == null) return n.r;
        n.l = deleteMin(n.l);
        return balance(n);
    }

    private Node delete(Node n, int k) {
        if (n == null) return null;
        if (k < n.key) n.l = delete(n.l, k);
        else if (k > n.key) n.r = delete(n.r, k);
        else {
            found = true;
            old = n.val;
            if (n.l == null) return n.r;
            if (n.r == null) return n.l;
            Node m = min(n.r);
            n.key = m.key;
            n.val = m.val;
            n.r = deleteMin(n.r);
        }
        return balance(n);
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
        return old;
    }

    @Override
    public String remove(Object key) {
        old = null;
        found = false;
        root = delete(root, (Integer) key);
        if (found) size--;
        return old;
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
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
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