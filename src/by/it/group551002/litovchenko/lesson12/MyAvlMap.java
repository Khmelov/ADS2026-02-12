package by.it.group551002.litovchenko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

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

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int balance = getBalance(n);
        if (balance > 1) {
            if (getBalance(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (balance < -1) {
            if (getBalance(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
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
        return findNode(root, (Integer) key) != null;
    }

    private Node findNode(Node node, Integer key) {
        Node curr = node;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode(root, (Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node existing = findNode(root, key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        Node existing = findNode(root, (Integer) key);
        if (existing == null) return null;
        String old = existing.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    private Node delete(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node minNode = getMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = delete(node.right, minNode.key);
            }
        }
        if (node == null) return null;
        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrder(node.right, sb);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) return false;
        return containsAllEntries(root, m);
    }

    private boolean containsAllEntries(Node node, Map<?, ?> m) {
        if (node == null) return true;
        Object val = m.get(node.key);
        if (!Objects.equals(node.value, val)) return false;
        if (val == null && !m.containsKey(node.key)) return false;
        return containsAllEntries(node.left, m) && containsAllEntries(node.right, m);
    }

    @Override
    public int hashCode() {
        return hashCode(root);
    }

    private int hashCode(Node node) {
        if (node == null) return 0;
        int h = Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
        return h + hashCode(node.left) + hashCode(node.right);
    }
}
