package by.it.group510902.ryzhkov.lesson12;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyRbMap implements SortedMap<Integer, String> {
    private boolean RED = true;
    private boolean BLACK = false;

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

    private Node NIL = new Node(null, null, BLACK);
    private Node root = NIL;
    private int size = 0;

    public MyRbMap() {
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = NIL;
        size = 0;
    }

    public boolean containsKey(Integer key) {
        return findNode(key) != NIL;
    }

    public boolean containsValue(String value) {
        return containsInTree(root, value);
    }

    private boolean containsInTree(Node node, String value) {
        if (node == NIL)
            return false;
        if (value == null && node.value == null)
            return true;
        if (value != null && value == node.value)
            return true;
        return containsInTree(node.left, value) || containsInTree(node.right, value);
    }

    public String get(Integer key) {
        Node node = findNode(key);
        return node == NIL ? null : node.value;
    }

    public String put(Integer key, String value) {
        Node t = root;
        if (t == NIL) {
            root = new Node(key, value, BLACK);
            root.left = NIL;
            root.right = NIL;
            root.parent = NIL;
            size++;
            return null;
        }
        Node parent = NIL;
        int c = 0;
        while (t != NIL) {
            parent = t;
            c = key.compareTo(t.key);
            if (c < 0)
                t = t.left;
            else if (c > 0)
                t = t.right;
            else {
                String oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        }
        Node e = new Node(key, value, RED);
        e.left = NIL;
        e.right = NIL;
        e.parent = parent;

        if (c < 0)
            parent.left = e;
        else
            parent.right = e;

        fixAfterInsertion(e);
        size++;
        return null;
    }

    public String remove(Integer key) {
        Node p = findNode(key);
        if (p == NIL)
            return null;

        String oldValue = p.value;
        deleteNode(p);
        return oldValue;
    }

    public Integer firstKey() {
        if (root == NIL)
            return null;
        Node n = root;
        while (n.left != NIL)
            n = n.left;
        return n.key;
    }

    public Integer lastKey() {
        if (root == NIL)
            return null;
        Node n = root;
        while (n.right != NIL)
            n = n.right;
        return n.key;
    }

    public SortedMap<Integer, String> headMap(Integer toKey) {
        TreeMap<Integer, String> subMap = new TreeMap<>();
        fillSubMapStandard(root, subMap, null, toKey);
        return subMap;
    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        TreeMap<Integer, String> subMap = new TreeMap<>();
        fillSubMapStandard(root, subMap, fromKey, null);
        return subMap;
    }

    private void fillSubMapStandard(Node current, TreeMap<Integer, String> target, Integer from, Integer to) {
        if (current == NIL)
            return;
        fillSubMapStandard(current.left, target, from, to);
        boolean tailRight = (from == null || current.key.compareTo(from) >= 0);
        boolean headRight = (to == null || current.key.compareTo(to) < 0);
        if (tailRight && headRight) {
            target.put(current.key, current.value);
        }
        fillSubMapStandard(current.right, target, from, to);
    }

    public String toString() {
        if (root == NIL)
            return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void buildToString(Node node, StringBuilder sb) {
        if (node == NIL)
            return;
        buildToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        buildToString(node.right, sb);
    }

    private Node findNode(Integer key) {
        Node current = root;
        while (current != NIL) {
            int c = key.compareTo(current.key);
            if (c < 0)
                current = current.left;
            else if (c > 0)
                current = current.right;
            else
                return current;
        }
        return NIL;
    }

    private void leftRotate(Node x) {
        if (x == NIL)
            return;
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL)
            y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        if (x == NIL)
            return;
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL)
            y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void fixAfterInsertion(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
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
        size--;
        if (yOriginalColor == BLACK) {
            fixAfterDeletion(x);
        }

    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != NIL) {
            v.parent = u.parent;
        }
    }

    private Node minimum(Node node) {
        while (node.left != NIL)
            node = node.left;
        return node;
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // Заглушки
    public java.util.Comparator<? super Integer> comparator() {
        return null;
    }

    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    public java.util.Set<Integer> keySet() {
        return null;
    }

    public java.util.Collection<String> values() {
        return null;
    }

    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        return null;
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    public boolean containsKey(Object key) {
        return containsKey((Integer) key);
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public String get(Object key) {
        return get((Integer) key);
    }

    public String remove(Object key) {
        return remove((Integer) key);
    }
}
