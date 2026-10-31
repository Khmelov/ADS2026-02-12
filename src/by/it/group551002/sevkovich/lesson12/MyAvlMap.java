package by.it.group551002.sevkovich.lesson12;

import java.util.Objects;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        byte height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 0;
        }

        byte getHeight() {
            return height;
        }

        byte getBFactor() {
            int rightHeight = (right != null) ? right.getHeight() : -1;
            int leftHeight = (left != null) ? left.getHeight() : -1;
            return (byte) (rightHeight - leftHeight);
        }

        void fixHeight() {
            int rightHeight = (right != null) ? right.getHeight() : -1;
            int leftHeight = (left != null) ? left.getHeight() : -1;
            this.height = (byte) (Math.max(rightHeight, leftHeight) + 1);
        }

        Node rotateRight() {
            Node q = this.left;
            this.left = q.right;
            q.right = this;
            this.fixHeight();
            q.fixHeight();
            return q;
        }

        Node rotateLeft() {
            Node q = this.right;
            this.right = q.left;
            q.left = this;
            this.fixHeight();
            q.fixHeight();
            return q;
        }

        Node balance() {
            this.fixHeight();

            if (this.getBFactor() == 2) {
                if (this.right.getBFactor() < 0) {
                    this.right = this.right.rotateRight();
                }
                return this.rotateLeft();
            }

            if (this.getBFactor() == -2) {
                if (this.left.getBFactor() > 0) {
                    this.left = this.left.rotateLeft();
                }
                return this.rotateRight();
            }

            return this;
        }
    }

    private Node head;
    private int size;

    private Node insert(Node dst, Node src) {
        if (dst == null) {
            return src;
        }

        if (src.key.intValue() < dst.key.intValue()) {
            dst.left = insert(dst.left, src);
        } else {
            dst.right = insert(dst.right, src);
        }

        return dst.balance();
    }

    private Node find(Node node, int key) {
        if (node == null || key == node.key.intValue()) {
            return node;
        }

        if (key < node.key.intValue()) {
            return find(node.left, key);
        } else {
            return find(node.right, key);
        }
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
        return node.balance();
    }

    private Node remove(Node node, int key) {
        if (node == null) {
            return null;
        }

        int nodeKey = node.key.intValue();

        if (key < nodeKey) {
            node.left = remove(node.left, key);
        } else if (key > nodeKey) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = removeMin(node.right);
        }

        return node.balance();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        Node existing = find(head, key.intValue());

        if (existing != null) {
            String oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }

        head = insert(head, new Node(key, value));
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = find(head, ((Integer) key).intValue());
        return node != null ? node.value : null;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null) {
            return null;
        }

        Node node = find(head, key.intValue());

        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        head = remove(head, key.intValue());
        size--;
        return oldValue;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return find(head, ((Integer) key).intValue()) != null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(head, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("values");
    }

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }

    private String toString(Node node) {
        if (node == null) {
            return "";
        }

        String left = toString(node.left);
        String right = toString(node.right);
        String current = node.key + "=" + node.value;

        if (left.length() == 0 && right.length() == 0) {
            return current;
        }
        if (left.length() == 0) {
            return current + ", " + right;
        }
        if (right.length() == 0) {
            return left + ", " + current;
        }

        return left + ", " + current + ", " + right;
    }

    @Override
    public String toString() {
        return "{" + toString(head) + "}";
    }
}