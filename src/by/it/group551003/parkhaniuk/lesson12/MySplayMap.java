package by.it.group551003.parkhaniuk.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    public static class Node {
        Integer key;
        String val;
        Node left, right, parent;

        Node(Integer key, String val, Node parent) {
            this.key = key;
            this.val = val;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        if (y != null) y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        if (y != null) y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node parent = x.parent;
            Node grandParent = parent.parent;

            if (grandParent == null) {
                if (x == parent.left) rotateRight(parent);
                else rotateLeft(parent);
            } else if (x == parent.left && parent == grandParent.left) {
                rotateRight(grandParent);
                rotateRight(parent);
            } else if (x == parent.right && parent == grandParent.right) {
                rotateLeft(grandParent);
                rotateLeft(parent);
            } else if (x == parent.left && parent == grandParent.right) {
                rotateRight(parent);
                rotateLeft(grandParent);
            } else {
                rotateLeft(parent);
                rotateRight(grandParent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node lastNonNull = null;

        while (current != null) {
            lastNonNull = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                splay(current);
                return current;
            }
        }

        if (lastNonNull != null) splay(lastNonNull);
        return null;
    }

    // --- Обязательные методы ---

    @Override
    public String put(Integer key, String val) {
        if (key == null) return null;

        if (root == null) {
            root = new Node(key, val, null);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                String oldVal = current.val;
                current.val = val;
                splay(current);
                return oldVal;
            }
        }

        Node newNode = new Node(key, val, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        Node node = findNode((Integer) key);
        if (node == null || !node.key.equals(key)) return null;

        String oldValue = node.val;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else {
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;

            Node maxLeft = root;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            splay(maxLeft);
            root.right = rightSubtree;
            if (rightSubtree != null) rightSubtree.parent = root;
        }

        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return (node != null && node.key.equals(key)) ? node.val : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node node = findNode((Integer) key);
        return node != null && node.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.val == null) || (value != null && value.equals(node.val))) {
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
    public Integer firstKey() {
        if (root == null) return null;
        Node p = root;
        while (p.left != null) p = p.left;
        splay(p);
        return p.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node p = root;
        while (p.right != null) p = p.right;
        splay(p);
        return p.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node current = root, result = null;
        while (current != null) {
            if (current.key.compareTo(key) < 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node current = root, result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp <= 0) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root, result = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp >= 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node current = root, result = null;
        while (current != null) {
            if (current.key.compareTo(key) > 0) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (result != null) {
            splay(result);
            return result.key;
        }
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        buildHeadMap(root, toKey, inclusive, subMap);
        return subMap;
    }

    private void buildHeadMap(Node node, Integer toKey, boolean inclusive, MySplayMap subMap) {
        if (node == null) return;
        buildHeadMap(node.left, toKey, inclusive, subMap);
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            subMap.put(node.key, node.val);
            buildHeadMap(node.right, toKey, inclusive, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        buildTailMap(root, fromKey, inclusive, subMap);
        return subMap;
    }

    private void buildTailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap subMap) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            buildTailMap(node.left, fromKey, inclusive, subMap);
            subMap.put(node.key, node.val);
        }
        buildTailMap(node.right, fromKey, inclusive, subMap);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }


    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.val).append(", ");
        inOrderTraversal(node.right, sb);
    }





    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
}