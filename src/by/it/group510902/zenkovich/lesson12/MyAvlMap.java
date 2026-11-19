package by.it.group510902.zenkovich.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
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

    Node balance(Node node) {
        updateHeight(node);
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balanceFactor < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    Node put(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        return balance(node);
    }

    Node findMin(Node node) {
        return node.left == null ? node : findMin(node.left);
    }


    Node remove(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {

            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {

                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node temp = findMin(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key);
            }
        }

        if (node == null) {
            return null;
        }

        return balance(node);
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrderTraversal(node.right, sb);
        }
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
    public String put(Integer key, String value) {
        String oldValue = get(key);
        if (oldValue == null) {
            size++;
        }
        root = put(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        String oldValue = get(key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
            size--;
        }
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node current = root;
        Integer k = (Integer) key;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }



    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (node.value == null ? value == null : node.value.equals(value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}