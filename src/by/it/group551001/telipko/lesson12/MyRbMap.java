package by.it.group551001.telipko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private final Node NIL = new Node(null, null, BLACK);
    private Node root = NIL;
    private int size;

    public MyRbMap() {
        NIL.left = NIL.right = NIL.parent = NIL;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private Node find(Object key) {
        if (!(key instanceof Integer)) return NIL;
        Integer k = (Integer) key;
        Node n = root;
        while (n != NIL) {
            int c = k.compareTo(n.key);
            if (c == 0) return n;
            n = c < 0 ? n.left : n.right;
        }
        return NIL;
    }

    @Override
    public String get(Object key) {
        Node n = find(key);
        return n == NIL ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != NIL;
    }

    @Override
    public String put(Integer key, String value) {
        Node parent = NIL, n = root;
        while (n != NIL) {
            parent = n;
            int c = key.compareTo(n.key);
            if (c == 0) {
                String old = n.value;
                n.value = value;
                return old;
            }
            n = c < 0 ? n.left : n.right;
        }
        Node z = new Node(key, value, RED);
        z.left = z.right = NIL;
        z.parent = parent;
        if (parent == NIL) root = z;
        else if (key < parent.key) parent.left = z;
        else parent.right = z;
        size++;
        insertFixup(z);
        return null;
    }


    private void insertFixup(Node z) {
        while (z.parent.color == RED) {
            Node g = z.parent.parent;
            if (z.parent == g.left) {
                Node uncle = g.right;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    g.color = RED;
                    rotateRight(g);
                }
            } else {
                Node uncle = g.left;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    g.color = RED;
                    rotateLeft(g);
                }
            }
        }
        root.color = BLACK;
    }


    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    private Node min(Node n) {
        while (n.left != NIL) n = n.left;
        return n;
    }

    @Override
    public String remove(Object key) {
        Node z = find(key);
        if (z == NIL) return null;
        String old = z.value;

        Node y = z;
        boolean yColor = y.color;
        Node x;
        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = min(z.right);
            yColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        size--;
        if (yColor == BLACK)
            deleteFixup(x);
        NIL.parent = NIL;
        return old;
    }

    private void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new java.util.NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != NIL) n = n.right;
        return n.key;
    }

    private void copyRange(Node n, Integer from, Integer to, MyRbMap map) {
        if (n == NIL) return;
        boolean geFrom = from == null || n.key >= from;
        boolean ltTo = to == null || n.key < to;
        if (geFrom) copyRange(n.left, from, to, map);
        if (geFrom && ltTo) map.put(n.key, n.value);
        if (ltTo) copyRange(n.right, from, to, map);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        copyRange(root, null, toKey, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        copyRange(root, fromKey, null, map);
        return map;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap map = new MyRbMap();
        copyRange(root, fromKey, toKey, map);
        return map;
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
        root = NIL;
        size = 0;
    }

    private boolean hasValue(Node n, Object value) {
        if (n == NIL) return false;
        if (Objects.equals(value, n.value)) return true;
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == NIL) return;
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


    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
