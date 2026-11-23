package by.it.group551001.romanovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final class Node {
        int key;
        String value;
        boolean red = true;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private static boolean isRed(Node node) {
        return node != null && node.red;
    }

    private static Node rotateLeft(Node node) {
        Node top = node.right;
        node.right = top.left;
        top.left = node;
        top.red = node.red;
        node.red = true;
        return top;
    }

    private static Node rotateRight(Node node) {
        Node top = node.left;
        node.left = top.right;
        top.right = node;
        top.red = node.red;
        node.red = true;
        return top;
    }

    private static void flipColors(Node node) {
        node.red = !node.red;
        node.left.red = !node.left.red;
        node.right.red = !node.right.red;
    }

    private static Node balance(Node node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    private static Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private static Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node find(int key) {
        Node node = root;
        while (node != null && node.key != key) {
            node = key < node.key ? node.left : node.right;
        }
        return node;
    }

    private static int key(Object key) {
        return (Integer) Objects.requireNonNull(key);
    }

    @Override
    public String put(Integer key, String value) {
        int k = key(key);
        Node existing = find(k);
        if (existing != null) {
            String previous = existing.value;
            existing.value = value;
            return previous;
        }
        root = insert(root, k, value);
        root.red = false;
        size++;
        return null;
    }

    private static Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else {
            node.right = insert(node.right, key, value);
        }
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    @Override
    public String remove(Object key) {
        int k = key(key);
        Node node = find(k);
        if (node == null) {
            return null;
        }
        String previous = node.value;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.red = true;
        }
        root = delete(root, k);
        if (root != null) {
            root.red = false;
        }
        size--;
        return previous;
    }

    private static Node delete(Node node, int key) {
        if (key < node.key) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key == node.key && node.right == null) {
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key == node.key) {
                Node successor = minimum(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = deleteMinimum(node.right);
            } else {
                node.right = delete(node.right, key);
            }
        }
        return balance(node);
    }

    private static Node deleteMinimum(Node node) {
        if (node.left == null) {
            return null;
        }
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMinimum(node.left);
        return balance(node);
    }

    private static Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key) {
        Node node = find(key(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private static boolean containsValue(Node node, Object value) {
        return node != null && (Objects.equals(node.value, value)
                || containsValue(node.left, value) || containsValue(node.right, value));
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
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return range(null, key(toKey));
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(key(fromKey), null);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        int from = key(fromKey);
        int to = key(toKey);
        if (from > to) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        return range(from, to);
    }

    private MyRbMap range(Integer from, Integer to) {
        MyRbMap result = new MyRbMap();
        copyRange(root, result, from, to);
        return result;
    }

    private static void copyRange(Node node, MyRbMap target, Integer from, Integer to) {
        if (node == null) {
            return;
        }
        if (from == null || node.key > from) {
            copyRange(node.left, target, from, to);
        }
        if ((from == null || node.key >= from) && (to == null || node.key < to)) {
            target.put(node.key, node.value);
        }
        if (to == null || node.key < to) {
            copyRange(node.right, target, from, to);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        append(root, result);
        return result.append('}').toString();
    }

    private static void append(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }
        append(node.left, result);
        if (result.length() > 1) {
            result.append(", ");
        }
        result.append(node.key).append('=').append(node.value);
        append(node.right, result);
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
