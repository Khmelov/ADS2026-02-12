package by.it.group551001.telipko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height = 1;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void update(Node n) {
        int hl = height(n.left), hr = height(n.right);
        n.height = 1 + (Math.max(hl, hr));
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

    private Node fix_balance(Node n) {
        update(n);
        int b = balance(n);
        if (b > 1) {
            if (balance(n.left) < 0)
                n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (b < -1) {
            if (balance(n.right) > 0)
                n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private String oldValue;

    private Node insert(Node n, Integer key, String value) {
        if (n == null) {
            size++;
            oldValue = null;
            return new Node(key, value);
        }
        int c = key.compareTo(n.key);
        if (c < 0) n.left = insert(n.left, key, value);
        else if (c > 0) n.right = insert(n.right, key, value);
        else {
            oldValue = n.value;
            n.value = value;
            return n;
        }
        return fix_balance(n);
    }

    @Override
    public String put(Integer key, String value) {
        root = insert(root, key, value);
        return oldValue;
    }

    private Node min(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return fix_balance(n);
    }

    private Node delete(Node n, Integer key) {
        if (n == null) return null;
        int c = key.compareTo(n.key);
        if (c < 0) n.left = delete(n.left, key);
        else if (c > 0) n.right = delete(n.right, key);
        else {
            oldValue = n.value;
            size--;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node m = min(n.right);
            m.right = removeMin(n.right);
            m.left = n.left;
            return fix_balance(m);
        }
        return fix_balance(n);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        oldValue = null;
        root = delete(root, (Integer) key);
        return oldValue;
    }


    private Node find(Object key) {
        if (!(key instanceof Integer k)) return null;
        Node n = root;
        while (n != null) {
            int c = k.compareTo(n.key);
            if (c == 0) return n;
            n = c < 0 ? n.left : n.right;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = find(key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
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

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        inorder(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        return sb.append('}').toString();
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(value, n.value)) return true;
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
