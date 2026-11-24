package by.it.group551002.savitsky.lesson12;

import java.util.Collection;
import java.util.List;
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

    @Override
    public String toString() {
        return "{" + inOrder(root) + "}";
    }

    private String inOrder(Node node) {
        if (node == null) {
            return "";
        }

        String left = inOrder(node.left);
        String right = inOrder(node.right);

        String result = node.key + "=" + node.value;

        if (!left.isEmpty()) {
            result = left + ", " + result;
        }
        if (!right.isEmpty()) {
            result = result + ", " + right;
        }

        return result;
    }

    @Override
    public String put(Integer key, String value) {
        Node existing = findNode(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }

        root = insert(root, key, value);
        size++;

        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node found = findNode((Integer) key);
        if (found == null) {
            return null;
        }

        String old = found.value;
        root = delete(root, (Integer) key);
        size--;

        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode((Integer) key) != null;
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

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balance(Node node) {
        return height(node.left) - height(node.right);
    }

    private Node rotateRight(Node nodeToDown) {
        Node newRoot = nodeToDown.left;
        nodeToDown.left = newRoot.right;
        newRoot.right = nodeToDown;

        updateHeight(nodeToDown);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node rotateLeft(Node nodeToDown) {
        Node newRoot = nodeToDown.right;
        nodeToDown.right = newRoot.left;
        newRoot.left = nodeToDown;

        updateHeight(nodeToDown);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node rebalance(Node node) {
        updateHeight(node);

        int balance = balance(node);

        if (balance > 1) {
            if (balance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (balance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node findNode(int key) {
        Node cur = root;

        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }

        return null;
    }

    private Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }

        return rebalance(node);
    }

    private Node minNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    private Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            Node m = minNode(node.right);
            node.key = m.key;
            node.value = m.value;
            node.right = delete(node.right, m.key);
        }

        return rebalance(node);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}