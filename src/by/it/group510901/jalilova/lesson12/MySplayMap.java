package by.it.group510901.jalilova.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    // Узел splay-дерева

    private static class Node {
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

    public MySplayMap() {
        root = null;
        size = 0;
    }

    // Вращения и splay

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
            if (x.parent.parent == null) { //Zig
                if (x == x.parent.left) rotateRight(x.parent); // x.parent спустился вправо
                else rotateLeft(x.parent);
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) { //Zig-Zig
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {//Zig-Zig зеркально
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) { //Zig-Zag
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else { //Zig-Zag зеркально
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    // Обязательные методы

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (key.compareTo(parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node n = findNode(key);
        if (n == null) return null;
        String old = n.value;
        Node leftTree = root.left;
        Node rightTree = root.right;
        if (leftTree != null) leftTree.parent = null;
        if (rightTree != null) rightTree.parent = null;
        if (leftTree == null) {
            root = rightTree;
        } else {
            Node max = leftTree;
            while (max.right != null) max = max.right;
            root = leftTree;
            splay(max);
            root.right = rightTree;
            if (rightTree != null) rightTree.parent = root;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Node n = findNode((Integer) keyObj);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
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

    // headMap / tailMap (обязательные, возвращают снимок)

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap res = new MySplayMap();
        collectHead(root, toKey, res);
        return res;
    }

    private void collectHead(Node node, Integer toKey, MySplayMap res) {
        if (node == null) return;
        collectHead(node.left, toKey, res);                       // левое поддерево
        if (node.key.compareTo(toKey) < 0) res.put(node.key, node.value);  // сам узел
        collectHead(node.right, toKey, res);                      // правое поддерево
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap res = new MySplayMap();
        collectTail(root, fromKey, res);
        return res;
    }

    private void collectTail(Node node, Integer fromKey, MySplayMap res) {
        if (node == null) return;
        collectTail(node.left, fromKey, res);
        if (node.key.compareTo(fromKey) >= 0) res.put(node.key, node.value);
        collectTail(node.right, fromKey, res);
    }

    // Ключевые запросы

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp <= 0) cur = cur.left;
            else { best = cur.key; cur = cur.right; }
        }
        return best;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.key;
            else if (cmp < 0) cur = cur.left;
            else { best = cur.key; cur = cur.right; }
        }
        return best;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.key;
            else if (cmp > 0) cur = cur.right;
            else { best = cur.key; cur = cur.left; }
        }
        return best;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp >= 0) cur = cur.right;
            else { best = cur.key; cur = cur.left; }
        }
        return best;
    }

    // toString (in-order, возрастание ключей)

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean[] first = {true};
        inOrder(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        inOrder(node.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;
        inOrder(node.right, sb, first);
    }

    // Заглушки (все остальные методы)

    @Override
    public Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException();
    }

    // SortedMap

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    // NavigableMap

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
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

    // Map

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
        throw new UnsupportedOperationException();
    }
}
