package by.it.group551001.docenko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height = 1;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private static Integer toKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (Integer) key;
    }

    private static int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private static void updateHeight(Node node) {
        int hl = height(node.left);
        int hr = height(node.right);
        node.height = (hl > hr ? hl : hr) + 1;
    }

    private static int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    private static Node rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        pivot.right = node;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private static Node rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private static Node balance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private static Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }
        return balance(node);
    }

    private static Node minNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private static Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    private static Node delete(Node node, Integer key) {
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node successor = minNode(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            return balance(successor);
        }
        return balance(node);
    }

    private static void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendInOrder(node.right, sb);
    }

    private static boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private static boolean matches(Node node, Map<?, ?> other) {
        if (node == null) {
            return true;
        }
        Object otherValue = other.get(node.key);
        boolean same = node.value == null
                ? otherValue == null && other.containsKey(node.key)
                : node.value.equals(otherValue);
        return same && matches(node.left, other) && matches(node.right, other);
    }

    private static int hash(Node node) {
        if (node == null) {
            return 0;
        }
        int h = node.key.hashCode() ^ (node.value == null ? 0 : node.value.hashCode());
        return h + hash(node.left) + hash(node.right);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        toKey(key);
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
        Node target = findNode(toKey(key));
        if (target == null) {
            return null;
        }
        String old = target.value;
        root = delete(root, target.key);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = findNode(toKey(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(toKey(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> other = (Map<?, ?>) o;
        return other.size() == size && matches(root, other);
    }

    @Override
    public int hashCode() {
        return hash(root);
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
