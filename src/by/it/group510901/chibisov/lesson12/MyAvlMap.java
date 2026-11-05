package by.it.group510901.chibisov.lesson12;

import java.util.*;

public class MyAvlMap<K, V> implements Map<K, V> {

    private static class Node {
        Object key;
        Object value;
        Node left;
        Node right;
        int height;

        Node(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<? super K>) first).compareTo((K) second);
    }

    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node rotateRight(Node node) {
        Node pivot = node.left;
        Node orphan = pivot.right;

        pivot.right = node;
        node.left = orphan;

        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private Node rotateLeft(Node node) {
        Node pivot = node.right;
        Node orphan = pivot.left;

        pivot.left = node;
        node.right = orphan;

        updateHeight(node);
        updateHeight(pivot);
        return pivot;
    }

    private Node balance(Node n) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        } else if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        updateHeight(n);
        return n;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) return "{}";
        sb.append('{');
        inOrder(root, sb);
        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
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
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException();
        V oldValue = get(key);
        root = insertNode(root, key, value);
        return oldValue;
    }

    private Node insertNode(Node n, K key, V value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = compare(key, n.key);
        if (cmp < 0) {
            n.left = insertNode(n.left, key, value);
        } else if (cmp > 0) {
            n.right = insertNode(n.right, key, value);
        } else {
            n.value = value;
            return n;
        }
        return balance(n);
    }

    @Override
    public V remove(Object key) {
        if (key == null) throw new NullPointerException();
        V oldValue = get(key);
        root = deleteNode(root, key);
        return oldValue;
    }

    private Node deleteNode(Node n, Object key) {
        if (n == null) return null;
        int cmp = compare(key, n.key);
        if (cmp < 0) {
            n.left = deleteNode(n.left, key);
        } else if (cmp > 0) {
            n.right = deleteNode(n.right, key);
        } else {
            if (n.left == null) {
                size--;
                return n.right;
            }
            if (n.right == null) {
                size--;
                return n.left;
            }
            Node successor = n.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            n.key = successor.key;
            n.value = successor.value;
            n.right = deleteNode(n.right, successor.key);
        }
        return balance(n);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return (V) current.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
