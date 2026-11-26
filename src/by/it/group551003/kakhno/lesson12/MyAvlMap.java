package by.it.group551003.kakhno.lesson12;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
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

    private int balanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    private void fixHeight(Node node) {
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        if (leftHeight > rightHeight) {
            node.height = leftHeight + 1;
        } else {
            node.height = rightHeight + 1;
        }
    }

    private Node rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        leftChild.right = node;

        fixHeight(node);
        fixHeight(leftChild);

        return leftChild;
    }

    private Node rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        rightChild.left = node;

        fixHeight(node);
        fixHeight(rightChild);

        return rightChild;
    }

    private Node balance(Node node) {
        if (node == null) {
            return null;
        }

        fixHeight(node);

        if (balanceFactor(node) == 2) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        if (balanceFactor(node) == -2) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        return node;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Node existing = findNode(root, key);
        String oldValue = null;

        if (existing != null) {
            oldValue = existing.value;
        } else {
            size++;
        }

        root = insert(root, key, value);
        return oldValue;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        int comparison = key.compareTo(node.key);

        if (comparison < 0) {
            node.left = insert(node.left, key, value);
        } else if (comparison > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private Node findNode(Node node, Integer key) {
        while (node != null) {
            int comparison = key.compareTo(node.key);

            if (comparison == 0) {
                return node;
            } else if (comparison < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode(root, (Integer) key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode(root, (Integer) key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer integerKey = (Integer) key;
        Node node = findNode(root, integerKey);

        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        root = delete(root, integerKey);
        size--;

        return oldValue;
    }

    private Node delete(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int comparison = key.compareTo(node.key);

        if (comparison < 0) {
            node.left = delete(node.left, key);
        } else if (comparison > 0) {
            node.right = delete(node.right, key);
        } else {
            Node left = node.left;
            Node right = node.right;

            if (right == null) {
                return left;
            }

            Node minimum = findMin(right);
            minimum.right = removeMin(right);
            minimum.left = left;

            return balance(minimum);
        }

        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = removeMin(node.left);
        return balance(node);
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
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result);
        result.append("}");
        return result.toString();
    }

    private void appendInOrder(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }

        appendInOrder(node.left, result);

        if (result.length() > 1) {
            result.append(", ");
        }

        result.append(node.key).append("=").append(node.value);

        appendInOrder(node.right, result);
    }

    // Остальные методы интерфейса Map не входят
    // в обязательный список задания.

    @Override
    public boolean containsValue(Object value) {
        return containsValueNode(root, value);
    }

    private boolean containsValueNode(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValueNode(node.left, value)
                || containsValueNode(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry
                : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
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
