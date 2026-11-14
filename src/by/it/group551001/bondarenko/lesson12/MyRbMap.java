package by.it.group551001.bondarenko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;
        int size;

        Node(Integer key, String value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node root;

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    private Node getNode(Node x, Integer key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey((Integer) key);
    }

    public boolean containsKey(Integer key) {
        if (key == null) return false;
        return getNode(root, key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get((Integer) key);
    }

    public String get(Integer key) {
        if (key == null) return null;
        Node x = getNode(root, key);
        return x == null ? null : x.value;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInternal(root, value);
    }

    private boolean containsValueInternal(Node x, Object value) {
        if (x == null) return false;
        if (value == null ? x.value == null : value.equals(x.value)) return true;
        return containsValueInternal(x.left, value) || containsValueInternal(x.right, value);
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED, 1);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null || !containsKey(key)) return null;
        String val = get(key);

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = remove(root, key);
        if (root != null) root.color = BLACK;
        return val;
    }

    private Node remove(Node h, Integer key) {
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = removeMin(h.right);
            } else {
                h.right = remove(h.right, key);
            }
        }
        return balance(h);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node removeMin(Node h) {
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = removeMin(h.left);
        return balance(h);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node x, Integer toKey, MyRbMap sub) {
        if (x == null) return;
        int cmp = toKey.compareTo(x.key);
        if (cmp > 0) {
            fillHeadMap(x.left, toKey, sub);
            sub.put(x.key, x.value);
            fillHeadMap(x.right, toKey, sub);
        } else {
            fillHeadMap(x.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node x, Integer fromKey, MyRbMap sub) {
        if (x == null) return;
        int cmp = fromKey.compareTo(x.key);
        if (cmp <= 0) {
            fillTailMap(x.left, fromKey, sub);
            sub.put(x.key, x.value);
            fillTailMap(x.right, fromKey, sub);
        } else {
            fillTailMap(x.right, fromKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendInOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node x, StringBuilder sb) {
        if (x == null) return;
        appendInOrder(x.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(x.key).append("=").append(x.value);
        appendInOrder(x.right, sb);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
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
