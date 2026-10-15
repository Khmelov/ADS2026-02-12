package by.it.group551004.zubko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root = null;
    private int size = 0;

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
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
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.right = x;
            x.parent = y;
        }
    }

    private void splay(Node x) {
        if (x == null) {
            return;
        }
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
            } else if (x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x;
    }

    private Node find(Integer key) {
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                splay(curr);
                return curr;
            }
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node != null) {
            inOrder(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrder(node.right, sb);
        }
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
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        return (n == null ? null : n.value);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node curr = root;
        Node parent = null;
        int cmp = 0;
        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                String old = curr.value;
                curr.value = value;
                splay(curr);
                return old;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node n = find((Integer) key);
        if (n == null) {
            return null;
        }
        String oldValue = n.value;
        size--;
        if (n.left == null) {
            root = n.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (n.right == null) {
            root = n.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node leftSubtree = n.left;
            leftSubtree.parent = null;
            Node rightSubtree = n.right;
            rightSubtree.parent = null;
            Node max = leftSubtree;
            while (max.right != null) {
                max = max.right;
            }
            root = leftSubtree;
            splay(max);
            root.right = rightSubtree;
            rightSubtree.parent = root;
        }
        return oldValue;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        splay(n);
        return n.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) < 0) {
                best = curr.key;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp <= 0) {
                best = curr.key;
                if (cmp == 0) {
                    return best;
                }
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp >= 0) {
                best = curr.key;
                if (cmp == 0) {
                    return best;
                }
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node curr = root;
        Integer best = null;
        while (curr != null) {
            if (curr.key.compareTo(key) > 0) {
                best = curr.key;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return best;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        fillHeadMap(root, toKey, inclusive, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap subMap) {
        if (node != null) {
            fillHeadMap(node.left, toKey, inclusive, subMap);
            int cmp = node.key.compareTo(toKey);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                subMap.put(node.key, node.value);
                fillHeadMap(node.right, toKey, inclusive, subMap);
            }
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        fillTailMap(root, fromKey, inclusive, subMap);
        return subMap;
    }

    private void fillTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap subMap) {
        if (node != null) {
            int cmp = node.key.compareTo(fromKey);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                fillTailMap(node.left, fromKey, inclusive, subMap);
                subMap.put(node.key, node.value);
            }
            fillTailMap(node.right, fromKey, inclusive, subMap);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map<?, ?> m)) {
            return false;
        }
        if (m.size() != size) {
            return false;
        }
        return checkEquals(root, m);
    }

    private boolean checkEquals(Node node, Map<?, ?> m) {
        if (node == null) {
            return true;
        }
        Object val = m.get(node.key);
        if (val == null ? (node.value != null || !m.containsKey(node.key)) : !val.equals(node.value)) {
            return false;
        }
        return checkEquals(node.left, m) && checkEquals(node.right, m);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса NavigableMap     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
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
