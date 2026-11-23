package by.it.group551003.yakushev.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * Красно-чёрное дерево (левостороннее, LLRB).
 */
@SuppressWarnings("unchecked")
public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        boolean red = true;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private String oldValue;
    private boolean found;

    private static boolean isRed(Node n) {
        return n != null && n.red;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private void flip(Node h) {
        h.red = !h.red;
        h.left.red = !h.left.red;
        h.right.red = !h.right.red;
    }

    private Node fixUp(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flip(h);
        return h;
    }

    private Node moveRedLeft(Node h) {
        flip(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flip(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flip(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flip(h);
        }
        return h;
    }

    private Node insert(Node h, int key, String value) {
        if (h == null) {
            size++;
            found = false;
            return new Node(key, value);
        }
        if (key < h.key) h.left = insert(h.left, key, value);
        else if (key > h.key) h.right = insert(h.right, key, value);
        else {
            found = true;
            oldValue = h.value;
            h.value = value;
        }
        return fixUp(h);
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return fixUp(h);
    }

    private Node delete(Node h, int key) {
        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key == h.key && h.right == null) return null;
            if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
            if (key == h.key) {
                Node m = h.right;
                while (m.left != null) m = m.left;
                h.key = m.key;
                h.value = m.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return fixUp(h);
    }

    private Node findNode(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node n = root;
        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        appendInOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    private boolean hasValue(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    /** Копирует в dst элементы с ключами из [lo, hi); границы могут отсутствовать. */
    private void copyRange(Node n, MyRbMap dst, boolean hasLo, int lo, boolean hasHi, int hi) {
        if (n == null) return;
        if (!hasLo || n.key > lo) copyRange(n.left, dst, hasLo, lo, hasHi, hi);
        if ((!hasLo || n.key >= lo) && (!hasHi || n.key < hi)) dst.put(n.key, n.value);
        if (!hasHi || n.key < hi) copyRange(n.right, dst, hasLo, lo, hasHi, hi);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        found = false;
        oldValue = null;
        root = insert(root, key, value);
        root.red = false;
        return found ? oldValue : null;
    }

    @Override
    public String remove(Object key) {
        Node n = findNode(key);
        if (n == null) return null;
        String old = n.value;
        int k = (Integer) key;
        if (!isRed(root.left) && !isRed(root.right)) root.red = true;
        root = delete(root, k);
        if (root != null) root.red = false;
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        Node n = findNode(key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap res = new MyRbMap();
        copyRange(root, res, false, 0, true, toKey);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap res = new MyRbMap();
        copyRange(root, res, true, fromKey, false, 0);
        return res;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey > toKey) throw new IllegalArgumentException("fromKey > toKey");
        MyRbMap res = new MyRbMap();
        copyRange(root, res, true, fromKey, true, toKey);
        return res;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
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