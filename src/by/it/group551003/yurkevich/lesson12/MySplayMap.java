package by.it.group551003.yurkevich.lesson12;

import java.util.*;

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

    public MySplayMap() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }
        Node node = new Node(key, value);
        node.parent = parent;
        if (key < parent.key) parent.left = node;
        else parent.right = node;
        size++;
        splay(node);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;
        String old = node.value;
        splay(node);
        deleteRoot();
        size--;
        return old;
    }

    private void deleteRoot() {
        if (root.left == null) {
            root = root.right;
            if (root != null) root.parent = null;
        } else {
            Node rightSubtree = root.right;
            Node leftSubtree = root.left;
            leftSubtree.parent = null;
            root = leftSubtree;
            Node maxLeft = root;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);
            root.right = rightSubtree;
            if (rightSubtree != null) rightSubtree.parent = root;
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;
        splay(node);
        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node node = findNode((Integer) key);
        if (node == null) return false;
        splay(node);
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
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
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        collectHead(root, toKey, sub);
        return sub;
    }

    private void collectHead(Node n, int toKey, MySplayMap sub) {
        if (n == null) return;
        if (n.key < toKey) {
            collectHead(n.left, toKey, sub);
            sub.put(n.key, n.value);
            collectHead(n.right, toKey, sub);
        } else {
            collectHead(n.left, toKey, sub);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        collectTail(root, fromKey, sub);
        return sub;
    }

    private void collectTail(Node n, int fromKey, MySplayMap sub) {
        if (n == null) return;
        if (n.key >= fromKey) {
            collectTail(n.left, fromKey, sub);
            sub.put(n.key, n.value);
            collectTail(n.right, fromKey, sub);
        } else {
            collectTail(n.right, fromKey, sub);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node n = lowerNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node n = floorNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node n = ceilingNode(key);
        return n == null ? null : n.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node n = higherNode(key);
        return n == null ? null : n.key;
    }

    private Node findNode(int key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur;
        }
        if (last != null) splay(last);
        return null;
    }

    private Node lowerNode(int key) {
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key < key) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best;
    }

    private Node floorNode(int key) {
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key <= key) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best;
    }

    private Node ceilingNode(int key) {
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key >= key) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best;
    }

    private Node higherNode(int key) {
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key > key) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best;
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

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer a, Integer b) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer a, boolean b, Integer c, boolean d) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer k, boolean b) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer k, boolean b) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lowerEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer k) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}