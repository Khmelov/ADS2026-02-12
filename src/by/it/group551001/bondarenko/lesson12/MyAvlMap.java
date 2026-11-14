package by.it.group551001.bondarenko.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyAvlMap implements Map<Integer, String> {

    // Узел АВЛ-дерева
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        // Левый перекос
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        // Правый перекос
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node getNode(Node n, Integer key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) return getNode(n.left, key);
        else if (cmp > 0) return getNode(n.right, key);
        else return n;
    }

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
        if (!(key instanceof Integer)) return false;
        return containsKey((Integer) key);
    }

    public boolean containsKey(Integer key) {
        if (key == null) return false;
        return getNode(root, key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get((Integer) key);
    }

    public String get(Integer key) {
        if (key == null) return null;
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        String oldValue = get(key);
        root = putInternal(root, key, value);
        return oldValue;
    }

    private Node putInternal(Node n, Integer key, String value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = putInternal(n.left, key, value);
        } else if (cmp > 0) {
            n.right = putInternal(n.right, key, value);
        } else {
            n.value = value;
            return n;
        }
        return balance(n);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null) return null;
        Node node = getNode(root, key);
        if (node == null) return null;
        String val = node.value;
        root = removeInternal(root, key);
        size--;
        return val;
    }

    private Node removeInternal(Node n, Integer key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = removeInternal(n.left, key);
        } else if (cmp > 0) {
            n.right = removeInternal(n.right, key);
        } else {
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;

            Node successor = getMin(n.right);
            n.key = successor.key;
            n.value = successor.value;
            n.right = removeMin(n.right);
        }
        return balance(n);
    }

    private Node getMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append("=").append(n.value);
        appendInOrder(n.right, sb);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInternal(root, value);
    }

    private boolean containsValueInternal(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueInternal(n.left, value) || containsValueInternal(n.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
