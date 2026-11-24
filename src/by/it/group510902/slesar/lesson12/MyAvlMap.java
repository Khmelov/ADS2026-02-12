package by.it.group510902.slesar.lesson12;


import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height = 1;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node node) {
        Node child = node.left;
        Node t = child.right;

        child.right = node;
        node.left = t;

        updateHeight(node);
        updateHeight(child);
        return child;
    }

    private Node rotateLeft(Node node) {
        Node child = node.right;
        Node t = child.left;

        child.left = node;
        node.right = t;

        updateHeight(node);
        updateHeight(child);
        return child;
    }

    private Node fix(Node node) {
        if (node == null) return null;
        updateHeight(node);

        if (balance(node) > 1) {
            if (balance(node.left) < 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance(node) < -1) {
            if (balance(node.right) > 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node old = find(root, key);
        String previous = old == null ? null : old.value;
        root = insert(root, key, value);
        if (old == null) size++;
        return previous;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, value);
        else if (cmp > 0) node.right = insert(node.right, key, value);
        else node.value = value;

        return fix(node);
    }

    private Node find(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return node;
            node = cmp < 0 ? node.left : node.right;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find(root, (Integer) key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node node = find(root, k);
        if (node == null) return null;
        String previous = node.value;
        root = delete(root, k);
        size--;
        return previous;
    }

    private Node delete(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = delete(node.left, key);
        else if (cmp > 0) node.right = delete(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node min = node.right;
            while (min.left != null) min = min.left;
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key);
        }
        return fix(node);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        return node != null &&
                (java.util.Objects.equals(node.value, value)
                        || containsValue(node.left, value)
                        || containsValue(node.right, value));
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
    public void clear() {
        root = null;
        size = 0;
    }

    private void append(Node node, StringBuilder sb) {
        if (node == null) return;
        append(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        append(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        append(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Map)) return false;
        Map<?, ?> other = (Map<?, ?>) obj;
        if (other.size() != size) return false;
        for (Node n = minimum(root); n != null; ) {
            if (!java.util.Objects.equals(n.value, other.get(n.key)))
                return false;
            n = successor(root, n.key);
        }
        return true;
    }

    private Node minimum(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    private Node successor(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            if (node.key > key) {
                result = node;
                node = node.left;
            } else node = node.right;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Node n = minimum(root); n != null; n = successor(root, n.key))
            result += n.key.hashCode() ^ (n.value == null ? 0 : n.value.hashCode());
        return result;
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }
}
