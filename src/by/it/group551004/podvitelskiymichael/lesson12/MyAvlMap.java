package by.it.group551004.podvitelskiymichael.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height = 1;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private static void update(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);
        n.height = 1 + (hl > hr ? hl : hr);
    }

    private static Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        update(y);
        update(x);
        return x;
    }

    private static Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        return y;
    }

    private static Node rebalance(Node n) {
        update(n);
        int balance = height(n.left) - height(n.right);
        if (balance > 1) {
            if (height(n.left.left) < height(n.left.right)) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (balance < -1) {
            if (height(n.right.right) < height(n.right.left)) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private static Node insert(Node n, int key, String value) {
        if (n == null) {
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else {
            n.right = insert(n.right, key, value);
        }
        return rebalance(n);
    }

    private static Node delete(Node n, int key) {
        if (n == null) {
            return null;
        }
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }
            Node successor = n.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            n.key = successor.key;
            n.value = successor.value;
            n.right = delete(n.right, successor.key);
        }
        return rebalance(n);
    }

    private Node find(int key) {
        Node n = root;
        while (n != null) {
            if (key < n.key) {
                n = n.left;
            } else if (key > n.key) {
                n = n.right;
            } else {
                return n;
            }
        }
        return null;
    }

    private static boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    private static String render(Node n) {
        if (n == null) {
            return "";
        }
        String left = render(n.left);
        String right = render(n.right);
        String result = left.isEmpty() ? "" : left + ", ";
        result += n.key + "=" + n.value;
        if (!right.isEmpty()) {
            result += ", " + right;
        }
        return result;
    }

    @Override
    public String toString() {
        return "{" + render(root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;
        Node existing = find(k);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insert(root, k, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int k = (Integer) key;
        Node existing = find(k);
        if (existing == null) {
            return null;
        }
        String old = existing.value;
        root = delete(root, k);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
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
        return hasValue(root, value);
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