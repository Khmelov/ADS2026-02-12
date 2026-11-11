package by.it.group551002.savitsky.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        boolean color = RED;
        Node left;
        Node right;
        Node parent;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private boolean isBlack(Node node) {
        return !isRed(node);
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

    private void rotateRight(Node y) {
        Node x = y.left;

        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }

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

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        Node existing = findNode(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }

        Node parent = null;
        Node cur = root;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (parent == null) {
            root = newNode;
        } else if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        fixInsert(newNode);

        return null;
    }

    private void fixInsert(Node node) {
        while (node != root && isRed(node.parent)) {
            Node parent = node.parent;
            Node grandad = parent.parent;

            if (parent == grandad.left) {
                Node uncle = grandad.right;

                if (isRed(uncle)) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandad.color = RED;
                    node = grandad;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.parent;
                    }

                    parent.color = BLACK;
                    grandad.color = RED;
                    rotateRight(grandad);
                }
            } else {
                Node uncle = grandad.left;

                if (isRed(uncle)) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandad.color = RED;
                    node = grandad;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                        parent = node.parent;
                    }

                    parent.color = BLACK;
                    grandad.color = RED;
                    rotateLeft(grandad);
                }
            }
        }

        root.color = BLACK;
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

    private Node minNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalColor = y.color;
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
            y = minNode(z.right);
            yOriginalColor = y.color;
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
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            fixDelete(x, xParent);
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        if (node == null) {
            return null;
        }

        String old = node.value;
        deleteNode(node);
        size--;

        return old;
    }

    private void fixDelete(Node node, Node nodeParent) {
        while (node != root && isBlack(node)) {
            if (node == nodeParent.left) {
                Node brother = nodeParent.right;

                if (isRed(brother)) {
                    brother.color = BLACK;
                    nodeParent.color = RED;
                    rotateLeft(nodeParent);
                    brother = nodeParent.right;
                }

                if (isBlack(brother.left) && isBlack(brother.right)) {
                    brother.color = RED;
                    node = nodeParent;
                    nodeParent = node.parent;
                } else {
                    if (isBlack(brother.right)) {
                        brother.left.color = BLACK;
                        brother.color = RED;
                        rotateRight(brother);
                        brother = nodeParent.right;
                    }

                    brother.color = nodeParent.color;
                    nodeParent.color = BLACK;
                    brother.right.color = BLACK;
                    rotateLeft(nodeParent);
                    node = root;
                }
            } else {
                Node brother = nodeParent.left;

                if (isRed(brother)) {

                    brother.color = BLACK;
                    nodeParent.color = RED;
                    rotateRight(nodeParent);
                    brother = nodeParent.left;
                }

                if (isBlack(brother.left) && isBlack(brother.right)) {

                    brother.color = RED;
                    node = nodeParent;
                    nodeParent = node.parent;
                } else {
                    if (isBlack(brother.left)) {

                        brother.right.color = BLACK;
                        brother.color = RED;
                        rotateLeft(brother);
                        brother = nodeParent.left;
                    }

                    brother.color = nodeParent.color;
                    nodeParent.color = BLACK;
                    brother.left.color = BLACK;
                    rotateRight(nodeParent);
                    node = root;
                }
            }
        }

        if (node != null) {
            node.color = BLACK;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null) {
            if (node.value == null) {
                return true;
            }
        } else {
            if (value.equals(node.value)) {
                return true;
            }
        }

        if (containsValue(node.left, value)) {
            return true;
        }

        return containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        return node == null ? null : node.value;
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
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        return minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node node = root;
        while (node.right != null) {
            node = node.right;
        }

        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);

        return result;
    }

    private void collectHead(Node node, int toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        collectHead(node.left, toKey, result);
        if (node.key < toKey) {
            result.put(node.key, node.value);
            collectHead(node.right, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);

        return result;
    }

    private void collectTail(Node node, int fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        if (node.key >= fromKey) {
            collectTail(node.left, fromKey, result);
            result.put(node.key, node.value);
        }
        collectTail(node.right, fromKey, result);
    }

    @Override
    public String toString() {
        return "{" + inOrder(root) + "}";
    }

    private String inOrder(Node node) {
        if (node == null) {
            return "";
        }

        String left = inOrder(node.left);
        String right = inOrder(node.right);

        String result = node.key + "=" + node.value;
        if (!left.isEmpty()) {
            result = left + ", " + result;
        }
        if (!right.isEmpty()) {
            result = result + ", " + right;
        }

        return result;
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
}
