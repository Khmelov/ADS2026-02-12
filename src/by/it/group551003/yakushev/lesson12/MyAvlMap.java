package by.it.group551003.yakushev.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height = 1;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // временные результаты рекурсивных операций
    private String oldValue;
    private boolean found;

    private int h(Node n) {
        return n == null ? 0 : n.height;
    }

    private void update(Node n) {
        n.height = Math.max(h(n.left), h(n.right)) + 1;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        update(y);
        update(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        return y;
    }

    private Node balance(Node n) {
        update(n);
        int b = h(n.left) - h(n.right);
        if (b > 1) {
            if (h(n.left.left) < h(n.left.right)) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (b < -1) {
            if (h(n.right.right) < h(n.right.left)) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) {
            size++;
            found = false;
            return new Node(key, value);
        }
        if (key < n.key) n.left = insert(n.left, key, value);
        else if (key > n.key) n.right = insert(n.right, key, value);
        else {
            found = true;
            oldValue = n.value;
            n.value = value;
            return n;
        }
        return balance(n);
    }

    private Node deleteMin(Node n) {
        if (n.left == null) return n.right;
        n.left = deleteMin(n.left);
        return balance(n);
    }

    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) n.left = delete(n.left, key);
        else if (key > n.key) n.right = delete(n.right, key);
        else {
            found = true;
            oldValue = n.value;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node m = n.right;
            while (m.left != null) m = m.left;
            n.key = m.key;
            n.value = m.value;
            n.right = deleteMin(n.right);
        }
        return balance(n);
    }

    private Node findNode(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        found = false;
        oldValue = null;
        root = insert(root, key, value);
        return found ? oldValue : null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        found = false;
        oldValue = null;
        root = delete(root, (Integer) key);
        if (!found) return null;
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        Node n = findNode(key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
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