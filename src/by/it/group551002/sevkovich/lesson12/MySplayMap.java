package by.it.group551002.sevkovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.left && p == g.right) {
                rotateRight(p);
                rotateLeft(g);
            } else { // x == p.right && p == g.left
                rotateLeft(p);
                rotateRight(g);
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        if (key == null) {
            return null;
        }
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                return cur;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return null;
    }

    // ---------- Map methods ----------

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        Node cur = root;
        Node parent = null;

        while (cur != null) {
            parent = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        Node node = new Node(key, value);
        node.parent = parent;
        if (key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;
        splay(node);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node node = findNode(k);
        if (node == null) {
            return null;
        }
        splay(node);
        return node.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node node = findNode(k);
        if (node == null) {
            return null;
        }

        splay(node);
        String oldValue = root.value;

        Node L = root.left;
        Node R = root.right;

        if (L == null) {
            root = R;
            if (R != null) {
                R.parent = null;
            }
        } else if (R == null) {
            root = L;
            L.parent = null;
        } else {
            L.parent = null;
            Node max = L;
            while (max.right != null) {
                max = max.right;
            }
            splay(max);
            max.right = R;
            R.parent = max;
        }

        size--;
        return oldValue;
    }

    public String remove(Integer key) {
        return remove((Object) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return findNode((Integer) key) != null;
    }

    public boolean containsKey(Integer key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    public boolean containsValue(String value) {
        return containsValue((Object) value);
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ---------- SortedMap / NavigableMap methods ----------

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node cur = root;
        while (cur.right != null) {
            cur = cur.right;
        }
        return cur.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    private void addToRange(Node node, Integer fromKey, boolean fromInclusive,
                            Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        addToRange(node.left, fromKey, fromInclusive, toKey, toInclusive, result);

        boolean ok = true;
        if (fromKey != null) {
            int cmp = node.key.compareTo(fromKey);
            if (cmp < 0 || (cmp == 0 && !fromInclusive)) {
                ok = false;
            }
        }
        if (ok && toKey != null) {
            int cmp = node.key.compareTo(toKey);
            if (cmp > 0 || (cmp == 0 && !toInclusive)) {
                ok = false;
            }
        }
        if (ok) {
            result.put(node.key, node.value);
        }

        addToRange(node.right, fromKey, fromInclusive, toKey, toInclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        addToRange(root, null, true, toKey, inclusive, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        addToRange(root, fromKey, inclusive, null, true, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }
        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        MySplayMap result = new MySplayMap();
        addToRange(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) < 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp <= 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            int cmp = cur.key.compareTo(key);
            if (cmp >= 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) > 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    // ---------- Unsupported operations ----------

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("lowerEntry");
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("floorEntry");
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("ceilingEntry");
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("higherEntry");
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("firstEntry");
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("lastEntry");
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("pollFirstEntry");
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("pollLastEntry");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("descendingMap");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("navigableKeySet");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("descendingKeySet");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }

    // ---------- toString ----------

    private String toString(Node node) {
        if (node == null) {
            return "";
        }
        String left = toString(node.left);
        String right = toString(node.right);
        String cur = node.key + "=" + node.value;

        if (left.isEmpty() && right.isEmpty()) {
            return cur;
        }
        if (left.isEmpty()) {
            return cur + ", " + right;
        }
        if (right.isEmpty()) {
            return left + ", " + cur;
        }
        return left + ", " + cur + ", " + right;
    }

    @Override
    public String toString() {
        return "{" + toString(root) + "}";
    }
}