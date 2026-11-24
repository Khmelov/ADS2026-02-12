package by.it.group510902.slesar.lesson12;


import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.NavigableSet;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private Node find(Integer key) {
        Node n = root;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c == 0) {
                splay(n);
                return n;
            }
            n = c < 0 ? n.left : n.right;
        }
        return null;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node n) {
        while (n.parent != null) {
            Node p = n.parent;
            Node g = p.parent;

            if (g == null) {
                if (n == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (n == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (n == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (n == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node n = root, p = null;
        while (n != null) {
            p = n;
            int c = key.compareTo(n.key);
            if (c == 0) {
                String old = n.value;
                n.value = value;
                splay(n);
                return old;
            }
            n = c < 0 ? n.left : n.right;
        }

        Node added = new Node(key, value, p);
        if (key < p.key) p.left = added;
        else p.right = added;
        size++;
        splay(added);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && find((Integer) key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find((Integer) key);
        if (n == null) return null;

        String old = n.value;
        Node left = n.left;
        Node right = n.right;

        if (left != null) left.parent = null;
        if (right != null) right.parent = null;

        if (left == null) {
            root = right;
        } else {
            root = left;
            Node max = left;
            while (max.right != null) max = max.right;
            splay(max);
            root.right = right;
            if (right != null) right.parent = root;
        }

        size--;
        return old;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        return (n.value == null ? value == null : n.value.equals(value))
                || containsValue(n.left, value)
                || containsValue(n.right, value);
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private void append(Node n, StringBuilder sb) {
        if (n == null) return;
        append(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        append(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        append(root, sb);
        return sb.append("}").toString();
    }

    private Integer neighbour(Integer key, int mode) {
        Node n = root;
        Integer result = null;

        while (n != null) {
            int cmp = key.compareTo(n.key);

            if (mode == 0) { // lowerKey: strictly smaller
                if (cmp <= 0) {
                    n = n.left;
                } else {
                    result = n.key;
                    n = n.right;
                }
            } else if (mode == 1) { // floorKey: smaller or equal
                if (cmp < 0) {
                    n = n.left;
                } else {
                    result = n.key;
                    if (cmp == 0) break;
                    n = n.right;
                }
            } else if (mode == 2) { // ceilingKey: greater or equal
                if (cmp > 0) {
                    n = n.right;
                } else {
                    result = n.key;
                    if (cmp == 0) break;
                    n = n.left;
                }
            } else { // higherKey: strictly greater
                if (cmp >= 0) {
                    n = n.right;
                } else {
                    result = n.key;
                    n = n.left;
                }
            }
        }

        return result;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return key == null ? null : neighbour(key, 0);
    }

    @Override
    public Integer floorKey(Integer key) {
        return key == null ? null : neighbour(key, 1);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return key == null ? null : neighbour(key, 2);
    }

    @Override
    public Integer higherKey(Integer key) {
        return key == null ? null : neighbour(key, 3);
    }

    private Node minimum(Node n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maximum(Node n) {
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public Integer firstKey() {
        Node n = minimum(root);
        if (n == null) throw new java.util.NoSuchElementException();
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = maximum(root);
        if (n == null) throw new java.util.NoSuchElementException();
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        copy(root, result, null, toKey);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        copy(root, result, fromKey, null);
        return result;
    }

    private void copy(Node n, MySplayMap result, Integer from, Integer to) {
        if (n == null) return;
        copy(n.left, result, from, to);
        if ((from == null || n.key >= from)
                && (to == null || n.key < to)) {
            result.put(n.key, n.value);
        }
        copy(n.right, result, from, to);
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer from, Integer to) {
        MySplayMap result = new MySplayMap();
        copy(root, result, from, to);
        return result;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer k) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> floorEntry(Integer k) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer k) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> higherEntry(Integer k) { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> subMap(
            Integer from, boolean fromInclusive,
            Integer to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer to, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer from, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
}
