package by.it.group551004.pedko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap<Integer, String> implements NavigableMap<Integer, String> {

    private class Node {
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

    private int compare(Object a, Object b) {
        java.lang.Integer x = (java.lang.Integer) a;
        java.lang.Integer y = (java.lang.Integer) b;
        return x.compareTo(y);
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

    private void rotateRight(Node x) {
        Node y = x.left;
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

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grand = parent.parent;

            if (grand == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grand.left) {
                rotateRight(grand);
                rotateRight(parent);
            } else if (node == parent.right && parent == grand.right) {
                rotateLeft(grand);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grand.left) {
                rotateLeft(parent);
                rotateRight(grand);
            } else {
                rotateRight(parent);
                rotateLeft(grand);
            }
        }
    }

    private Node find(Object key) {
        Node node = root;
        Node last = null;

        while (node != null) {
            last = node;
            int c = compare(key, node.key);

            if (c == 0) {
                splay(node);
                return node;
            }
            if (c < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (last != null) {
            splay(last);
        }
        return null;
    }

    private java.lang.String buildString(Node node) {
        if (node == null) {
            return "";
        }

        java.lang.String left = buildString(node.left);
        java.lang.String current = node.key + "=" + node.value;
        java.lang.String right = buildString(node.right);

        if (left.length() == 0) {
            if (right.length() == 0) {
                return current;
            }
            return current + ", " + right;
        }

        if (right.length() == 0) {
            return left + ", " + current;
        }

        return left + ", " + current + ", " + right;
    }

    private boolean hasValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        if (hasValue(node.left, value)) {
            return true;
        }
        return hasValue(node.right, value);
    }

    private void copyNodes(MySplayMap<Integer, String> target, Node node,
                           Object from, boolean fromInclusive,
                           Object to, boolean toInclusive) {
        if (node == null) {
            return;
        }

        copyNodes(target, node.left, from, fromInclusive, to, toInclusive);

        boolean okFrom = true;
        if (from != null) {
            int c = compare(node.key, from);
            if (fromInclusive) {
                okFrom = c >= 0;
            } else {
                okFrom = c > 0;
            }
        }

        boolean okTo = true;
        if (to != null) {
            int c = compare(node.key, to);
            if (toInclusive) {
                okTo = c <= 0;
            } else {
                okTo = c < 0;
            }
        }

        if (okFrom && okTo) {
            target.put(node.key, node.value);
        }

        copyNodes(target, node.right, from, fromInclusive, to, toInclusive);
    }

    private MySplayMap<Integer, String> copyRange(Object from, boolean fromInclusive,
                                                  Object to, boolean toInclusive) {
        MySplayMap<Integer, String> result = new MySplayMap<>();
        copyNodes(result, root, from, fromInclusive, to, toInclusive);
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "{" + buildString(root) + "}";
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        Node node = root;

        while (true) {
            int c = compare(key, node.key);

            if (c == 0) {
                String old = node.value;
                node.value = value;
                splay(node);
                return old;
            }

            if (c < 0) {
                if (node.left == null) {
                    Node inserted = new Node(key, value);
                    inserted.parent = node;
                    node.left = inserted;
                    size = size + 1;
                    splay(inserted);
                    return null;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    Node inserted = new Node(key, value);
                    inserted.parent = node;
                    node.right = inserted;
                    size = size + 1;
                    splay(inserted);
                    return null;
                }
                node = node.right;
            }
        }
    }

    @Override
    public String remove(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        String old = node.value;
        Node left = node.left;
        Node right = node.right;
        size = size - 1;

        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }

        if (left == null) {
            root = right;
            return old;
        }
        if (right == null) {
            root = left;
            return old;
        }

        Node max = left;
        while (max.right != null) {
            max = max.right;
        }

        root = left;
        splay(max);
        root.right = right;
        right.parent = root;
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = find(key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
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
        if (root == null) {
            return null;
        }

        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }

        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = root;
        Integer result = null;

        while (node != null) {
            if (compare(node.key, key) < 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = root;
        Integer result = null;

        while (node != null) {
            if (compare(node.key, key) <= 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = root;
        Integer result = null;

        while (node != null) {
            if (compare(node.key, key) >= 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = root;
        Integer result = null;

        while (node != null) {
            if (compare(node.key, key) > 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return copyRange(null, true, toKey, inclusive);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return copyRange(fromKey, inclusive, null, true);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return copyRange(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return copyRange(fromKey, fromInclusive, toKey, toInclusive);
    }

    // не обязательные

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }

    @Override
    public Map.Entry<Integer, String> firstEntry() { return null; }

    @Override
    public Map.Entry<Integer, String> lastEntry() { return null; }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() { return null; }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() { return null; }

    @Override
    public NavigableMap<Integer, String> descendingMap() { return null; }

    @Override
    public NavigableSet<Integer> navigableKeySet() { return null; }

    @Override
    public NavigableSet<Integer> descendingKeySet() { return null; }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Set<Integer> keySet() { return null; }

    @Override
    public Collection<String> values() { return null; }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { return null; }
}
