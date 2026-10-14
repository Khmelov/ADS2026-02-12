package by.it.group551004.podvitelskiymichael.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        return containsValueRecursive(n.left, value) || containsValueRecursive(n.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node curr = root;
        while (curr != null) {
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.parent = h.parent;
        if (h.parent == null) root = x;
        else if (h == h.parent.left) h.parent.left = x;
        else h.parent.right = x;
        x.left = h;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.parent = h.parent;
        if (h.parent == null) root = x;
        else if (h == h.parent.left) h.parent.left = x;
        else h.parent.right = x;
        x.right = h;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        String old = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        if (old == null) size++;
        return old;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        if (!containsKey(k)) return null;
        String val = get(k);
        root = remove(root, k);
        size--;
        if (root != null) root.color = BLACK;
        return val;
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
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node removeMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = removeMin(h.left);
        return balance(h);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null)) return null;
            if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
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

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node n, Integer toKey, MyRbMap sub) {
        if (n == null) return;
        if (n.key.compareTo(toKey) < 0) {
            fillHeadMap(n.left, toKey, sub);
            sub.put(n.key, n.value);
            fillHeadMap(n.right, toKey, sub);
        } else {
            fillHeadMap(n.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node n, Integer fromKey, MyRbMap sub) {
        if (n == null) return;
        if (n.key.compareTo(fromKey) >= 0) {
            fillTailMap(n.left, fromKey, sub);
            sub.put(n.key, n.value);
            fillTailMap(n.right, fromKey, sub);
        } else {
            fillTailMap(n.right, fromKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillSubMap(root, fromKey, toKey, sub);
        return sub;
    }

    private void fillSubMap(Node n, Integer fromKey, Integer toKey, MyRbMap sub) {
        if (n == null) return;
        if (n.key.compareTo(fromKey) >= 0 && n.key.compareTo(toKey) < 0) {
            fillSubMap(n.left, fromKey, toKey, sub);
            sub.put(n.key, n.value);
            fillSubMap(n.right, fromKey, toKey, sub);
        } else if (n.key.compareTo(fromKey) < 0) {
            fillSubMap(n.right, fromKey, toKey, sub);
        } else {
            fillSubMap(n.left, fromKey, toKey, sub);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = new TreeSet<>();
        inOrderKeys(root, set);
        return set;
    }

    private void inOrderKeys(Node n, Set<Integer> set) {
        if (n == null) return;
        inOrderKeys(n.left, set);
        set.add(n.key);
        inOrderKeys(n.right, set);
    }

    @Override
    public Collection<String> values() {
        List<String> list = new ArrayList<>();
        inOrderValues(root, list);
        return list;
    }

    private void inOrderValues(Node n, List<String> list) {
        if (n == null) return;
        inOrderValues(n.left, list);
        list.add(n.value);
        inOrderValues(n.right, list);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> set = new LinkedHashSet<>();
        inOrderEntries(root, set);
        return set;
    }

    private void inOrderEntries(Node n, Set<Entry<Integer, String>> set) {
        if (n == null) return;
        inOrderEntries(n.left, set);
        set.add(new AbstractMap.SimpleEntry<>(n.key, n.value));
        inOrderEntries(n.right, set);
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendToString(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

    private void appendToString(Node n, StringBuilder sb) {
        if (n == null) return;
        appendToString(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        appendToString(n.right, sb);
    }
}