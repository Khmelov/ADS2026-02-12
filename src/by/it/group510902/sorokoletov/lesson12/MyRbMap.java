package by.it.group510902.sorokoletov.lesson12;

import java.util.SortedMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.red = true;
        }
    }

    private Node root;
    private int size;



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
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (node.value == null ? value == null : node.value.equals(value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    private Node getNode(Integer key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                String old = x.value;
                x.value = value;
                return old;
            }
        }
        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) root = z;
        else {
            int cmp = key.compareTo(y.key);
            if (cmp < 0) y.left = z;
            else y.right = z;
        }
        z.left = null;
        z.right = null;
        z.red = true;
        size++;
        insertFixup(z);
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.red) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.red = false;
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
    public String remove(Object key) {
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String removedValue = z.value;
        deleteNode(z);
        size--;
        return removedValue;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        Node xParent;
        boolean yOriginalRed = y.red;

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
            yOriginalRed = y.red;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.red = z.red;
        }

        if (!yOriginalRed) {
            deleteFixup(x, xParent);
        }
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

    private void deleteFixup(Node x, Node parent) {
        while (x != root && (x == null || !x.red)) {
            if (x == parent.left) {
                Node w = parent.right;
                if (w != null && w.red) {
                    w.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (w == null || ((w.left == null || !w.left.red) && (w.right == null || !w.right.red))) {
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.right == null || !w.right.red) {
                        if (w.left != null) w.left.red = false;
                        w.red = true;
                        rotateRight(w);
                        w = parent.right;
                    }
                    w.red = parent.red;
                    parent.red = false;
                    if (w.right != null) w.right.red = false;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;
                if (w != null && w.red) {
                    w.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (w == null || ((w.right == null || !w.right.red) && (w.left == null || !w.left.red))) {
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || !w.left.red) {
                        if (w.right != null) w.right.red = false;
                        w.red = true;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.red = parent.red;
                    parent.red = false;
                    if (w.left != null) w.left.red = false;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        if (x != null) x.red = false;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, null, toKey, true);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, fromKey, null, false);
        return map;
    }

    private void addToMap(Node node, MyRbMap map, Integer fromKey, Integer toKey, boolean head) {
        if (node == null) return;
        addToMap(node.left, map, fromKey, toKey, head);
        boolean include = true;
        if (head && toKey != null) {
            if (node.key.compareTo(toKey) >= 0) include = false;
        }
        if (!head && fromKey != null) {
            if (node.key.compareTo(fromKey) < 0) include = false;
        }
        if (include) {
            map.put(node.key, node.value);
        }
        addToMap(node.right, map, fromKey, toKey, head);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2);
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
    public Comparator<? super Integer> comparator() {
        return null;
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
        throw new UnsupportedOperationException();
    }
}