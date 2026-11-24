package by.it.group510902.shust.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        int key;
        String value;
        int height = 1;
        Node left, right;
        Node(int key, String value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) { return n == null ? 0 : n.height; }
    private int bFactor(Node n) { return n == null ? 0 : height(n.right) - height(n.left); }
    private void fixHeight(Node n) {
        int hl = height(n.left), hr = height(n.right);
        n.height = (hl > hr ? hl : hr) + 1;
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }

    private Node balance(Node p) {
        fixHeight(p);
        if (bFactor(p) == 2) {
            if (bFactor(p.right) < 0) p.right = rotateRight(p.right);
            return rotateLeft(p);
        }
        if (bFactor(p) == -2) {
            if (bFactor(p.left) > 0) p.left = rotateLeft(p.left);
            return rotateRight(p);
        }
        return p;
    }

    private Node insert(Node p, int key, String value) {
        if (p == null) return new Node(key, value);
        if (key < p.key) p.left = insert(p.left, key, value);
        else if (key > p.key) p.right = insert(p.right, key, value);
        else p.value = value;
        return balance(p);
    }

    private Node findMin(Node p) {
        return p.left != null ? findMin(p.left) : p;
    }

    private Node removeMin(Node p) {
        if (p.left == null) return p.right;
        p.left = removeMin(p.left);
        return balance(p);
    }

    private Node remove(Node p, int key) {
        if (p == null) return null;
        if (key < p.key) p.left = remove(p.left, key);
        else if (key > p.key) p.right = remove(p.right, key);
        else {
            if (p.left == null) return p.right;
            if (p.right == null) return p.left;
            Node min = findMin(p.right);
            min.right = removeMin(p.right);
            min.left = p.left;
            return balance(min);
        }
        return balance(p);
    }

    private Node search(Node p, int key) {
        if (p == null) return null;
        if (key < p.key) return search(p.left, key);
        else if (key > p.key) return search(p.right, key);
        else return p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n != null) {
            inOrder(n.left, sb);
            sb.append(n.key).append("=").append(n.value).append(", ");
            inOrder(n.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        Node res = search(root, key);
        if (res != null) {
            String old = res.value;
            res.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        int k = (Integer) key;
        Node res = search(root, k);
        if (res == null) return null;
        root = remove(root, k);
        size--;
        return res.value;
    }

    @Override
    public String get(Object key) {
        Node res = search(root, (Integer) key);
        return res == null ? null : res.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return search(root, (Integer) key) != null;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    // --- Обязательные заглушки интерфейса Map ---
    @Override public boolean containsValue(Object value) { return false; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}