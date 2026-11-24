package by.it.group551003.kalach.lesson12;


import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // ---------- узел дерева ----------
    private static class Node {
        int key;
        String value;
        boolean color = RED;
        Node left, right, parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // фиктивный чёрный лист (вместо null), как в классическом алгоритме
    private final Node NIL = new Node(0, null);
    private Node root;
    private int size;

    public MyRbMap() {
        NIL.color = BLACK;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
        root = NIL;
    }

    // ---------- повороты ----------
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

    private Node find(int key) {
        Node cur = root;
        while (cur != NIL) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur;
        }
        return NIL;
    }

    private Node minimum(Node n) {
        while (n.left != NIL) n = n.left;
        return n;
    }

    private Node maximum(Node n) {
        while (n.right != NIL) n = n.right;
        return n;
    }

    // ---------- toString ----------
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == NIL) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    // ---------- put ----------
    @Override
    public String put(Integer key, String value) {
        Node parent = NIL;
        Node cur = root;
        while (cur != NIL) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                return old;
            }
        }
        Node z = new Node(key, value);
        z.parent = parent;
        z.left = NIL;
        z.right = NIL;
        z.color = RED;
        if (parent == NIL) root = z;
        else if (key < parent.key) parent.left = z;
        else parent.right = z;
        size++;
        insertFixup(z);
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
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
                Node uncle = z.parent.parent.left;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
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

    // ---------- remove ----------
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = find((Integer) key);
        if (z == NIL) return null;
        String old = z.value;
        deleteNode(z);
        size--;
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;
        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
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
        if (yOriginalColor == BLACK) deleteFixup(x);
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

    // ---------- get / containsKey / containsValue ----------
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == NIL ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find((Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == NIL) return false;
        boolean same = value == null ? n.value == null : value.equals(n.value);
        return same || hasValue(n.left, value) || hasValue(n.right, value);
    }

    // ---------- size / clear / isEmpty ----------
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ---------- SortedMap: headMap / tailMap / firstKey / lastKey ----------
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, result, null, toKey);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, result, fromKey, null);
        return result;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap result = new MyRbMap();
        copyRange(root, result, fromKey, toKey);
        return result;
    }

    // копирует ключи из [from, to) ; null означает «без ограничения»
    private void copyRange(Node n, MyRbMap dst, Integer from, Integer to) {
        if (n == NIL) return;
        if (from == null || n.key >= from) copyRange(n.left, dst, from, to);
        if ((from == null || n.key >= from) && (to == null || n.key < to)) {
            dst.put(n.key, n.value);
        }
        if (to == null || n.key < to) copyRange(n.right, dst, from, to);
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
    }

    // ---------- необязательные методы ----------
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
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
