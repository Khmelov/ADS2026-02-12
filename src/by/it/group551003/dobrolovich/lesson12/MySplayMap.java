package by.it.group551003.dobrolovich.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node implements Map.Entry<Integer, String> {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(int key, String value) {
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
            String old = value;
            value = v;
            return old;
        }
    }

    private Node root;
    private int size;

    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        if (x == p.left) {
            p.left = x.right;
            if (x.right != null) {
                x.right.parent = p;
            }
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) {
                x.left.parent = p;
            }
            x.left = p;
        }
        p.parent = x;
        x.parent = g;
        if (g != null) {
            if (g.left == p) {
                g.left = x;
            } else {
                g.right = x;
            }
        }
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g != null) {
                if ((g.left == p) == (p.left == x)) {
                    rotate(p);
                } else {
                    rotate(x);
                }
            }
            rotate(x);
        }
        root = x;
    }

    private Node search(int key) {
        Node c = root;
        Node last = null;
        while (c != null) {
            last = c;
            if (key < c.key) {
                c = c.left;
            } else if (key > c.key) {
                c = c.right;
            } else {
                splay(c);
                return c;
            }
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    private void detachRoot() {
        Node l = root.left;
        Node r = root.right;
        if (l != null) {
            l.parent = null;
        }
        if (r != null) {
            r.parent = null;
        }
        if (l == null) {
            root = r;
            return;
        }
        Node m = l;
        while (m.right != null) {
            m = m.right;
        }
        root = l;
        splay(m);
        root.right = r;
        if (r != null) {
            r.parent = root;
        }
    }

    private Node min(Node n) {
        if (n == null) {
            return null;
        }
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    private Node max(Node n) {
        if (n == null) {
            return null;
        }
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    private Node successor(Node n) {
        if (n.right != null) {
            return min(n.right);
        }
        Node p = n.parent;
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }

    private Node lowerNode(int k) {
        Node c = root;
        Node r = null;
        while (c != null) {
            if (c.key < k) {
                r = c;
                c = c.right;
            } else {
                c = c.left;
            }
        }
        return r;
    }

    private Node floorNode(int k) {
        Node c = root;
        Node r = null;
        while (c != null) {
            if (c.key <= k) {
                r = c;
                c = c.right;
            } else {
                c = c.left;
            }
        }
        return r;
    }

    private Node ceilingNode(int k) {
        Node c = root;
        Node r = null;
        while (c != null) {
            if (c.key >= k) {
                r = c;
                c = c.left;
            } else {
                c = c.right;
            }
        }
        return r;
    }

    private Node higherNode(int k) {
        Node c = root;
        Node r = null;
        while (c != null) {
            if (c.key > k) {
                r = c;
                c = c.left;
            } else {
                c = c.right;
            }
        }
        return r;
    }

    private Integer keyOf(Node n) {
        return n == null ? null : n.key;
    }

    private MySplayMap range(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap r = new MySplayMap();
        for (Node n = min(root); n != null; n = successor(n)) {
            if (from != null) {
                if (n.key < from.intValue() || (n.key == from.intValue() && !fromInclusive)) {
                    continue;
                }
            }
            if (to != null) {
                if (n.key > to.intValue() || (n.key == to.intValue() && !toInclusive)) {
                    break;
                }
            }
            r.put(n.key, n.value);
        }
        return r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Node n = min(root); n != null; n = successor(n)) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(n.key).append('=').append(n.value);
        }
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node c = root;
        Node parent = null;
        while (c != null) {
            parent = c;
            if (key < c.key) {
                c = c.left;
            } else if (key > c.key) {
                c = c.right;
            } else {
                String old = c.value;
                c.value = value;
                splay(c);
                return old;
            }
        }
        Node n = new Node(key, value);
        n.parent = parent;
        if (parent == null) {
            root = n;
        } else if (key < parent.key) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        splay(n);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = search((Integer) key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        detachRoot();
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = search((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && search((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node n = min(root); n != null; n = successor(n)) {
            if (n.value == null ? value == null : n.value.equals(value)) {
                return true;
            }
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return range(null, false, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return range(fromKey, true, null, false);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return range(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(null, false, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(fromKey, inclusive, null, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return max(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyOf(lowerNode(key));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOf(floorNode(key));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOf(ceilingNode(key));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOf(higherNode(key));
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return lowerNode(key);
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return floorNode(key);
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return ceilingNode(key);
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return higherNode(key);
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        return min(root);
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        return max(root);
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Node n = min(root);
        if (n == null) {
            return null;
        }
        search(n.key);
        detachRoot();
        size--;
        return n;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Node n = max(root);
        if (n == null) {
            return null;
        }
        search(n.key);
        detachRoot();
        size--;
        return n;
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}