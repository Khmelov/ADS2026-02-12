package by.it.group551003.popko.lesson12;

import java.util.SortedMap;
import java.util.TreeMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private int size;

    // ---------- вспомогательное ----------

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            Node gp = z.parent.parent;
            if (z.parent == gp.left) {
                Node uncle = gp.right;
                if (uncle != null && uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    gp.color = RED;
                    z = gp;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node uncle = gp.left;
                if (uncle != null && uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    gp.color = RED;
                    z = gp;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private void deleteFixup(Node x, Node parent) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == (parent == null ? null : parent.left)) {
                Node w = parent.right;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (w == null) { x = parent; parent = x.parent; continue; }
                if ((w.left == null || w.left.color == BLACK) &&
                        (w.right == null || w.right.color == BLACK)) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.right == null || w.right.color == BLACK) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (w == null) { x = parent; parent = x.parent; continue; }
                if ((w.right == null || w.right.color == BLACK) &&
                        (w.left == null || w.left.color == BLACK)) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || w.left.color == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private Node find(Integer key) {
        Node n = root;
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

    // ---------- SortedMap ----------

    @Override
    public java.util.Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, fromKey, true, toKey, false, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, null, false, toKey, false, res);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, fromKey, true, null, false, res);
        return res;
    }

    private void collectRange(Node n, Integer from, boolean fromInc, Integer to, boolean toInc, TreeMap<Integer, String> res) {
        if (n == null) return;
        if (from != null && n.key.compareTo(from) < 0) {
            collectRange(n.right, from, fromInc, to, toInc, res);
            return;
        }
        if (to != null && n.key.compareTo(to) > 0) {
            collectRange(n.left, from, fromInc, to, toInc, res);
            return;
        }
        collectRange(n.left, from, fromInc, to, toInc, res);
        boolean okFrom = from == null || (fromInc ? n.key.compareTo(from) >= 0 : n.key.compareTo(from) > 0);
        boolean okTo = to == null || (toInc ? n.key.compareTo(to) <= 0 : n.key.compareTo(to) < 0);
        if (okFrom && okTo) res.put(n.key, n.value);
        collectRange(n.right, from, fromInc, to, toInc, res);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    // ---------- Map ----------

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) { return find((Integer) key) != null; }

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
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        Node existing = find(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        Node z = new Node(key, value);
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else x = x.right;
        }
        z.parent = y;
        if (y == null) root = z;
        else if (key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;
        size++;
        insertFixup(z);
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node z = find(key);
        if (z == null) return null;
        String old = z.value;
        Node y = z;
        Node x;
        Node xParent;
        boolean yOriginalColor = y.color;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minNode(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
                if (x != null) x.parent = y;
            } else {
                xParent = y.parent;
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
        if (yOriginalColor == BLACK) {
            deleteFixup(x, xParent);
        }
        return old;
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        for (java.util.Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }
}