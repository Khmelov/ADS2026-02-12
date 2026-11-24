package by.it.group551002.kurzhalov.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

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

    // ================= AVL core =================

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int b = balance(n);
        if (b > 1) {
            if (balance(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (b < -1) {
            if (balance(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) return new Node(key, value);
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else if (key > n.key) {
            n.right = insert(n.right, key, value);
        } else {
            n.value = value;
            return n;
        }
        return rebalance(n);
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node m = minNode(n.right);
            n.key = m.key;
            n.value = m.value;
            n.right = delete(n.right, m.key);
        }
        return rebalance(n);
    }

    private Node find(Node n, int key) {
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    // ================= Map API =================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        Node existing = find(root, key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find(root, (Integer) key);
        if (n == null) return null;
        String old = n.value;
        root = delete(root, (Integer) key);
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // ================= toString =================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    // ================= keySet =================

    private class KeyIterator implements Iterator<Integer> {
        private Node[] stack = new Node[16];
        private int top = 0;
        private Node next;

        KeyIterator() {
            pushLeft(root);
            advance();
        }

        private void pushLeft(Node n) {
            while (n != null) {
                if (top == stack.length) {
                    Node[] bigger = new Node[stack.length * 2];
                    System.arraycopy(stack, 0, bigger, 0, stack.length);
                    stack = bigger;
                }
                stack[top++] = n;
                n = n.left;
            }
        }

        private void advance() {
            if (top == 0) {
                next = null;
            } else {
                next = stack[--top];
                pushLeft(next.right);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Integer next() {
            if (next == null) throw new NoSuchElementException();
            Integer k = next.key;
            advance();
            return k;
        }
    }

    @Override
    public Set<Integer> keySet() {
        return new AbstractSet<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new KeyIterator();
            }

            @Override
            public int size() {
                return size;
            }
        };
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