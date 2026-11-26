package by.it.group551003.kovalchuk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private static class Node {
        Integer key;
        String value;
        boolean color;

        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            color = RED;
        }
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;

        x.right = y.left;

        if (y.left != null)
            y.left.parent = x;

        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;

        x.left = y.right;

        if (y.right != null)
            y.right.parent = x;

        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;

        y.right = x;
        x.parent = y;
    }

    private void fixAfterInsert(Node node) {

        while (node.parent != null && node.parent.color == RED) {

            if (node.parent == node.parent.parent.left) {

                Node uncle = node.parent.parent.right;

                if (isRed(uncle)) {

                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;

                } else {

                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }

                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {

                Node uncle = node.parent.parent.left;

                if (isRed(uncle)) {

                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;

                } else {

                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }

                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }

        root.color = BLACK;
    }

    private Node findNode(Integer key) {

        Node current = root;

        while (current != null) {

            int cmp = key.compareTo(current.key);

            if (cmp < 0)
                current = current.left;
            else if (cmp > 0)
                current = current.right;
            else
                return current;
        }

        return null;
    }

    @Override
    public String put(Integer key, String value) {

        if (root == null) {

            root = new Node(key, value);
            root.color = BLACK;
            size++;

            return null;
        }

        Node parent = null;
        Node current = root;

        while (current != null) {

            parent = current;

            int cmp = key.compareTo(current.key);

            if (cmp < 0)
                current = current.left;
            else if (cmp > 0)
                current = current.right;
            else {

                String old = current.value;
                current.value = value;
                return old;
            }
        }

        Node node = new Node(key, value);
        node.parent = parent;

        if (key < parent.key)
            parent.left = node;
        else
            parent.right = node;

        fixAfterInsert(node);

        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    private boolean containsValue(Node node, Object value) {

        if (node == null)
            return false;

        if (value == null) {
            if (node.value == null)
                return true;
        } else {
            if (value.equals(node.value))
                return true;
        }

        return containsValue(node.left, value)
                || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private Node remove(Node node, Integer key) {

        if (node == null)
            return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0)
            node.left = remove(node.left, key);

        else if (cmp > 0)
            node.right = remove(node.right, key);

        else {

            if (node.left == null)
                return node.right;

            if (node.right == null)
                return node.left;

            Node min = node.right;

            while (min.left != null)
                min = min.left;

            node.key = min.key;
            node.value = min.value;

            node.right = remove(node.right, min.key);
        }

        return node;
    }

    @Override
    public String remove(Object key) {

        Node node = findNode((Integer) key);

        if (node == null)
            return null;

        String old = node.value;

        root = remove(root, (Integer) key);
        size--;

        return old;
    }

    private void inorder(Node node, StringBuilder sb) {

        if (node == null)
            return;

        inorder(node.left, sb);

        if (sb.length() > 1)
            sb.append(", ");

        sb.append(node.key)
                .append("=")
                .append(node.value);

        inorder(node.right, sb);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("{");

        inorder(root, sb);

        sb.append("}");

        return sb.toString();
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

    private void fillHead(Node node, Integer key, MyRbMap map) {

        if (node == null)
            return;

        fillHead(node.left, key, map);

        if (node.key < key)
            map.put(node.key, node.value);

        fillHead(node.right, key, map);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {

        MyRbMap map = new MyRbMap();

        fillHead(root, toKey, map);

        return map;
    }

    private void fillTail(Node node, Integer key, MyRbMap map) {

        if (node == null)
            return;

        fillTail(node.left, key, map);

        if (node.key >= key)
            map.put(node.key, node.value);

        fillTail(node.right, key, map);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {

        MyRbMap map = new MyRbMap();

        fillTail(root, fromKey, map);

        return map;
    }

    @Override
    public Integer firstKey() {

        if (root == null)
            return null;

        Node cur = root;

        while (cur.left != null)
            cur = cur.left;

        return cur.key;
    }

    @Override
    public Integer lastKey() {

        if (root == null)
            return null;

        Node cur = root;

        while (cur.right != null)
            cur = cur.right;

        return cur.key;
    }

    // Остальные методы интерфейса

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
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}