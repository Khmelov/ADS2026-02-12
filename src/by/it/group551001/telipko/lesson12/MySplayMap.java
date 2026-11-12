package by.it.group551001.telipko.lesson12;

import java.util.*;


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

    private Node[] path = new Node[64];

    private void splay(int key) {
        if (root == null) return;
        int d = 0;
        Node n = root;
        while (true) {
            if (d == path.length) {
                Node[] bigger = new Node[path.length * 2];
                for (int j = 0; j < d; j++) bigger[j] = path[j];
                path = bigger;
            }
            path[d++] = n;
            Node next = key < n.key ? n.left : key > n.key ? n.right : null;
            if (next == null) break;
            n = next;
        }

        int i = d - 1;
        while (i > 0) {
            Node x = path[i], p = path[i - 1];
            if (i == 1) {
                root = (p.left == x) ? rotateRight(p) : rotateLeft(p);
                break;
            }
            Node g = path[i - 2], sub;
            if (g.left == p && p.left == x) {
                sub = rotateRight(rotateRight(g));
            } else if (g.right == p && p.right == x) {
                sub = rotateLeft(rotateLeft(g));
            } else if (g.left == p) {
                g.left = rotateLeft(p);
                sub = rotateRight(g);
            } else {
                g.right = rotateRight(p);
                sub = rotateLeft(g);
            }
            if (i == 2) root = sub;
            else if (path[i - 3].left == g) path[i - 3].left = sub;
            else path[i - 3].right = sub;
            path[i - 2] = sub;
            i -= 2;
        }
        for (int j = 0; j < d; j++) path[j] = null;
    }


    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        splay(key);
        int c = key.compareTo(root.key);
        if (c == 0) {
            String old = root.value;
            root.value = value;
            return old;
        }

        Node n = new Node(key, value);
        if (c < 0) {
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
            splay(k);
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        splay((Integer) key);
        return root.key.equals(key) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || root == null) return false;
        splay((Integer) key);
        return root.key.equals(key);
    }


    private Integer neighbour(Integer key, boolean less, boolean orEqual) {
        Node n = root;
        Integer best = null;
        while (n != null) {
            int c = n.key.compareTo(key);
            if (c == 0 && orEqual) {
                best = n.key;
                break;
            }
            if (less) {
                if (c < 0) {
                    best = n.key;
                    n = n.right;
                } else n = n.left;
            } else {
                if (c > 0) {
                    best = n.key;
                    n = n.left;
                } else n = n.right;
            }
        }
        if (best != null) splay(best);
        return best;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return neighbour(key, true, false);
    }

    @Override
    public Integer floorKey(Integer key) {
        return neighbour(key, true, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return neighbour(key, false, true);
    }

    @Override
    public Integer higherKey(Integer key) {
        return neighbour(key, false, false);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        splay(n.key);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        splay(n.key);
        return n.key;
    }


    private void copyRange(Node n, Integer from, boolean fromInc, Integer to, boolean toInc, MySplayMap map) {
        if (n == null) return;
        boolean okFrom = from == null || (fromInc ? n.key >= from : n.key > from);
        boolean okTo = to == null || (toInc ? n.key <= to : n.key < to);
        if (okFrom) copyRange(n.left, from, fromInc, to, toInc, map);
        if (okFrom && okTo) map.put(n.key, n.value);
        if (okTo) copyRange(n.right, from, fromInc, to, toInc, map);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        copyRange(root, null, false, toKey, inclusive, map);
        return map;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        copyRange(root, fromKey, inclusive, null, false, map);
        return map;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        MySplayMap map = new MySplayMap();
        copyRange(root, fromKey, fromInclusive, toKey, toInclusive, map);
        return map;
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

    private boolean hasValue(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(value, n.value)) return true;
        return hasValue(n.left, value) || hasValue(n.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        inorder(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
