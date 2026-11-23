package by.it.group510902.slesar.lesson12;


import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red = true;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private Node find(Integer key) {
        Node n = root;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c == 0) return n;
            n = c < 0 ? n.left : n.right;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value, null);
            root.red = false;
            size++;
            return null;
        }

        Node p = null, n = root;
        while (n != null) {
            p = n;
            int c = key.compareTo(n.key);
            if (c == 0) {
                String old = n.value;
                n.value = value;
                return old;
            }
            n = c < 0 ? n.left : n.right;
        }

        Node added = new Node(key, value, p);
        if (key < p.key) p.left = added;
        else p.right = added;
        size++;
        fixInsert(added);
        return null;
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

    private void fixInsert(Node n) {
        while (n != root && n.parent.red) {
            Node p = n.parent;
            Node g = p.parent;
            if (p == g.left) {
                Node u = g.right;
                if (u != null && u.red) {
                    p.red = false;
                    u.red = false;
                    g.red = true;
                    n = g;
                } else {
                    if (n == p.right) {
                        n = p;
                        rotateLeft(n);
                    }
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateRight(n.parent.parent);
                }
            } else {
                Node u = g.left;
                if (u != null && u.red) {
                    p.red = false;
                    u.red = false;
                    g.red = true;
                    n = g;
                } else {
                    if (n == p.left) {
                        n = p;
                        rotateRight(n);
                    }
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateLeft(n.parent.parent);
                }
            }
        }
        root.red = false;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object v) {
        if (n == null) return false;
        return (n.value == null ? v == null : n.value.equals(v))
                || containsValue(n.left, v) || containsValue(n.right, v);
    }

    private Node minimum(Node n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = find((Integer) key);
        if (z == null) return null;

        String old = z.value;
        if (z.left == null) transplant(z, z.right);
        else if (z.right == null) transplant(z, z.left);
        else {
            Node y = minimum(z.right);
            if (y.parent != z) {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.red = z.red;
        }
        size--;
        return old;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private void append(Node n, StringBuilder sb) {
        if (n == null) return;
        append(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        append(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        append(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public Integer firstKey() {
        Node n = minimum(root);
        if (n == null) throw new java.util.NoSuchElementException();
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = root;
        if (n == null) throw new java.util.NoSuchElementException();
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        copyLess(root, toKey, result);
        return result;
    }

    private void copyLess(Node n, Integer key, MyRbMap result) {
        if (n == null) return;
        copyLess(n.left, key, result);
        if (n.key < key) result.put(n.key, n.value);
        copyLess(n.right, key, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        copyGreater(root, fromKey, result);
        return result;
    }

    private void copyGreater(Node n, Integer key, MyRbMap result) {
        if (n == null) return;
        copyGreater(n.left, key, result);
        if (n.key >= key) result.put(n.key, n.value);
        copyGreater(n.right, key, result);
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer from, Integer to) {
        MyRbMap result = new MyRbMap();
        copyRange(root, from, to, result);
        return result;
    }

    private void copyRange(Node n, Integer from, Integer to, MyRbMap result) {
        if (n == null) return;
        copyRange(n.left, from, to, result);
        if (n.key >= from && n.key < to) result.put(n.key, n.value);
        copyRange(n.right, from, to, result);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
}
