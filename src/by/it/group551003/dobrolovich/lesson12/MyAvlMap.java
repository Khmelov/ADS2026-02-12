package by.it.group551003.dobrolovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height = 1;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

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
            if (h(n.left.left) < h(n.left.right)) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (b < -1) {
            if (h(n.right.right) < h(n.right.left)) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node find(int key) {
        Node c = root;
        while (c != null) {
            if (key < c.key) {
                c = c.left;
            } else if (key > c.key) {
                c = c.right;
            } else {
                return c;
            }
        }
        return null;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) {
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else {
            n.right = insert(n.right, key, value);
        }
        return balance(n);
    }

    private Node delete(Node n, int key) {
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }
            Node m = n.right;
            while (m.left != null) {
                m = m.left;
            }
            n.key = m.key;
            n.value = m.value;
            n.right = delete(n.right, m.key);
        }
        return balance(n);
    }

    private void write(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        write(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        write(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (n.value == null ? value == null : n.value.equals(value)) {
            return true;
        }
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        write(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node n = find(key);
        if (n != null) {
            String old = n.value;
            n.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}