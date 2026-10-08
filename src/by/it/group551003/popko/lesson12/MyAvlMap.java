package by.it.group551003.popko.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    // ---------- вспомогательное ----------

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t = x.right;
        x.right = y;
        y.left = t;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t = y.left;
        y.left = x;
        x.right = t;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, Integer key, String value, boolean[] added) {
        if (n == null) {
            added[0] = true;
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = insert(n.left, key, value, added);
        else if (cmp > 0) n.right = insert(n.right, key, value, added);
        else {
            n.value = value;
            return n;
        }
        return balance(n);
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node removeNode(Node n, Integer key, boolean[] removed) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = removeNode(n.left, key, removed);
        else if (cmp > 0) n.right = removeNode(n.right, key, removed);
        else {
            removed[0] = true;
            size--;
            if (n.left == null || n.right == null) {
                n = (n.left != null) ? n.left : n.right;
            } else {
                Node succ = minNode(n.right);
                n.key = succ.key;
                n.value = succ.value;
                boolean[] dummy = new boolean[1];
                size++; // компенсируем повторное уменьшение
                n.right = removeNode(n.right, succ.key, dummy);
            }
        }
        if (n == null) return null;
        return balance(n);
    }

    private Node find(Node n, Integer key) {
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n;
        }
        return null;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    // ---------- Map ----------

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (n.value == null ? value == null : n.value.equals(value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public String get(Object key) {
        Node n = find(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        Node n = find(root, key);
        String old = (n == null) ? null : n.value;
        boolean[] added = new boolean[1];
        root = insert(root, key, value, added);
        return old;
    }

    @Override
    public String remove(Object key) {
        Node n = find(root, (Integer) key);
        if (n == null) return null;
        String old = n.value;
        boolean[] removed = new boolean[1];
        root = removeNode(root, (Integer) key, removed);
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.setLength(sb.length() - 2); // убрать последнюю ", "
        sb.append('}');
        return sb.toString();
    }
}