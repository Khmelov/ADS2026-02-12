package by.it.group510901.poltorak.lesson12;

import java.util.Collection;
import java.util.List;
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

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }

        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }

        return node;
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

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        Integer key = (Integer) keyObj;
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }
    private String oldValueRes;

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Ключ не может быть null");
        oldValueRes = null;
        root = insert(root, key, value);
        return oldValueRes;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            oldValueRes = node.value;
            node.value = value;
            return node;
        }
        updateHeight(node);
        return balance(node);
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (!containsKey(key)) return null;
        String val = get(key);
        root = delete(root, key);
        size--;
        return val;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    private Node delete(Node root, Integer key) {
        if (root == null) return null;

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = delete(root.left, key);
        } else if (cmp > 0) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            Node successor = getMin(root.right);
            root.key = successor.key;
            root.value = successor.value;
            root.right = delete(root.right, successor.key);
        }

        if (root == null) return null;

        updateHeight(root);
        return balance(root);
    }

    private Node getMin(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        appendInOrder(node.right, sb);
    }
}