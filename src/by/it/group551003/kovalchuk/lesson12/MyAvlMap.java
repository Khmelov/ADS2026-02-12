package by.it.group551003.kovalchuk.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;
    private int size = 0;

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

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private void fixHeight(Node node) {
        int hl = height(node.left);
        int hr = height(node.right);
        node.height = Math.max(hl, hr) + 1;
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;

        fixHeight(p);
        fixHeight(q);

        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;

        fixHeight(q);
        fixHeight(p);

        return p;
    }

    private Node balance(Node node) {
        fixHeight(node);

        if (balanceFactor(node) == 2) {
            if (balanceFactor(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        if (balanceFactor(node) == -2) {
            if (balanceFactor(node.left) > 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        return node;
    }

    private String oldValue;

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key)
            node.left = insert(node.left, key, value);
        else if (key > node.key)
            node.right = insert(node.right, key, value);
        else {
            oldValue = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private Node find(Node node, Integer key) {
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else
                return node;
        }
        return null;
    }

    private Node findMin(Node node) {
        return node.left == null ? node : findMin(node.left);
    }

    private Node removeMin(Node node) {
        if (node.left == null)
            return node.right;

        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node removeNode(Node node, Integer key) {
        if (node == null)
            return null;

        if (key < node.key)
            node.left = removeNode(node.left, key);
        else if (key > node.key)
            node.right = removeNode(node.right, key);
        else {
            oldValue = node.value;
            size--;

            Node left = node.left;
            Node right = node.right;

            if (right == null)
                return left;

            Node min = findMin(right);
            min.right = removeMin(right);
            min.left = left;

            return balance(min);
        }

        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = insert(root, key, value);
        return oldValue;
    }

    @Override
    public String get(Object key) {
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String remove(Object key) {
        oldValue = null;
        root = removeNode(root, (Integer) key);
        return oldValue;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer) key) != null;
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

    private void inorder(Node node, StringBuilder sb) {
        if (node == null)
            return;

        inorder(node.left, sb);

        if (sb.length() > 1)
            sb.append(", ");

        sb.append(node.key).append("=").append(node.value);

        inorder(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        sb.append("}");
        return sb.toString();
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
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}