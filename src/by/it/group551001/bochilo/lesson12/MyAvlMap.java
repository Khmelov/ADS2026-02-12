package by.it.group551001.bochilo.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    //Создайте аналог Map на основе АВЛ-дерева БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node {
        Integer key;
        String value;
        int height = 1;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node findNode(Object key) {
        Integer k = (Integer) key;
        Node node = root;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if (cmp == 0) {
                return node;
            }
            node = cmp < 0 ? node.left : node.right;
        }
        return null;
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
            node.value = value;
            return node;
        }
        return balance(node);
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node delete(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node min = node.right;
            while (min.left != null) {
                min = min.left;
            }
            min.right = removeMin(node.right);
            min.left = node.left;
            return balance(min);
        }
        return balance(node);
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        inOrder(node.right, sb);
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node node = findNode(key);
        if (node != null) {
            String old = node.value;
            node.value = value;
            return old;
        }
        root = insert(root, key, value);
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        String old = node.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
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
