package by.it.group551003.klimuk.lesson12;

import java.util.Comparator;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(int key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;
    private final Comparator<? super Integer> comparator;

    public MyRbMap() {
        this.comparator = null;
    }

    public MyRbMap(Comparator<? super Integer> comparator) {
        this.comparator = comparator;
    }

    private int compare(int a, int b) {
        return comparator == null ? Integer.compare(a, b) : comparator.compare(a, b);
    }

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

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key is null");
        Node parent = null;
        Node cur = root;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = compare(key, cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                return old;
            }
        }
        Node z = new Node(key, value, RED);
        z.parent = parent;
        if (parent == null) root = z;
        else if (cmp < 0) parent.left = z;
        else parent.right = z;
        size++;
        insertFixup(z);
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            Node gp = z.parent.parent;
            if (z.parent == gp.left) {
                Node y = gp.right;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    gp.color = RED;
                    z = gp;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    gp.color = RED;
                    rotateRight(gp);
                }
            } else {
                Node y = gp.left;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    gp.color = RED;
                    z = gp;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    gp.color = RED;
                    rotateLeft(gp);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && findNode((Integer) key) != null;
    }

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            int c = compare(key, cur.key);
            if (c < 0) cur = cur.left;
            else if (c > 0) cur = cur.right;
            else return cur;
        }
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueHelper(n.left, value) || containsValueHelper(n.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = findNode((Integer) key);
        if (z == null) return null;
        String removed = z.value;
        deleteNode(z);
        size--;
        return removed;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yOrigColor = y.color;
        Node x;
        Node xParent;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOrigColor = y.color;
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

        if (yOrigColor == BLACK) {
            deleteFixup(x, xParent);
        }
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private void deleteFixup(Node x, Node parent) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == parent.left) {
                Node w = parent.right;
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (colorOf(w) == BLACK) {
                    Node wl = w == null ? null : w.left;
                    Node wr = w == null ? null : w.right;
                    if (colorOf(wl) == BLACK && colorOf(wr) == BLACK) {
                        if (w != null) w.color = RED;
                        x = parent;
                        parent = x.parent;
                    } else {
                        if (colorOf(wr) == BLACK) {
                            if (wl != null) wl.color = BLACK;
                            if (w != null) {
                                w.color = RED;
                                rotateRight(w);
                            }
                            w = parent.right;
                        }
                        if (w != null) {
                            w.color = parent.color;
                            if (w.right != null) w.right.color = BLACK;
                        }
                        parent.color = BLACK;
                        rotateLeft(parent);
                        x = root;
                        parent = null;
                    }
                } else {
                    x = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (colorOf(w) == BLACK) {
                    Node wl = w == null ? null : w.left;
                    Node wr = w == null ? null : w.right;
                    if (colorOf(wl) == BLACK && colorOf(wr) == BLACK) {
                        if (w != null) w.color = RED;
                        x = parent;
                        parent = x.parent;
                    } else {
                        if (colorOf(wl) == BLACK) {
                            if (wr != null) wr.color = BLACK;
                            if (w != null) {
                                w.color = RED;
                                rotateLeft(w);
                            }
                            w = parent.left;
                        }
                        if (w != null) {
                            w.color = parent.color;
                            if (w.left != null) w.left.color = BLACK;
                        }
                        parent.color = BLACK;
                        rotateRight(parent);
                        x = root;
                        parent = null;
                    }
                } else {
                    x = root;
                    parent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private boolean colorOf(Node n) {
        return n == null ? BLACK : n.color;
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
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap sub = new MyRbMap(comparator);
        addKeysLess(root, toKey, sub);
        return sub;
    }

    private void addKeysLess(Node n, int toKey, MyRbMap sub) {
        if (n == null) return;
        if (compare(n.key, toKey) < 0) {
            addKeysLess(n.left, toKey, sub);
            sub.put(n.key, n.value);
            addKeysLess(n.right, toKey, sub);
        } else {
            addKeysLess(n.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap sub = new MyRbMap(comparator);
        addKeysGreaterOrEq(root, fromKey, sub);
        return sub;
    }

    private void addKeysGreaterOrEq(Node n, int fromKey, MyRbMap sub) {
        if (n == null) return;
        if (compare(n.key, fromKey) >= 0) {
            addKeysGreaterOrEq(n.left, fromKey, sub);
            sub.put(n.key, n.value);
            addKeysGreaterOrEq(n.right, fromKey, sub);
        } else {
            addKeysGreaterOrEq(n.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inorder(n.right, sb);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return comparator;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}