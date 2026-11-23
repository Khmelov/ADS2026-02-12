package by.it.group551001.romanovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static final class Node {
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

    private static int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private static void update(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private static Node rotateRight(Node node) {
        Node top = node.left;
        node.left = top.right;
        top.right = node;
        update(node);
        update(top);
        return top;
    }

    private static Node rotateLeft(Node node) {
        Node top = node.right;
        node.right = top.left;
        top.left = node;
        update(node);
        update(top);
        return top;
    }

    private static Node balance(Node node) {
        update(node);
        int factor = height(node.left) - height(node.right);
        if (factor > 1) {
            if (height(node.left.left) < height(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (factor < -1) {
            if (height(node.right.right) < height(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node find(int key) {
        Node node = root;
        while (node != null && node.key != key) {
            node = key < node.key ? node.left : node.right;
        }
        return node;
    }

    private static int key(Object key) {
        return (Integer) Objects.requireNonNull(key);
    }

    @Override
    public String put(Integer key, String value) {
        int k = key(key);
        Node existing = find(k);
        if (existing != null) {
            String previous = existing.value;
            existing.value = value;
            return previous;
        }
        root = insert(root, k, value);
        size++;
        return null;
    }

    private static Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        int k = key(key);
        Node node = find(k);
        if (node == null) {
            return null;
        }
        String previous = node.value;
        root = delete(root, k);
        size--;
        return previous;
    }

    private static Node delete(Node node, int key) {
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
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return balance(node);
    }

    @Override
    public String get(Object key) {
        Node node = find(key(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private static boolean containsValue(Node node, Object value) {
        return node != null && (Objects.equals(node.value, value)
                || containsValue(node.left, value) || containsValue(node.right, value));
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        append(root, result);
        return result.append('}').toString();
    }

    private static void append(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }
        append(node.left, result);
        if (result.length() > 1) {
            result.append(", ");
        }
        result.append(node.key).append('=').append(node.value);
        append(node.right, result);
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
