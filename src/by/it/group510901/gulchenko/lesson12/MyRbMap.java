package by.it.group510901.gulchenko.lesson12;

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
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size = 0;

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        newRoot.color = node.color;
        node.color = RED;

        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        newRoot.color = node.color;
        node.color = RED;

        return newRoot;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private Node balance(Node node) {
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

    private Node moveRedLeft(Node node) {
        flipColors(node);

        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }

        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);

        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }

        return node;
    }

    private Node findNode(Integer key) {
        Node current = root;

        while (current != null) {
            int comparison = key.compareTo(current.key);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return current;
            }
        }

        return null;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value, RED);
        }

        int comparison = key.compareTo(node.key);

        if (comparison < 0) {
            node.left = insert(node.left, key, value);
        } else if (comparison > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
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

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return null;
        }

        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node delete(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }

            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            if (key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }

            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }

            if (key.compareTo(node.key) == 0) {
                Node successor = findMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = delete(node.right, key);
            }
        }

        return balance(node);
    }

    private boolean findValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return findValue(node.left, value) || findValue(node.right, value);
    }

    private String appendInOrder(Node node, String result) {
        if (node == null) {
            return result;
        }

        result = appendInOrder(node.left, result);

        if (result.length() > 1) {
            result += ", ";
        }

        result += node.key + "=" + node.value;

        return appendInOrder(node.right, result);
    }

    private void copyHead(Node node, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        copyHead(node.left, toKey, result);

        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            copyHead(node.right, toKey, result);
        }
    }

    private void copyTail(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            copyTail(node.left, fromKey, result);
            result.put(node.key, node.value);
        }

        copyTail(node.right, fromKey, result);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return appendInOrder(root, "{") + "}";
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        Node existing = findNode(key);

        if (existing != null) {
            String oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }

        root = insert(root, key, value);
        root.color = BLACK;
        size++;

        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer integerKey = (Integer) key;
        Node existing = findNode(integerKey);

        if (existing == null) {
            return null;
        }

        String oldValue = existing.value;

        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = delete(root, integerKey);

        if (root != null) {
            root.color = BLACK;
        }

        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        return key instanceof Integer && findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return findValue(root, value);
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyHead(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }

        MyRbMap result = new MyRbMap();
        copyTail(root, fromKey, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node current = root;

        while (current.right != null) {
            current = current.right;
        }

        return current.key;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(
            Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}