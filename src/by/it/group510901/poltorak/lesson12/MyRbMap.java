package by.it.group510901.poltorak.lesson12;

import java.util.*;

import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color = RED;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root = null;
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Ключ не может быть null");

        if (root == null) {
            root = new Node(key, value, null);
            root.color = BLACK;
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
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

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    private void fixAfterInsertion(Node x) {
        while (x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node z = root;
        while (z != null) {
            int cmp = key.compareTo(z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else break;
        }
        if (z == null) return null;
        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private void deleteNode(Node z) {
        Node y;
        Node x;

        if (z.left == null || z.right == null) {
            y = z;
        } else {
            y = z.right;
            while (y.left != null) y = y.left;
        }

        if (y.left != null) {
            x = y.left;
        } else {
            x = y.right;
        }

        if (x != null) {
            x.parent = y.parent;
        }

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        if (y != z) {
            z.key = y.key;
            z.value = y.value;
        }

        if (y.color == BLACK) {
            fixAfterDeletion(x, y.parent);
        }
    }

    private void fixAfterDeletion(Node x, Node parent) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == parent.left) {
                Node sib = parent.right;
                if (sib != null && sib.color == RED) {
                    sib.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    sib = parent.right;
                }
                if ((sib == null || sib.left == null || sib.left.color == BLACK) &&
                        (sib == null || sib.right == null || sib.right.color == BLACK)) {
                    if (sib != null) sib.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (sib == null || sib.right == null || sib.right.color == BLACK) {
                        if (sib != null && sib.left != null) sib.left.color = BLACK;
                        if (sib != null) sib.color = RED;
                        rotateRight(sib);
                        sib = parent.right;
                    }
                    if (sib != null) {
                        sib.color = parent.color;
                        if (sib.right != null) sib.right.color = BLACK;
                    }
                    parent.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                Node sib = parent.left;
                if (sib != null && sib.color == RED) {
                    sib.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    sib = parent.left;
                }
                if ((sib == null || sib.right == null || sib.right.color == BLACK) &&
                        (sib == null || sib.left == null || sib.left.color == BLACK)) {
                    if (sib != null) sib.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (sib == null || sib.left == null || sib.left.color == BLACK) {
                        if (sib != null && sib.right != null) sib.right.color = BLACK;
                        if (sib != null) sib.color = RED;
                        rotateLeft(sib);
                        sib = parent.left;
                    }
                    if (sib != null) {
                        sib.color = parent.color;
                        if (sib.left != null) sib.left.color = BLACK;
                    }
                    parent.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) current = current.left;
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) current = current.right;
        return current.key;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            sub.put(node.key, node.value);
            fillHeadMap(node.left, toKey, sub);
            fillHeadMap(node.right, toKey, sub);
        } else {
            fillHeadMap(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            sub.put(node.key, node.value);
            fillTailMap(node.left, fromKey, sub);
            fillTailMap(node.right, fromKey, sub);
        } else {
            fillTailMap(node.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendToString(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void appendToString(Node node, StringBuilder sb) {
        if (node == null) return;
        appendToString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendToString(node.right, sb);
    }
}
