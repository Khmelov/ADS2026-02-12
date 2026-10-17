package by.it.group551002.efimchikov.lesson12;

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
        int key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

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
            } else {
                rotateLeft(p);
                rotateRight(g);
            }
        }
        root = x;
    }

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

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    private Node find(int key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                splay(cur);
                return cur;
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
            root = new Node(key, value);
            size++;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                cur = cur.right;
            } else {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = find((Integer) key);
        if (node == null) {
            return null;
        }
        String removed = node.value;
        Node left = node.left;
        Node right = node.right;
        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }
        if (left == null) {
            root = right;
        } else {
            root = left;
            Node maxLeft = left;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft);
            root.right = right;
            if (right != null) {
                right.parent = root;
            }
        }
        size--;
        return removed;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = find((Integer) key);
        return node == null ? null : node.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return false;
        }
        return find((Integer) key) != null;
    }

    public boolean containsKey(Integer key) {
        return containsKey((Object) key);
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
    public String toString() {
        return "{" + toString(root) + "}";
    }

    private String toString(Node node) {
        if (node == null) {
            return "";
        }
        String left = toString(node.left);
        String right = toString(node.right);
        String current = node.key + "=" + node.value;
        if (left.length() > 0 && right.length() > 0) {
            return left + ", " + current + ", " + right;
        } else if (left.length() > 0) {
            return left + ", " + current;
        } else if (right.length() > 0) {
            return current + ", " + right;
        } else {
            return current;
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        copyHead(root, toKey, result);
        return result;
    }

    private void copyHead(Node node, int toKey, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key < toKey) {
            copyHead(node.left, toKey, result);
            result.put(node.key, node.value);
            copyHead(node.right, toKey, result);
        } else {
            copyHead(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap result = new MySplayMap();
        copyTail(root, fromKey, result);
        return result;
    }

    private void copyTail(Node node, int fromKey, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            copyTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            copyTail(node.right, fromKey, result);
        } else {
            copyTail(node.right, fromKey, result);
        }
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
        splay(cur);
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
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (key <= cur.key) {
                cur = cur.left;
            } else {
                result = cur.key;
                cur = cur.right;
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
            if (key < cur.key) {
                cur = cur.left;
            } else if (key > cur.key) {
                result = cur.key;
                cur = cur.right;
            } else {
                return cur.key;
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
            if (key > cur.key) {
                cur = cur.right;
            } else if (key < cur.key) {
                result = cur.key;
                cur = cur.left;
            } else {
                return cur.key;
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
            if (key >= cur.key) {
                cur = cur.right;
            } else {
                result = cur.key;
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }
}
