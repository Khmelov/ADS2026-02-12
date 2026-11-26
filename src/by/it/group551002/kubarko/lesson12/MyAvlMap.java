package by.it.group551002.kubarko.lesson12;

import java.util.Collection;
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

    private Node rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        pivot.right = node;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private Node rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private Node balance(Node node) {
        if (node == null) {
            return null;
        }
        updateHeight(node);
        int balance = height(node.left) - height(node.right);

        if (balance > 1) {
            if (height(node.left.left) < height(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balance < -1) {
            if (height(node.right.right) < height(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node find(Integer key) {
        Node current = root;
        while (current != null) {
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                return current;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    private Node insert(Node node, Integer key, String value) {
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

    private Node remove(Node node, Integer key) {
        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.key = successor.key;
            node.value = successor.value;
            node.right = remove(node.right, successor.key);
        }
        return balance(node);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result, new boolean[]{true});
        return result.append("}").toString();
    }

    private void appendInOrder(Node node, StringBuilder result, boolean[] first) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, result, first);
        if (!first[0]) {
            result.append(", ");
        }
        result.append(node.key).append("=").append(node.value);
        first[0] = false;
        appendInOrder(node.right, result, first);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node existing = find(key);
        if (existing != null) {
            String previous = existing.value;
            existing.value = value;
            return previous;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node existing = find(integerKey);
        if (existing == null) {
            return null;
        }
        String previous = existing.value;
        root = remove(root, integerKey);
        size--;
        return previous;
    }

    @Override
    public String get(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        Node node = find(integerKey);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Integer integerKey = (Integer) key;
        if (integerKey == null) {
            throw new NullPointerException();
        }
        return find(integerKey) != null;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
