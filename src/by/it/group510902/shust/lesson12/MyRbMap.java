package by.it.group510902.shust.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        int key;
        String value;
        boolean color;
        Node left, right;
        Node(int key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size = 0;

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private Node put(Node h, int key, String value) {
        if (h == null) return new Node(key, value, RED);

        if (key < h.key) h.left = put(h.left, key, value);
        else if (key > h.key) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        if (old == null) size++;
        root = put(root, key, value);
        root.color = BLACK;
        return old;
    }

    private Node remove(Node x, int key) {
        if (x == null) return null;
        if (key < x.key) x.left = remove(x.left, key);
        else if (key > x.key) x.right = remove(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    @Override
    public String remove(Object key) {
        int k = (Integer) key;
        String old = get(k);
        if (old != null) {
            root = remove(root, k);
            size--;
        }
        return old;
    }

    @Override
    public String get(Object key) {
        Node x = root;
        int k = (Integer) key;
        while (x != null) {
            if (k < x.key) x = x.left;
            else if (k > x.key) x = x.right;
            else return x.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node x, Object value) {
        if (x == null) return false;
        if (value == null ? x.value == null : value.equals(x.value)) return true;
        return containsValue(x.left, value) || containsValue(x.right, value);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n != null) {
            inOrder(n.left, sb);
            sb.append(n.key).append("=").append(n.value).append(", ");
            inOrder(n.right, sb);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node n, int toKey, MyRbMap map) {
        if (n == null) return;
        if (n.key < toKey) {
            fillHeadMap(n.left, toKey, map);
            map.put(n.key, n.value);
            fillHeadMap(n.right, toKey, map);
        } else {
            fillHeadMap(n.left, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap subMap = new MyRbMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node n, int fromKey, MyRbMap map) {
        if (n == null) return;
        if (n.key >= fromKey) {
            fillTailMap(n.left, fromKey, map);
            map.put(n.key, n.value);
            fillTailMap(n.right, fromKey, map);
        } else {
            fillTailMap(n.right, fromKey, map);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    // --- Обязательные заглушки интерфейса SortedMap/Map ---
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}