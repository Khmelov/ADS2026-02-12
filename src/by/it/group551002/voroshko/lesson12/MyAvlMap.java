package by.it.group551002.voroshko.lesson12;

import com.sun.source.tree.BreakTree;

import java.util.*;
import java.util.Map.*;

public class MyAvlMap implements Map<Integer, String> {
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

    private Node root;
    private int size;

    private int height(Node n) {
        if (n == null) return 0;
        return n.height;
    }

    private void updateHeight(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);

        if (hl > hr) {
            n.height = hl + 1;
        } else {
            n.height = hr + 1;
        }
    }

    private int balanceFactor(Node n) {
        if (n == null) return 0;
        return height(n.left) - height(n.right);
    }

    private Node rotateLeft(Node a) {
        Node b = a.right;
        Node temp = b.left;

        b.left = a;
        a.right = temp;

        updateHeight(a);
        updateHeight(b);

        return b;
    }

    private Node rotateRight(Node a) {
        Node b = a.left;
        Node temp = b.right;

        b.right = a;
        a.left = temp;

        updateHeight(a);
        updateHeight(b);

        return b;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        inOrder(root, sb);

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;

        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = new String[1];

        root = insert(root, key, value, old);

        if (old[0] == null) {
            size++;
        }

        return old[0];
    }

    private Node insert(Node node, Integer key, String value, String[] old) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = insert(node.left, key, value, old);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value, old);
        } else {
            old[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String[] old = new String[1];

        Integer k = (Integer) key;

        if(!containsKey(k)) {
            return null;
        }

        root = delete(root, k, old);
        size--;

        return old[0];
    }

    private Node delete(Node node, Integer key, String[] old) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = delete(node.left, key, old);
        } else if (cmp > 0) {
            node.right = delete(node.right, key, old);
        } else {
            old[0] = node.value;
            if (node.left == null) return node.right;
            if (node.right == null) return  node.left;

            Node min = node.right;

            while (min.left != null) {
                min = min.left;
            }

            node.key = min.key;
            node.value = min.value;
            node.right = deleteMin(node.right);
        }

        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);

        return balance(node);
    }

    @Override
    public String get(Object key) {
        Node cur = root;

        Integer k = (Integer) key;

        while (cur != null) {
            int cmp = k.compareTo(cur.key);

            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return cur.value;
            }
        }

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        Integer k = (Integer) key;

        Node cur = root;

        while (cur != null) {
            int cmp = k.compareTo(cur.key);

            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return true;
            }
        }

        return false;
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
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {

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
}
