package by.it.group551004.sharkevich.lesson12;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        int key;
        String value;
        Node left, right;
        int height;
        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node rotateRight(Node n) {
        Node t1 = n.left;
        Node t2 = t1.right;
        t1.right = n;
        n.left = t2;
        updateHeight(n);
        updateHeight(t1);
        return t1;
    }

    private Node rotateLeft(Node n) {
        Node t1 = n.right;
        Node t2 = t1.left;
        t1.left = n;
        n.right = t2;
        updateHeight(n);
        updateHeight(t1);
        return t1;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node findNode(int key) {
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                curr = curr.left;
            } else if (key > curr.key) {
                curr = curr.right;
            } else {
                return curr;
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
        } else if (key > n.key) {
            n.right = insert(n.right, key,value);
        } else {
            return n;
        }
        return balance(n);
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    private Node findMin(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
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
            Node min = findMin(n.right);
            n.key = min.key;
            n.value = min.value;
            n.right = delete(n.right, min.key);
        }
        return balance(n);
    }

    public String put(Integer key, String value) {
        Node existing = findNode(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = findNode((Integer) key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    public boolean containsKey(Object key) {
        return key instanceof Integer && findNode((Integer) key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
}
