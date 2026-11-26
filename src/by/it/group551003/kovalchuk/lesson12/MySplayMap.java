package by.it.group551003.kovalchuk.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private Node root;
    private int size;

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

    private void rotateLeft(Node x) {
        Node y = x.right;

        x.right = y.left;

        if (y.left != null)
            y.left.parent = x;

        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;

        x.left = y.right;

        if (y.right != null)
            y.right.parent = x;

        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;

        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {

        while (x.parent != null) {

            if (x.parent.parent == null) {

                if (x == x.parent.left)
                    rotateRight(x.parent);
                else
                    rotateLeft(x.parent);

            }
            else if (x == x.parent.left &&
                    x.parent == x.parent.parent.left) {

                rotateRight(x.parent.parent);
                rotateRight(x.parent);

            }
            else if (x == x.parent.right &&
                    x.parent == x.parent.parent.right) {

                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);

            }
            else if (x == x.parent.right &&
                    x.parent == x.parent.parent.left) {

                rotateLeft(x.parent);
                rotateRight(x.parent);

            }
            else {

                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }

        root = x;
    }

    private Node findNode(Integer key) {

        Node current = root;

        while (current != null) {

            int cmp = key.compareTo(current.key);

            if (cmp < 0)
                current = current.left;
            else if (cmp > 0)
                current = current.right;
            else {
                splay(current);
                return current;
            }
        }

        return null;
    }

    @Override
    public String put(Integer key, String value) {

        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {

            parent = current;

            int cmp = key.compareTo(current.key);

            if (cmp < 0)
                current = current.left;
            else if (cmp > 0)
                current = current.right;
            else {

                String old = current.value;
                current.value = value;
                splay(current);

                return old;
            }
        }

        Node node = new Node(key, value);
        node.parent = parent;

        if (key < parent.key)
            parent.left = node;
        else
            parent.right = node;

        splay(node);
        size++;

        return null;
    }

    @Override
    public String get(Object key) {

        Node node = findNode((Integer) key);

        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    private boolean containsValue(Node node, Object value) {

        if (node == null)
            return false;

        if (value == null) {
            if (node.value == null)
                return true;
        } else {
            if (value.equals(node.value))
                return true;
        }

        return containsValue(node.left, value)
                || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private Node maximum(Node node) {

        while (node.right != null)
            node = node.right;

        return node;
    }

    @Override
    public String remove(Object key) {

        Node node = findNode((Integer) key);

        if (node == null)
            return null;

        String old = node.value;

        splay(node);

        if (root.left == null) {

            root = root.right;

            if (root != null)
                root.parent = null;

        } else {

            Node rightSubtree = root.right;

            root = root.left;
            root.parent = null;

            Node max = maximum(root);

            splay(max);

            root.right = rightSubtree;

            if (rightSubtree != null)
                rightSubtree.parent = root;
        }

        size--;

        return old;
    }

    private void inorder(Node node, StringBuilder sb) {

        if (node == null)
            return;

        inorder(node.left, sb);

        if (sb.length() > 1)
            sb.append(", ");

        sb.append(node.key)
                .append("=")
                .append(node.value);

        inorder(node.right, sb);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("{");

        inorder(root, sb);

        sb.append("}");

        return sb.toString();
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

        if (root == null)
            return null;

        Node cur = root;

        while (cur.left != null)
            cur = cur.left;

        splay(cur);

        return cur.key;
    }

    @Override
    public Integer lastKey() {

        if (root == null)
            return null;

        Node cur = root;

        while (cur.right != null)
            cur = cur.right;

        splay(cur);

        return cur.key;
    }

    private void fillHead(Node node,
                          Integer toKey,
                          MySplayMap map) {

        if (node == null)
            return;

        fillHead(node.left, toKey, map);

        if (node.key < toKey)
            map.put(node.key, node.value);

        fillHead(node.right, toKey, map);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {

        MySplayMap map = new MySplayMap();

        fillHead(root, toKey, map);

        return map;
    }

    private void fillTail(Node node,
                          Integer fromKey,
                          MySplayMap map) {

        if (node == null)
            return;

        fillTail(node.left, fromKey, map);

        if (node.key >= fromKey)
            map.put(node.key, node.value);

        fillTail(node.right, fromKey, map);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {

        MySplayMap map = new MySplayMap();

        fillTail(root, fromKey, map);

        return map;
    }

    @Override
    public Integer lowerKey(Integer key) {

        Node cur = root;
        Integer result = null;

        while (cur != null) {

            if (cur.key < key) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }

        return result;
    }

    @Override
    public Integer floorKey(Integer key) {

        Node cur = root;
        Integer result = null;

        while (cur != null) {

            if (cur.key.equals(key))
                return key;

            if (cur.key < key) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }

        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {

        Node cur = root;
        Integer result = null;

        while (cur != null) {

            if (cur.key.equals(key))
                return key;

            if (cur.key > key) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        return result;
    }

    @Override
    public Integer higherKey(Integer key) {

        Node cur = root;
        Integer result = null;

        while (cur != null) {

            if (cur.key > key) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        return result;
    }

    // ---------- SortedMap ----------

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey,
                                             Integer toKey) {
        return null;
    }

    // ---------- Map ----------

    @Override
    public void putAll(Map<? extends Integer,
            ? extends String> m) {
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
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    // ---------- NavigableMap ----------

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
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
    public NavigableMap<Integer, String> subMap(
            Integer fromKey,
            boolean fromInclusive,
            Integer toKey,
            boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(
            Integer toKey,
            boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(
            Integer fromKey,
            boolean inclusive) {
        return null;
    }
}