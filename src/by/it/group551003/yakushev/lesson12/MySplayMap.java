package by.it.group551003.yakushev.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * Splay-дерево (нисходящий splay).
 */
@SuppressWarnings("unchecked")
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class Entry implements Map.Entry<Integer, String> {
        private final Integer key;
        private final String value;

        Entry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    /** Нисходящий splay: после вызова в корне узел с ключом key либо ближайший к нему. */
    private void splay(int key) {
        if (root == null) return;
        Node header = new Node(0, null);
        Node l = header, r = header, t = root;
        while (true) {
            if (key < t.key) {
                if (t.left == null) break;
                if (key < t.left.key) {
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) break;
                }
                r.left = t;
                r = t;
                t = t.left;
            } else if (key > t.key) {
                if (t.right == null) break;
                if (key > t.right.key) {
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) break;
                }
                l.right = t;
                l = t;
                t = t.right;
            } else {
                break;
            }
        }
        l.right = t.left;
        r.left = t.right;
        t.left = header.right;
        t.right = header.left;
        root = t;
    }

    /** Все узлы в порядке возрастания ключей (итеративно, дерево может быть глубоким). */
    private Node[] inOrder() {
        Node[] res = new Node[size];
        Node[] stack = new Node[size];
        int sp = 0, idx = 0;
        Node cur = root;
        while (cur != null || sp > 0) {
            while (cur != null) {
                stack[sp++] = cur;
                cur = cur.left;
            }
            cur = stack[--sp];
            res[idx++] = cur;
            cur = cur.right;
        }
        return res;
    }

    private MySplayMap range(boolean hasLo, int lo, boolean loInc, boolean hasHi, int hi, boolean hiInc) {
        MySplayMap res = new MySplayMap();
        for (Node n : inOrder()) {
            if (hasLo && (n.key < lo || (n.key == lo && !loInc))) continue;
            if (hasHi && (n.key > hi || (n.key == hi && !hiInc))) continue;
            res.put(n.key, n.value);
        }
        return res;
    }

    private Node lowerNode(int key, boolean inclusive) {
        Node best = null, n = root;
        while (n != null) {
            if (n.key < key || (inclusive && n.key == key)) {
                best = n;
                n = n.right;
            } else {
                n = n.left;
            }
        }
        return best;
    }

    private Node higherNode(int key, boolean inclusive) {
        Node best = null, n = root;
        while (n != null) {
            if (n.key > key || (inclusive && n.key == key)) {
                best = n;
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return best;
    }

    private static Map.Entry<Integer, String> entry(Node n) {
        return n == null ? null : new Entry(n.key, n.value);
    }

    private static Integer keyOf(Node n) {
        return n == null ? null : n.key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Node n : inOrder()) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(n.key).append('=').append(n.value);
        }
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        int k = key;
        if (root == null) {
            root = new Node(k, value);
            size = 1;
            return null;
        }
        splay(k);
        if (root.key == k) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(k, value);
        if (k < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        int k = (Integer) key;
        splay(k);
        if (root.key != k) return null;
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            root = root.left;
            splay(k); // максимум левого поддерева всплывает в корень
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        int k = (Integer) key;
        splay(k);
        return root.key == k ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || root == null) return false;
        int k = (Integer) key;
        splay(k);
        return root.key == k;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node n : inOrder()) {
            if (value == null ? n.value == null : value.equals(n.value)) return true;
        }
        return false;
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
    public Integer lowerKey(Integer key) {
        return keyOf(lowerNode(key, false));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOf(lowerNode(key, true));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOf(higherNode(key, true));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOf(higherNode(key, false));
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return entry(lowerNode(key, false));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return entry(lowerNode(key, true));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return entry(higherNode(key, true));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return entry(higherNode(key, false));
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        if (root == null) return null;
        return entry(higherNode(Integer.MIN_VALUE, true));
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        if (root == null) return null;
        return entry(lowerNode(Integer.MAX_VALUE, true));
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Map.Entry<Integer, String> e = firstEntry();
        if (e != null) remove(e.getKey());
        return e;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Map.Entry<Integer, String> e = lastEntry();
        if (e != null) remove(e.getKey());
        return e;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return range(false, 0, false, true, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(true, fromKey, true, false, 0, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(false, 0, false, true, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(true, fromKey, inclusive, false, 0, false);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        if (fromKey > toKey) throw new IllegalArgumentException("fromKey > toKey");
        return range(true, fromKey, fromInclusive, true, toKey, toInclusive);
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
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}