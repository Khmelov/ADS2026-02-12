package by.it.group510902.zenkovich.lesson12;

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

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;
    private final Comparator<? super Integer> comparator;

    public MySplayMap() {
        this.root = null;
        this.size = 0;
        this.comparator = null;
    }

    public MySplayMap(Comparator<? super Integer> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    private int compare(Integer k1, Integer k2) {
        return comparator == null ? k1.compareTo(k2) : comparator.compare(k1, k2);
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        if (y != null) {
            y.left = x;
        }
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        if (y != null) {
            y.right = x;
        }
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x == x.parent.left) {
                    rotateRight(x.parent);
                } else {
                    rotateLeft(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private Node search(Integer key) {
        Node current = root;
        Node last = null;
        while (current != null) {
            last = current;
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(node.key).append("=").append(node.value);
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node current = root;
        Node parent = null;
        while (current != null) {
            parent = current;
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (compare(key, parent.key) < 0) {
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
        if (!(key instanceof Integer)) return null;
        Node node = search((Integer) key);
        if (node == null) return null;
        String oldValue = node.value;

        splay(node);

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            Node leftTree = node.left;
            Node rightTree = node.right;

            leftTree.parent = null;
            rightTree.parent = null;

            Node maxLeft = leftTree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            root = leftTree;
            Node current = root;
            while (current.right != null) {
                current = current.right;
            }
            current.right = rightTree;
            rightTree.parent = current;
        }
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = search((Integer) key);
        return node == null ? null : node.value;
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
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
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
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap map = new MySplayMap(comparator);
        headMap(root, toKey, inclusive, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        int cmp = compare(node.key, toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            headMap(node.left, toKey, inclusive, map);
            map.put(node.key, node.value);
            headMap(node.right, toKey, inclusive, map);
        } else {
            headMap(node.left, toKey, inclusive, map);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap map = new MySplayMap(comparator);
        tailMap(root, fromKey, inclusive, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap map) {
        if (node == null) return;
        int cmp = compare(node.key, fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            tailMap(node.left, fromKey, inclusive, map);
            map.put(node.key, node.value);
            tailMap(node.right, fromKey, inclusive, map);
        } else {
            tailMap(node.right, fromKey, inclusive, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) current = current.left;
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) current = current.right;
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp > 0) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp > 0) {
                result = current.key;
                current = current.right;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                return current.key;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                result = current.key;
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.key;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return comparator;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
}