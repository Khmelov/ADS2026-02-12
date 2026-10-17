package by.it.group551002.efimchikov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer,String>{
    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    private String oldValue;
    private String removedValue;

    private String buffer;

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        oldValue = null;
        root = put(root, key.intValue(), value);
        return oldValue;
    }

    private Node put(Node node, int key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value);
        } else if (key > node.key) {
            node.right = put(node.right, key, value);
        } else {
            oldValue = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = find(root, ((Integer) key).intValue());
        return node == null ? null : node.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    private Node find(Node node, int key) {
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        return find(root, ((Integer) key).intValue()) != null;
    }

    public boolean containsKey(Integer key) {
        return containsKey((Object) key);
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        removedValue = null;
        root = remove(root, ((Integer) key).intValue());
        return removedValue;
    }

    public String remove(Integer key) {
        return remove((Object) key);
    }

    private Node remove(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else {
            removedValue = node.value;

            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }

            Node min = min(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = removeMin(node.right);
        }

        return balance(node);
    }

    private Node min(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            size--;
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
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
    public String toString() {
        buffer = "";
        appendInOrder(root);
        String result = "{" + buffer + "}";
        buffer = null;
        return result;
    }

    private void appendInOrder(Node node) {
        if (node == null) {
            return;
        }

        appendInOrder(node.left);

        if (buffer.length() > 0) {
            buffer += ", ";
        }
        buffer += node.key + "=" + node.value;

        appendInOrder(node.right);
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + max(height(node.left), height(node.right));
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m){}

    @Override
    public Set<Integer> keySet() {return null;}

    @Override
    public Collection<String> values(){return null;}

    public Set<Map.Entry<Integer, String>> entrySet(){return null;}
}
