package by.it.group510902.makaruk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // --- Базовая операция Splay (перенос узла с ключом k в корень) ---
    private Node splay(Node h, Integer key) {
        if (h == null) return null;
        int cmp1 = key.compareTo(h.key);

        if (cmp1 < 0) {
            if (h.left == null) return h;
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) h.left = rotateLeft(h.left);
            }
            return h.left == null ? h : rotateRight(h);
        } else if (cmp1 > 0) {
            if (h.right == null) return h;
            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) h.right = rotateRight(h.right);
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }
            return h.right == null ? h : rotateLeft(h);
        } else {
            return h;
        }
    }

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

    // --- Обязательные методы ---

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsVal(root, value);
    }

    private boolean containsVal(Node n, Object val) {
        if (n == null) return false;
        if ((n.value == null && val == null) || (n.value != null && n.value.equals(val))) return true;
        return containsVal(n.left, val) || containsVal(n.right, val);
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer) || root == null) return null;
        Integer key = (Integer) keyObj;
        root = splay(root, key);
        return root.key.equals(key) ? root.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(key, value);
        if (cmp < 0) {
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
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer) || root == null) return null;
        Integer key = (Integer) keyObj;
        root = splay(root, key);
        if (!root.key.equals(key)) return null;

        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node x = root.right;
            root = splay(root.left, key);
            root.right = x;
        }
        size--;
        return old;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        root = splay(root, curr.key);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        root = splay(root, curr.key);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (isEmpty() || key == null) return null;
        root = splay(root, key);
        if (root.key.compareTo(key) < 0) return root.key;
        Node curr = root.left;
        if (curr == null) return null;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (isEmpty() || key == null) return null;
        root = splay(root, key);
        if (root.key.compareTo(key) <= 0) return root.key;
        Node curr = root.left;
        if (curr == null) return null;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (isEmpty() || key == null) return null;
        root = splay(root, key);
        if (root.key.compareTo(key) >= 0) return root.key;
        Node curr = root.right;
        if (curr == null) return null;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (isEmpty() || key == null) return null;
        root = splay(root, key);
        if (root.key.compareTo(key) > 0) return root.key;
        Node curr = root.right;
        if (curr == null) return null;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        fillHead(root, toKey, sub);
        return sub;
    }

    private void fillHead(Node n, Integer toKey, MySplayMap sub) {
        if (n == null) return;
        if (n.key.compareTo(toKey) < 0) {
            sub.put(n.key, n.value);
            fillHead(n.left, toKey, sub);
            fillHead(n.right, toKey, sub);
        } else {
            fillHead(n.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        fillTail(root, fromKey, sub);
        return sub;
    }

    private void fillTail(Node n, Integer fromKey, MySplayMap sub) {
        if (n == null) return;
        if (n.key.compareTo(fromKey) >= 0) {
            sub.put(n.key, n.value);
            fillTail(n.left, fromKey, sub);
            fillTail(n.right, fromKey, sub);
        } else {
            fillTail(n.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inOrder(n.right, sb);
    }

    // --- Остальные методы NavigableMap (заглушки для лабы) ---
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { for(Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue()); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
