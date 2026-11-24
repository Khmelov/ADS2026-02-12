package by.it.group510902.zenkovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    static final boolean RED = true;
    static final boolean BLACK = false;

    static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private int size;
    private final Comparator<? super Integer> comparator;

    public MyRbMap() {
        this.root = null;
        this.size = 0;
        this.comparator = null;
    }

    public MyRbMap(Comparator<? super Integer> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    private int compare(Integer k1, Integer k2) {
        return comparator == null ? k1.compareTo(k2) : comparator.compare(k1, k2);
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void fixInsert(Node k) {
        while (k.parent != null && k.parent.color == RED) {
            if (k.parent == k.parent.parent.right) {
                Node u = k.parent.parent.left;
                if (u != null && u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rotateRight(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rotateLeft(k.parent.parent);
                }
            } else {
                Node u = k.parent.parent.right;
                if (u != null && u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rotateRight(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        root.color = BLACK;
    }



    private Node search(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current;
        }
        return null;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(node.key).append("=").append(node.value);
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = compare(key, x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }
        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) root = z;
        else if (compare(key, y.key) < 0) y.left = z;
        else y.right = z;
        size++;
        fixInsert(z);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = search((Integer) key);
        if (z == null) return null;
        String oldValue = z.value;
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;
        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                if (x != null) x.parent = y;
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
        if (yOriginalColor == BLACK && x != null) {
            fixDelete(x);
        } else if (yOriginalColor == BLACK && x == null && root != null) {
        }
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = search((Integer) key);
        return node == null ? null : node.value;
    }

    private void fixDelete(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node s = x.parent.right;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    s = x.parent.right;
                }
                if ((s.left == null || s.left.color == BLACK) && (s.right == null || s.right.color == BLACK)) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.right == null || s.right.color == BLACK) {
                        if (s.left != null) s.left.color = BLACK;
                        s.color = RED;
                        rotateRight(s);
                        s = x.parent.right;
                    }
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (s.right != null) s.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node s = x.parent.left;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    s = x.parent.left;
                }
                if ((s.right == null || s.right.color == BLACK) && (s.left == null || s.left.color == BLACK)) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.left == null || s.left.color == BLACK) {
                        if (s.right != null) s.right.color = BLACK;
                        s.color = RED;
                        rotateLeft(s);
                        s = x.parent.left;
                    }
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (s.left != null) s.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap(comparator);
        headMap(root, toKey, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, MyRbMap map) {
        if (node == null) return;
        int cmp = compare(node.key, toKey);
        if (cmp < 0) {
            headMap(node.left, toKey, map);
            map.put(node.key, node.value);
            headMap(node.right, toKey, map);
        } else {
            headMap(node.left, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap(comparator);
        tailMap(root, fromKey, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node == null) return;
        int cmp = compare(node.key, fromKey);
        if (cmp >= 0) {
            tailMap(node.left, fromKey, map);
            map.put(node.key, node.value);
            tailMap(node.right, fromKey, map);
        } else {
            tailMap(node.right, fromKey, map);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return maximum(root).key;
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
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
}