package by.it.group551002.efimchikov.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(int key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private static final Node NIL;

    static {
        NIL = new Node(0, null, BLACK);
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = NIL;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        Node z = new Node(key, value, RED);
        Node y = NIL;
        Node x = root;

        while (x != NIL) {
            y = x;
            if (z.key < x.key) {
                x = x.left;
            } else if (z.key > x.key) {
                x = x.right;
            } else {
                String old = x.value;
                x.value = value;
                return old;
            }
        }

        z.parent = y;
        if (y == NIL) {
            root = z;
        } else if (z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }

        z.left = NIL;
        z.right = NIL;
        z.color = RED;

        fixInsert(z);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        int k = (Integer) key;
        Node z = findNode(k);
        if (z == NIL) {
            return null;
        }

        String removed = z.value;
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

        if (yOriginalColor == BLACK) {
            fixRemove(x);
        }

        size--;
        return removed;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = findNode((Integer) key);
        return node == NIL ? null : node.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != NIL;
    }

    public boolean containsKey(Integer key) {
        return containsKey((Object) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == NIL) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

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

    @Override
    public String toString() {
        return "{" + toString(root) + "}";
    }

    private String toString(Node node) {
        if (node == NIL) {
            return "";
        }
        String left = toString(node.left);
        String right = toString(node.right);
        String current = node.key + "=" + node.value;

        if (left.length() > 0 && right.length() > 0) {
            return left + ", " + current + ", " + right;
        } else if (left.length() > 0) {
            return left + ", " + current;
        } else if (right.length() > 0) {
            return current + ", " + right;
        } else {
            return current;
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException("toKey == null");
        }
        MyRbMap result = new MyRbMap();
        copyHead(root, toKey, result);
        return result;
    }

    private void copyHead(Node node, int toKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }
        if (node.key < toKey) {
            copyHead(node.left, toKey, result);
            result.put(node.key, node.value);
            copyHead(node.right, toKey, result);
        } else {
            copyHead(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey == null");
        }
        MyRbMap result = new MyRbMap();
        copyTail(root, fromKey, result);
        return result;
    }

    private void copyTail(Node node, int fromKey, MyRbMap result) {
        if (node == NIL) {
            return;
        }
        if (node.key >= fromKey) {
            copyTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            copyTail(node.right, fromKey, result);
        } else {
            copyTail(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == NIL) {
            throw new NoSuchElementException("Map is empty");
        }
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) {
            throw new NoSuchElementException("Map is empty");
        }
        return maximum(root).key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // заглушка
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return null;
    }

    private Node findNode(int key) {
        Node node = root;
        while (node != NIL) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return NIL;
    }

    private Node minimum(Node node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != NIL) {
            node = node.right;
        }
        return node;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != NIL) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == NIL) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    private void fixInsert(Node z) {
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
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
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

    private void fixRemove(Node x) {
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
                if (w.left.color == BLACK && w.right.color == BLACK) {
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
}
