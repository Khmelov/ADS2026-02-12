package by.it.group510902.shust.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        int key;
        String value;
        Node left, right;
        Node(int key, String value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size = 0;

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    private Node splay(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) {
            if (h.left == null) return h;
            if (key < h.left.key) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (key > h.left.key) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) h.left = rotateLeft(h.left);
            }
            return h.left == null ? h : rotateRight(h);
        } else if (key > h.key) {
            if (h.right == null) return h;
            if (key < h.right.key) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) h.right = rotateRight(h.right);
            } else if (key > h.right.key) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }
            return h.right == null ? h : rotateLeft(h);
        } else {
            return h;
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        if (root.key == key) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(key, value);
        if (key < root.key) {
            n.right = root;
            n.left = root.left;
            root.left = null;
        } else {
            n.left = root;
            n.right = root.right;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        int k = (Integer) key;
        if (root == null) return null;
        root = splay(root, k);
        if (root.key != k) return null;

        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node temp = root.right;
            root = root.left;
            root = splay(root, k);
            root.right = temp;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        int k = (Integer) key;
        if (root == null) return null;
        root = splay(root, k);
        return (root.key == k) ? root.value : null;
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
    public Integer lowerKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key < key) {
                best = curr.key;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) root = splay(root, best);
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key <= key) {
                best = curr.key;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) root = splay(root, best);
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key >= key) {
                best = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) root = splay(root, best);
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key > key) {
                best = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) root = splay(root, best);
        return best;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node n, int toKey, MySplayMap map) {
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
        MySplayMap subMap = new MySplayMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node n, int fromKey, MySplayMap map) {
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
        root = splay(root, x.key);
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        root = splay(root, x.key);
        return x.key;
    }

    // --- Обязательные заглушки интерфейса NavigableMap ---
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> firstEntry() { return null; }
    @Override public Map.Entry<Integer, String> lastEntry() { return null; }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Map.Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}