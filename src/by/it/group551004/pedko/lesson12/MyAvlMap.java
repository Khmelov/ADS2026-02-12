package by.it.group551004.pedko.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap<Integer, String> implements Map<Integer, String> {

    private class Node {
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

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int balance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private void fix(Node node) {
        int l = height(node.left);
        int r = height(node.right);

        if (l > r) {
            node.height = 1 + l;
        } else {
            node.height = 1 + r;
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        fix(y);
        fix(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        fix(x);
        fix(y);
        return y;
    }

    private Node rebalance(Node node) {
        fix(node);
        int b = balance(node);

        if (b > 1) {
            if (balance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (b < -1) {
            if (balance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private int compare(Object a, Object b) {
        java.lang.Integer x = (java.lang.Integer) a;
        java.lang.Integer y = (java.lang.Integer) b;
        return x.compareTo(y);
    }

    private Node find(Object key) {
        Node current = root;

        while (current != null) {
            int c = compare(key, current.key);

            if (c == 0) {
                return current;
            }
            if (c < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        int c = compare(key, node.key);

        if (c < 0) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }

        return rebalance(node);
    }

    private Node removeNode(Node node, Object key) {
        if (node == null) {
            return null;
        }

        int c = compare(key, node.key);

        if (c < 0) {
            node.left = removeNode(node.left, key);
        } else if (c > 0) {
            node.right = removeNode(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            Node min = node.right;
            while (min.left != null) {
                min = min.left;
            }

            node.key = min.key;
            node.value = min.value;
            node.right = removeNode(node.right, min.key);
        }

        return rebalance(node);
    }

    private java.lang.String buildString(Node node) {
        if (node == null) {
            return "";
        }

        java.lang.String left = buildString(node.left);
        java.lang.String current = node.key + "=" + node.value;
        java.lang.String right = buildString(node.right);

        if (left.length() == 0) {
            if (right.length() == 0) {
                return current;
            }
            return current + ", " + right;
        }

        if (right.length() == 0) {
            return left + ", " + current;
        }

        return left + ", " + current + ", " + right;
    }

    @Override
    public java.lang.String toString() {
        return "{" + buildString(root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        Node node = find(key);

        if (node != null) {
            String old = node.value;
            node.value = value;
            return old;
        }

        root = insert(root, key, value);
        size = size + 1;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        String old = node.value;
        root = removeNode(root, key);
        size = size - 1;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
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

    // не обязательные

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Set<Integer> keySet() { return null; }

    @Override
    public Collection<String> values() { return null; }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}