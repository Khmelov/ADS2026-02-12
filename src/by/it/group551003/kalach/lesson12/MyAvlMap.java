package by.it.group551003.kalach.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // ---------- узел дерева ----------
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
    private String oldValue; // сюда рекурсия кладёт прежнее значение для put/remove

    // ---------- вспомогательные методы АВЛ ----------
    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void update(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
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
        int diff = height(n.left) - height(n.right);
        if (diff > 1) {
            if (height(n.left.left) < height(n.left.right)) {
                n.left = rotateLeft(n.left);        // случай «лево-право»
            }
            return rotateRight(n);
        }
        if (diff < -1) {
            if (height(n.right.right) < height(n.right.left)) {
                n.right = rotateRight(n.right);     // случай «право-лево»
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node find(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur;
        }
        return null;
    }

    // ---------- toString ----------
    @Override
    public String toString() {  //через симметричный обход
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    // ---------- put ----------
    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = insert(root, key, value);
        return oldValue;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else if (key > n.key) {
            n.right = insert(n.right, key, value);
        } else {
            oldValue = n.value;     // ключ уже есть: заменяем значение
            n.value = value;
            return n;
        }
        return balance(n);
    }

    // ---------- remove ----------
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        oldValue = null;
        root = delete(root, (Integer) key);
        return oldValue;
    }

    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            oldValue = n.value;
            size--;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            // два потомка: заменяем минимумом правого поддерева
            Node min = n.right;
            while (min.left != null) min = min.left;
            n.key = min.key;
            n.value = min.value;
            n.right = removeMin(n.right);
        }
        return balance(n);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    // ---------- get / containsKey ----------
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find((Integer) key) != null;
    }

    // ---------- size / clear / isEmpty ----------
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

    // ---------- необязательные методы интерфейса Map ----------
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
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
