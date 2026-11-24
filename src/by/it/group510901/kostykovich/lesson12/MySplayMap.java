package by.it.group510901.kostykovich.lesson12;

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

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int currentSize;

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        String res = inOrderToString(root);
        return "{" + res.substring(0, res.length() - 2) + "}";
    }

    private String inOrderToString(Node node) {
        if (node == null) {
            return "";
        }
        return inOrderToString(node.left)
                + node.key + "=" + node.value + ", "
                + inOrderToString(node.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        int k = key;
        if (root == null) {
            root = new Node(k, value, null);
            currentSize = 1;
            return null;
        }

        Node curr = root;
        Node parent = null;
        while (curr != null) {
            parent = curr;
            if (k < curr.key) {
                curr = curr.left;
            } else if (k > curr.key) {
                curr = curr.right;
            } else {
                String oldValue = curr.value;
                curr.value = value;
                splay(curr);
                return oldValue;
            }
        }

        Node newNode = new Node(k, value, parent);
        if (k < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode);
        currentSize++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node target = findNode((Integer) key);
        if (target == null) {
            return null;
        }
        splay(target);
        String oldValue = target.value;

        if (target.left == null) {
            root = target.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node rightSubtree = target.right;
            Node leftSubtree = target.left;
            leftSubtree.parent = null;

            Node maxLeft = leftSubtree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            root = leftSubtree;
            splay(maxLeft);

            root.right = rightSubtree;
            if (rightSubtree != null) {
                rightSubtree.parent = root;
            }
        }
        currentSize--;
        return oldValue;
    }

    public String remove(Integer key) {
        return remove((Object) key);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node target = findNode((Integer) key);
        if (target == null) {
            return null;
        }
        splay(target);
        return target.value;
    }

    public String get(Integer key) {
        return get((Object) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        Node target = findNode((Integer) key);
        if (target == null) {
            return false;
        }
        splay(target);
        return true;
    }

    public boolean containsKey(Integer key) {
        return containsKey((Object) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInSubtree(root, value);
    }

    public boolean containsValue(String value) {
        return containsValue((Object) value);
    }

    private boolean containsValueInSubtree(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueInSubtree(node.left, value) || containsValueInSubtree(node.right, value);
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        root = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MySplayMap subMap = new MySplayMap();
        fillHeadMap(root, toKey, subMap);
        return subMap;
    }

    private void fillHeadMap(Node node, int toKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key < toKey) {
            fillHeadMap(node.left, toKey, subMap);
            subMap.put(node.key, node.value);
            fillHeadMap(node.right, toKey, subMap);
        } else {
            fillHeadMap(node.left, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MySplayMap subMap = new MySplayMap();
        fillTailMap(root, fromKey, subMap);
        return subMap;
    }

    private void fillTailMap(Node node, int fromKey, MySplayMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            fillTailMap(node.left, fromKey, subMap);
            subMap.put(node.key, node.value);
            fillTailMap(node.right, fromKey, subMap);
        } else {
            fillTailMap(node.right, fromKey, subMap);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        splay(curr);
        return curr.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node candidate = null;
        while (curr != null) {
            if (curr.key < key) {
                candidate = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node candidate = null;
        while (curr != null) {
            if (curr.key == key) {
                splay(curr);
                return curr.key;
            }
            if (curr.key < key) {
                candidate = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node candidate = null;
        while (curr != null) {
            if (curr.key == key) {
                splay(curr);
                return curr.key;
            }
            if (curr.key > key) {
                candidate = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        Node candidate = null;
        while (curr != null) {
            if (curr.key > key) {
                candidate = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }
        return null;
    }

    private Node findNode(int key) {
        Node curr = root;
        while (curr != null) {
            if (key < curr.key) {
                curr = curr.left;
            } else if (key > curr.key) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) {
            return;
        }
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
        if (y == null) {
            return;
        }
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
    private void splay(Node x) {
        if (x == null) {
            return;
        }
        while (x.parent != null) {
            Node parent = x.parent;
            Node grandParent = parent.parent;
            if (grandParent == null) {
                if (x == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (x == parent.left && parent == grandParent.left) {
                rotateRight(grandParent);
                rotateRight(parent);
            } else if (x == parent.right && parent == grandParent.right) {
                rotateLeft(grandParent);
                rotateLeft(parent);
            } else if (x == parent.right && parent == grandParent.left) {
                rotateLeft(parent);
                rotateRight(grandParent);
            } else {
                rotateRight(parent);
                rotateLeft(grandParent);
            }
        }
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
    public NavigableSet navigableKeySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public NavigableSet descendingKeySet() {
        throw new UnsupportedOperationException();
    }
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
    public Comparator<? super Integer> comparator() {
        return null;
    }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Collection values() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}