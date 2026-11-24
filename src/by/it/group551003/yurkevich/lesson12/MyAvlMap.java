package by.it.group551003.yurkevich.lesson12;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String[] old = new String[1];
        root = insert(root, key, value, old);
        return old[0];
    }

    private Node insert(Node node, int key, String value, String[] old) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value, old);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, old);
        } else {
            old[0] = node.value;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String[] old = new String[1];
        root = delete(root, (Integer) key, old);
        return old[0];
    }

    private Node delete(Node node, int key, String[] old) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key, old);
        } else if (key > node.key) {
            node.right = delete(node.right, key, old);
        } else {
            old[0] = node.value;
            size--;

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node min = node.right;
            while (min.left != null) min = min.left;

            node.key = min.key;
            node.value = min.value;

            size++;
            node.right = delete(node.right, min.key, new String[1]);
        }

        return rebalance(node);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node cur = root;
        int k = (Integer) key;
        while (cur != null) {
            if (k < cur.key) cur = cur.left;
            else if (k > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
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

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
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

    private Node rebalance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}