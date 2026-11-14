package by.it.group510902.ryzhkov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    static class Node {
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

    Node root;
    int size;

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

        // Л-Л
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        // Л-П
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // П-П
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        // П-Л
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node findNode(Node node, int key) {
        if (node == null || node.key == key) {
            return node;
        }
        if (key < node.key) {
            return findNode(node.left, key);
        }
        return findNode(node.right, key);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean containsKey(Object key) {
        return findNode(root, (Integer) key) != null;
    }

    public String get(Object key) {
        Node node = findNode(root, (Integer) key);
        return node != null ? node.value : null;
    }

    public String put(Integer key, String value) {
        String[] oldValue = { null };
        root = put(root, key, value, oldValue);
        return oldValue[0];
    }

    private Node put(Node node, int key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    public String remove(Object key) {
        int intKey = (Integer) key;
        String[] oldValue = { null };
        if (containsKey(intKey)) {
            root = remove(root, intKey, oldValue);
            size--;
        }
        return oldValue[0];
    }

    private Node remove(Node node, int key, String[] oldValue) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = remove(node.left, key, oldValue);
        } else if (key > node.key) {
            node.right = remove(node.right, key, oldValue);
        } else {
            oldValue[0] = node.value;

            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node temp = getMinValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key, new String[1]);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    private Node getMinValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        buildString(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node != null) {
            buildString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            buildString(node.right, sb);
        }
    }

    // Заглушки
    public boolean containsValue(Object value) {
        return false;
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    public Set<Integer> keySet() {
        return null;
    }

    public Collection<String> values() {
        return null;
    }

    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}
