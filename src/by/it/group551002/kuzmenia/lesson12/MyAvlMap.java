package by.it.group551002.kuzmenia.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        int key;
        String value;
        int height;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendInOrder(node.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        Node found = findNode(key);
        if (found != null) {
            String old = found.value;
            found.value = value;
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
        Node found = findNode((Integer) key);
        if (found == null) {
            return null;                    // нечего удалять
        }
        String old = found.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node found = findNode((Integer) key);
        return found == null ? null : found.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
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

    /////////////////////////////////////////////////////////////////////////
    //////                   Вспомогательные методы                   ///////
    /////////////////////////////////////////////////////////////////////////

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    private static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private static void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private static int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private static Node rotateRight(Node y) {
        Node x = y.left;
        Node b = x.right;

        x.right = y;
        y.left = b;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private static Node rotateLeft(Node x) {
        Node y = x.right;
        Node b = y.left;

        y.left = x;
        x.right = b;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private static Node rebalance(Node n) {
        updateHeight(n);
        int balance = balanceFactor(n);

        if (balance > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }

        if (balance < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }

        return n;
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
        return rebalance(n);
    }

    private Node delete(Node n, int key) {
        if (n == null) {
            return null;
        }
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
            Node min = n.right;
            while (min.left != null) {
                min = min.left;
            }
            n.key = min.key;
            n.value = min.value;
            n.right = delete(n.right, min.key);
        }
        return rebalance(n);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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