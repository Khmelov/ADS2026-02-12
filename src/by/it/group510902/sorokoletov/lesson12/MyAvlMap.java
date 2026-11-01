package by.it.group510902.sorokoletov.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return true;
        }
        return false;
    }

    @Override
    public String get(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = put(root, key, value);
        return oldValue;
    }

    private String oldValue;

    private Node put(Node node, Integer key, String value) {
        if (node == null) { size++; return new Node(key, value); }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else { oldValue = node.value; node.value = value; return node; }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        removedValue = null;
        removedFlag = false;
        root = remove(root, (Integer) key);
        if (removedFlag) size--;
        return removedValue;
    }

    private String removedValue;
    private boolean removedFlag;

    private Node remove(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key);
        else if (cmp > 0) node.right = remove(node.right, key);
        else {
            removedFlag = true;
            removedValue = node.value;
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node min = node.right;
                while (min.left != null) min = min.left;
                node.key = min.key;
                node.value = min.value;
                node.right = removeMin(node.right);
            }
        }
        if (node == null) return null;
        return balance(node);
    }

    private Node removeMin(Node node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left);
        return balance(node);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append('=').append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    private int height(Node n) { return n == null ? 0 : n.height; }

    private void updateHeight(Node n) {
        int l = height(n.left), r = height(n.right);
        n.height = 1 + (l > r ? l : r);
    }

    private int balanceFactor(Node n) { return height(n.left) - height(n.right); }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}