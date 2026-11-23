package by.it.group551001.akynchits.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
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
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
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
    }

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
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (Objects.equals(node.value, value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                splay(curr);
                return curr.value;
            }
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
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
        Integer k = (Integer) key;
        Node curr = root;
        Node last = null;
        while (curr != null) {
            last = curr;
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                break;
            }
        }
        if (curr == null) {
            if (last != null) {
                splay(last);
            }
            return null;
        }

        splay(curr);
        String old = curr.value;

        if (curr.left == null) {
            root = curr.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node rightSubtree = curr.right;
            Node leftSubtree = curr.left;
            leftSubtree.parent = null;

            Node maxLeft = leftSubtree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            root = leftSubtree;
            splay(maxLeft);

            maxLeft.right = rightSubtree;
            if (rightSubtree != null) {
                rightSubtree.parent = maxLeft;
            }
            root = maxLeft;
        }

        size--;
        return old;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp < 0) {
                best = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp <= 0) {
                best = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp >= 0) {
                best = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node best = null;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp > 0) {
                best = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        }
        return null;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node p = root;
        while (p.left != null) {
            p = p.left;
        }
        splay(p);
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node p = root;
        while (p.right != null) {
            p = p.right;
        }
        splay(p);
        return p.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        inOrderCollect(root, subMap, null, false, toKey, inclusive);
        return subMap;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        inOrderCollect(root, subMap, fromKey, inclusive, null, false);
        return subMap;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap subMap = new MySplayMap();
        inOrderCollect(root, subMap, fromKey, fromInclusive, toKey, toInclusive);
        return subMap;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    private void inOrderCollect(Node node, MySplayMap target, Integer fromKey, boolean fromInc, Integer toKey, boolean toInc) {
        if (node == null) {
            return;
        }
        inOrderCollect(node.left, target, fromKey, fromInc, toKey, toInc);
        boolean afterFrom = fromKey == null || (fromInc ? node.key.compareTo(fromKey) >= 0 : node.key.compareTo(fromKey) > 0);
        boolean beforeTo = toKey == null || (toInc ? node.key.compareTo(toKey) <= 0 : node.key.compareTo(toKey) < 0);
        if (afterFrom && beforeTo) {
            target.put(node.key, node.value);
        }
        inOrderCollect(node.right, target, fromKey, fromInc, toKey, toInc);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
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
    public Set<Entry<Integer, String>> entrySet() {
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
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map<?, ?> m)) return false;
        if (m.size() != size()) return false;
        return checkEquals(root, m);
    }

    private boolean checkEquals(Node node, Map<?, ?> m) {
        if (node == null) return true;
        if (!checkEquals(node.left, m)) return false;
        Object val = m.get(node.key);
        if (val == null || !val.equals(node.value)) return false;
        return checkEquals(node.right, m);
    }

    @Override
    public int hashCode() {
        return calcHashCode(root);
    }

    private int calcHashCode(Node node) {
        if (node == null) return 0;
        int entryHash = Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
        return entryHash + calcHashCode(node.left) + calcHashCode(node.right);
    }
}
