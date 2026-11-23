package by.it.group551003.popko.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // ---------- splay ----------

    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        if (p.left == x) {
            p.left = x.right;
            if (x.right != null) x.right.parent = p;
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) x.left.parent = p;
            x.left = p;
        }
        p.parent = x;
        x.parent = g;
        if (g != null) {
            if (g.left == p) g.left = x;
            else g.right = x;
        } else {
            root = x;
        }
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                rotate(x);
            } else if ((g.left == p) == (p.left == x)) {
                rotate(p);
                rotate(x);
            } else {
                rotate(x);
                rotate(x);
            }
        }
    }

    private Node find(Integer key) {
        Node n = root;
        Node last = null;
        while (n != null) {
            last = n;
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else {
                splay(n);
                return n;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    // ---------- NavigableMap ----------

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Integer lowerKey(Integer key) {
        Node n = root;
        Integer res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp > 0) { res = n.key; n = n.right; }
            else n = n.left;
        }
        return res;
    }

    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Integer floorKey(Integer key) {
        Node n = root;
        Integer res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) return n.key;
            if (cmp > 0) { res = n.key; n = n.right; }
            else n = n.left;
        }
        return res;
    }

    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Integer ceilingKey(Integer key) {
        Node n = root;
        Integer res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) return n.key;
            if (cmp < 0) { res = n.key; n = n.left; }
            else n = n.right;
        }
        return res;
    }

    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Integer higherKey(Integer key) {
        Node n = root;
        Integer res = null;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp < 0) { res = n.key; n = n.left; }
            else n = n.right;
        }
        return res;
    }

    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, fromKey, fromInclusive, toKey, toInclusive, res);
        return res;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, null, false, toKey, inclusive, res);
        return res;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        TreeMap<Integer, String> res = new TreeMap<>();
        collectRange(root, fromKey, inclusive, null, false, res);
        return res;
    }

    private void collectRange(Node n, Integer from, boolean fromInc, Integer to, boolean toInc, TreeMap<Integer, String> res) {
        if (n == null) return;
        if (from != null && n.key.compareTo(from) < 0) {
            collectRange(n.right, from, fromInc, to, toInc, res);
            return;
        }
        if (to != null && n.key.compareTo(to) > 0) {
            collectRange(n.left, from, fromInc, to, toInc, res);
            return;
        }
        collectRange(n.left, from, fromInc, to, toInc, res);
        boolean okFrom = from == null || (fromInc ? n.key.compareTo(from) >= 0 : n.key.compareTo(from) > 0);
        boolean okTo = to == null || (toInc ? n.key.compareTo(to) <= 0 : n.key.compareTo(to) < 0);
        if (okFrom && okTo) res.put(n.key, n.value);
        collectRange(n.right, from, fromInc, to, toInc, res);
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() { return null; }

    @Override
    public java.util.SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public java.util.SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public java.util.SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    // ---------- Map ----------

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) { return find((Integer) key) != null; }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (n.value == null ? value == null : n.value.equals(value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public String get(Object key) {
        Node n = find((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node n = root;
        Node last = null;
        while (n != null) {
            last = n;
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else {
                String old = n.value;
                n.value = value;
                splay(n);
                return old;
            }
        }
        Node z = new Node(key, value);
        z.parent = last;
        if (key.compareTo(last.key) < 0) last.left = z;
        else last.right = z;
        size++;
        splay(z);
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node z = find(key);
        if (z == null) return null;
        String old = z.value;
        splay(z);

        Node leftTree = z.left;
        Node rightTree = z.right;
        if (leftTree != null) leftTree.parent = null;
        if (rightTree != null) rightTree.parent = null;

        if (leftTree == null) {
            root = rightTree;
        } else {
            Node max = leftTree;
            while (max.right != null) max = max.right;
            // splay max в корень левого дерева
            splayInTree(max, leftTree);
            max.right = rightTree;
            if (rightTree != null) rightTree.parent = max;
            root = max;
        }
        size--;
        return old;
    }

    private void splayInTree(Node x, Node treeRoot) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) rotateInTree(x, treeRoot);
            else if ((g.left == p) == (p.left == x)) {
                rotateInTree(p, treeRoot);
                rotateInTree(x, treeRoot);
            } else {
                rotateInTree(x, treeRoot);
                rotateInTree(x, treeRoot);
            }
        }
    }

    private void rotateInTree(Node x, Node treeRoot) {
        Node p = x.parent;
        Node g = p.parent;
        if (p.left == x) {
            p.left = x.right;
            if (x.right != null) x.right.parent = p;
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) x.left.parent = p;
            x.left = p;
        }
        p.parent = x;
        x.parent = g;
        if (g != null) {
            if (g.left == p) g.left = x;
            else g.right = x;
        }
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        for (java.util.Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Set<java.util.Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }
}