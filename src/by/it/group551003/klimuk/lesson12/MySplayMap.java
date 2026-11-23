package by.it.group551003.klimuk.lesson12;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right, parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;
    private final Comparator<? super Integer> comparator;

    public MySplayMap() {
        this.comparator = null;
    }

    public MySplayMap(Comparator<? super Integer> comparator) {
        this.comparator = comparator;
    }

    private int compare(int a, int b) {
        return comparator == null ? Integer.compare(a, b) : comparator.compare(a, b);
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
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
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
        if (key == null) throw new NullPointerException("Key is null");
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node cur = root;
        Node parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = compare(key, cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }
        Node z = new Node(key, value);
        z.parent = parent;
        if (cmp < 0) parent.left = z;
        else parent.right = z;
        size++;
        splay(z);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findAndSplay((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && findAndSplay((Integer) key) != null;
    }

    private Node findAndSplay(int key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int c = compare(key, cur.key);
            if (c < 0) cur = cur.left;
            else if (c > 0) cur = cur.right;
            else {
                splay(cur);
                return cur;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValueHelper(n.left, value) || containsValueHelper(n.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = findAndSplay((Integer) key);
        if (z == null) return null;
        String old = z.value;
        Node leftSub = z.left;
        Node rightSub = z.right;
        if (leftSub != null) leftSub.parent = null;
        if (rightSub != null) rightSub.parent = null;

        if (leftSub == null) {
            root = rightSub;
        } else {
            Node max = leftSub;
            while (max.right != null) max = max.right;
            root = leftSub;
            splay(max);
            max.right = rightSub;
            if (rightSub != null) rightSub.parent = max;
        }
        size--;
        return old;
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
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int c = compare(key, cur.key);
            if (c <= 0) {
                cur = cur.left;
            } else {
                best = cur.key;
                cur = cur.right;
            }
        }
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int c = compare(key, cur.key);
            if (c < 0) {
                cur = cur.left;
            } else {
                best = cur.key;
                cur = cur.right;
            }
        }
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int c = compare(key, cur.key);
            if (c > 0) {
                cur = cur.right;
            } else {
                best = cur.key;
                cur = cur.left;
            }
        }
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int c = compare(key, cur.key);
            if (c >= 0) {
                cur = cur.right;
            } else {
                best = cur.key;
                cur = cur.left;
            }
        }
        return best;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap sub = new MySplayMap(comparator);
        collectHead(root, toKey, inclusive, sub);
        return sub;
    }

    private void collectHead(Node n, int toKey, boolean inclusive, MySplayMap sub) {
        if (n == null) return;
        int c = compare(n.key, toKey);
        if (c < 0 || (c == 0 && inclusive)) {
            collectHead(n.left, toKey, inclusive, sub);
            sub.put(n.key, n.value);
            collectHead(n.right, toKey, inclusive, sub);
        } else {
            collectHead(n.left, toKey, inclusive, sub);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap sub = new MySplayMap(comparator);
        collectTail(root, fromKey, inclusive, sub);
        return sub;
    }

    private void collectTail(Node n, int fromKey, boolean inclusive, MySplayMap sub) {
        if (n == null) return;
        int c = compare(n.key, fromKey);
        if (c > 0 || (c == 0 && inclusive)) {
            collectTail(n.left, fromKey, inclusive, sub);
            sub.put(n.key, n.value);
            collectTail(n.right, fromKey, inclusive, sub);
        } else {
            collectTail(n.right, fromKey, inclusive, sub);
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inorder(n.right, sb);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return comparator;
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}