package by.it.group551002.sevkovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean red;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.red = true;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        return node != null && node.red;
    }

    private Node findNode(Integer key) {
        if (key == null) {
            return null;
        }

        Node current = root;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null;
    }

    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
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

    private void fixInsert(Node z) {
        while (z.parent != null && z.parent.red) {
            Node parent = z.parent;
            Node grand = parent.parent;

            if (grand == null) {
                break;
            }

            if (parent == grand.left) {
                Node uncle = grand.right;

                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;
                    z = grand;
                } else {
                    if (z == parent.right) {
                        z = parent;
                        rotateLeft(z);
                        parent = z.parent;
                        grand = parent.parent;
                    }

                    parent.red = false;
                    grand.red = true;
                    rotateRight(grand);
                }
            } else {
                Node uncle = grand.left;

                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grand.red = true;
                    z = grand;
                } else {
                    if (z == parent.left) {
                        z = parent;
                        rotateRight(z);
                        parent = z.parent;
                        grand = parent.parent;
                    }

                    parent.red = false;
                    grand.red = true;
                    rotateLeft(grand);
                }
            }
        }

        root.red = false;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }

        if (v != null) {
            v.parent = u.parent;
        }
    }

    private void fixDelete(Node x, Node parent) {
        while (x != root && !isRed(x)) {
            if (parent == null) {
                break;
            }

            if (x == parent.left) {
                Node w = parent.right;

                if (isRed(w)) {
                    w.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    w = parent.right;
                }

                if (!isRed(w != null ? w.left : null)
                        && !isRed(w != null ? w.right : null)) {
                    if (w != null) {
                        w.red = true;
                    }
                    x = parent;
                    parent = x.parent;
                } else {
                    if (!isRed(w != null ? w.right : null)) {
                        if (w != null && w.left != null) {
                            w.left.red = false;
                        }
                        if (w != null) {
                            w.red = true;
                            rotateRight(w);
                        }
                        w = parent.right;
                    }

                    if (w != null) {
                        w.red = parent.red;
                        if (w.right != null) {
                            w.right.red = false;
                        }
                    }

                    parent.red = false;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;

                if (isRed(w)) {
                    w.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    w = parent.left;
                }

                if (!isRed(w != null ? w.left : null)
                        && !isRed(w != null ? w.right : null)) {
                    if (w != null) {
                        w.red = true;
                    }
                    x = parent;
                    parent = x.parent;
                } else {
                    if (!isRed(w != null ? w.left : null)) {
                        if (w != null && w.right != null) {
                            w.right.red = false;
                        }
                        if (w != null) {
                            w.red = true;
                            rotateLeft(w);
                        }
                        w = parent.left;
                    }

                    if (w != null) {
                        w.red = parent.red;
                        if (w.left != null) {
                            w.left.red = false;
                        }
                    }

                    parent.red = false;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }

        if (x != null) {
            x.red = false;
        }
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalRed = y.red;
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
            yOriginalRed = y.red;
            x = y.right;

            if (y.parent == z) {
                xParent = y;
                if (x != null) {
                    x.parent = y;
                }
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
            fixDelete(x, xParent);
        }

        size--;
    }

    private String removeKey(Integer key) {
        Node node = findNode(key);

        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        deleteNode(node);
        return oldValue;
    }

    private void copyHead(Node node, int toKey, MyRbMap target) {
        if (node == null) {
            return;
        }

        if (node.key.intValue() < toKey) {
            copyHead(node.left, toKey, target);
            target.put(node.key, node.value);
            copyHead(node.right, toKey, target);
        } else {
            copyHead(node.left, toKey, target);
        }
    }

    private void copyTail(Node node, int fromKey, MyRbMap target) {
        if (node == null) {
            return;
        }

        if (node.key.intValue() < fromKey) {
            copyTail(node.right, fromKey, target);
        } else {
            copyTail(node.left, fromKey, target);
            target.put(node.key, node.value);
            copyTail(node.right, fromKey, target);
        }
    }

    private void copySub(Node node, int fromKey, int toKey, MyRbMap target) {
        if (node == null) {
            return;
        }

        int key = node.key.intValue();

        if (key < fromKey) {
            copySub(node.right, fromKey, toKey, target);
        } else if (key >= toKey) {
            copySub(node.left, fromKey, toKey, target);
        } else {
            copySub(node.left, fromKey, toKey, target);
            target.put(node.key, node.value);
            copySub(node.right, fromKey, toKey, target);
        }
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private String toString(Node node) {
        if (node == null) {
            return "";
        }

        String left = toString(node.left);
        String right = toString(node.right);
        String current = node.key + "=" + node.value;

        if (left.length() == 0 && right.length() == 0) {
            return current;
        }

        if (left.length() == 0) {
            return current + ", " + right;
        }

        if (right.length() == 0) {
            return left + ", " + current;
        }

        return left + ", " + current + ", " + right;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        if (root == null) {
            root = new Node(key, value);
            root.red = false;
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp == 0) {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node node = new Node(key, value);
        node.parent = parent;

        if (key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size++;
        fixInsert(node);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        return node != null ? node.value : null;
    }

    public String get(Integer key) {
        Node node = findNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        return removeKey((Integer) key);
    }

    public String remove(Integer key) {
        return removeKey(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode((Integer) key) != null;
    }

    public boolean containsKey(Integer key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    public boolean containsValue(String value) {
        return containsValue((Object) value);
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
        root = null;
        size = 0;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }

        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException("fromKey > toKey");
        }

        MyRbMap result = new MyRbMap();
        copySub(root, fromKey.intValue(), toKey.intValue(), result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyHead(root, toKey.intValue(), result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyTail(root, fromKey.intValue(), result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        return maximum(root).key;
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }

    @Override
    public String toString() {
        return "{" + toString(root) + "}";
    }
}