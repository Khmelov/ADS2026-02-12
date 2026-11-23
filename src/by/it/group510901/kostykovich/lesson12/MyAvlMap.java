package by.it.group510901.kostykovich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Node leftChild;
        Node rightChild;
        String value;
        int key;
        int height;

        public Node(String value, int key) {
            this.value = value;
            this.key = key;
            this.height = 1;
        }
    }

    private int currentSize;
    private Node root;

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        String res = inOrderToString(root);
        return "{" + res.substring(0, res.length() - 2) + "}";
    }

    private String inOrderToString(Node node) {
        if (node == null) {
            return "";
        }
        return inOrderToString(node.leftChild)
                + node.key + "=" + node.value + ", "
                + inOrderToString(node.rightChild);
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node existing = search(this.root, key);
        String prevValue = (existing != null) ? existing.value : null;

        this.root = insert(this.root, key, value);
        if (existing == null) {
            currentSize++;
        }
        return prevValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;
        Node existing = search(this.root, intKey);
        if (existing == null) {
            return null;
        }
        String removedValue = existing.value;
        this.root = delete(this.root, intKey);
        currentSize--;
        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node res = search(this.root, (Integer) key);
        return (res != null) ? res.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return search(this.root, (Integer) key) != null;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    private Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(value, key);
        }
        if (key < node.key) {
            node.leftChild = insert(node.leftChild, key, value);
        } else if (key > node.key) {
            node.rightChild = insert(node.rightChild, key, value);
        } else {
            node.value = value;
            return node;
        }
        return balance(node);
    }

    private Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.leftChild = delete(node.leftChild, key);
        } else if (key > node.key) {
            node.rightChild = delete(node.rightChild, key);
        } else {
            if (node.leftChild == null || node.rightChild == null) {
                node = (node.leftChild != null) ? node.leftChild : node.rightChild;
            } else {
                Node minRight = findMin(node.rightChild);
                node.key = minRight.key;
                node.value = minRight.value;
                node.rightChild = delete(node.rightChild, minRight.key);
            }
        }
        if (node == null) {
            return null;
        }
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    private Node search(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (node.key == key) {
            return node;
        }
        if (key < node.key) {
            return search(node.leftChild, key);
        } else {
            return search(node.rightChild, key);
        }
    }

    private Node balance(Node node) {
        updateHeight(node);
        int fact = getBalance(node);

        if (fact > 1) {
            if (getBalance(node.rightChild) < 0) {
                node.rightChild = rotateRight(node.rightChild);
            }
            return rotateLeft(node);
        }
        if (fact < -1) {
            if (getBalance(node.leftChild) > 0) {
                node.leftChild = rotateLeft(node.leftChild);
            }
            return rotateRight(node);
        }
        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.leftChild;
        Node T2 = x.rightChild;

        x.rightChild = y;
        y.leftChild = T2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.rightChild;
        Node T2 = y.leftChild;

        y.leftChild = x;
        x.rightChild = T2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.leftChild), height(node.rightChild));
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.rightChild) - height(node.leftChild);
    }

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    @Override
    public boolean containsValue(Object value) {
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
