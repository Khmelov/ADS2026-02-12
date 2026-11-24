package by.it.group551002.yermakou.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        Integer key;
        String value;
        int height;
        Node left, right;

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

    private int getBalance(Node n) {
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

    private Node balance(Node node) {
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String[] oldValue = new String[1];
        root = remove(root, (Integer) key, oldValue);
        if (oldValue[0] != null) {
            size--;
        }
        return oldValue[0];
    }

    private Node remove(Node node, Integer key, String[] oldValue) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, oldValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, oldValue);
        } else {
            oldValue[0] = node.value;
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node maxLeft = getMax(node.left);
                node.key = maxLeft.key;
                node.value = maxLeft.value;
                node.left = remove(node.left, maxLeft.key, new String[1]);
            }
        }

        if (node == null) return null;
        return balance(node);
    }

    private Node getMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node current = root;
        Integer k = (Integer) key;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null || (get(key) == null && searchNode(root, key) != null);
    }

    private Node searchNode(Node node, Object key) {
        if (node == null || !(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        int cmp = k.compareTo(node.key);
        if (cmp < 0) return searchNode(node.left, key);
        if (cmp > 0) return searchNode(node.right, key);
        return node;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1) {
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

    // Нереализованные методы интерфейса Map
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
