package by.it.group551003.kakhno.lesson12;


import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private Node rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        leftChild.right = node;
        return leftChild;
    }

    private Node rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        rightChild.left = node;
        return rightChild;
    }

    private Node splay(Node node, Integer key) {
        if (node == null || node.key.equals(key)) {
            return node;
        }

        if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                return node;
            }

            if (key.compareTo(node.left.key) < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (key.compareTo(node.left.key) > 0) {
                node.left.right = splay(node.left.right, key);

                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }

            if (node.left == null) {
                return node;
            }

            return rotateRight(node);
        } else {
            if (node.right == null) {
                return node;
            }

            if (key.compareTo(node.right.key) > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (key.compareTo(node.right.key) < 0) {
                node.right.left = splay(node.right.left, key);

                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }

            if (node.right == null) {
                return node;
            }

            return rotateLeft(node);
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);

        int comparison = key.compareTo(root.key);

        if (comparison == 0) {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }

        Node node = new Node(key, value);

        if (comparison < 0) {
            node.left = root.left;
            node.right = root;
            root.left = null;
        } else {
            node.right = root.right;
            node.left = root;
            root.right = null;
        }

        root = node;
        size++;

        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }

        Integer integerKey = (Integer) key;
        root = splay(root, integerKey);

        if (root.key.equals(integerKey)) {
            return root.value;
        }

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return false;
        }

        Integer integerKey = (Integer) key;
        root = splay(root, integerKey);

        return root.key.equals(integerKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueNode(root, value);
    }

    private boolean containsValueNode(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValueNode(node.left, value)
                || containsValueNode(node.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }

        Integer integerKey = (Integer) key;
        root = splay(root, integerKey);

        if (!root.key.equals(integerKey)) {
            return null;
        }

        String oldValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSubtree = root.right;
            root = splay(root.left, integerKey);
            root.right = rightSubtree;
        }

        size--;

        return oldValue;
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

    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }

        Node node = minimum(root);
        Integer key = node.key;
        root = splay(root, key);

        return key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }

        Node node = maximum(root);
        Integer key = node.key;
        root = splay(root, key);

        return key;
    }


    private Integer findBound(Integer key, int mode) {
        if (key == null) {
            throw new NullPointerException();
        }

        Node current = root;
        Integer candidate = null;

        while (current != null) {
            int comparison = current.key.compareTo(key);

            if (mode == 0) {
                if (comparison < 0) {
                    candidate = current.key;
                    current = current.right;
                } else {
                    current = current.left;
                }
            } else if (mode == 1) {
                if (comparison <= 0) {
                    candidate = current.key;
                    current = current.right;
                } else {
                    current = current.left;
                }
            } else if (mode == 2) {
                if (comparison >= 0) {
                    candidate = current.key;
                    current = current.left;
                } else {
                    current = current.right;
                }
            } else {
                if (comparison > 0) {
                    candidate = current.key;
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }

        return candidate;
    }


    @Override
    public Integer lowerKey(Integer key) {
        return findBound(key, 0);
    }

    @Override
    public Integer floorKey(Integer key) {
        return findBound(key, 1);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findBound(key, 2);
    }

    @Override
    public Integer higherKey(Integer key) {
        return findBound(key, 3);
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
    public MySplayMap headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        copyHead(root, toKey, result);
        return result;
    }

    private void copyHead(Node node, Integer toKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        copyHead(node.left, toKey, result);

        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            copyHead(node.right, toKey, result);
        }
    }

    @Override
    public MySplayMap tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        copyTail(root, fromKey, result);
        return result;
    }

    private void copyTail(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            copyTail(node.left, fromKey, result);
            result.put(node.key, node.value);
            copyTail(node.right, fromKey, result);
        } else {
            copyTail(node.right, fromKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(
            Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(
            Integer fromKey, boolean fromInclusive,
            Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }

        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException();
        }

        MySplayMap result = new MySplayMap();
        copyRange(root, fromKey, fromInclusive, toKey, toInclusive, result);

        return result;
    }

    private void copyRange(Node node, Integer fromKey, boolean fromInclusive,
                           Integer toKey, boolean toInclusive,
                           MySplayMap result) {
        if (node == null) {
            return;
        }

        copyRange(node.left, fromKey, fromInclusive,
                toKey, toInclusive, result);

        int fromComparison = node.key.compareTo(fromKey);
        int toComparison = node.key.compareTo(toKey);

        if ((fromComparison > 0 || (fromInclusive && fromComparison == 0))
                && (toComparison < 0 || (toInclusive && toComparison == 0))) {
            result.put(node.key, node.value);
        }

        copyRange(node.right, fromKey, fromInclusive,
                toKey, toInclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> headMap(
            Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyHeadInclusive(root, toKey, inclusive, result);
        return result;
    }

    private void copyHeadInclusive(Node node, Integer toKey,
                                   boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        copyHeadInclusive(node.left, toKey, inclusive, result);

        int comparison = node.key.compareTo(toKey);

        if (comparison < 0 || (inclusive && comparison == 0)) {
            result.put(node.key, node.value);
            copyHeadInclusive(node.right, toKey, inclusive, result);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(
            Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        copyTailInclusive(root, fromKey, inclusive, result);
        return result;
    }

    private void copyTailInclusive(Node node, Integer fromKey,
                                   boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }

        int comparison = node.key.compareTo(fromKey);

        if (comparison > 0 || (inclusive && comparison == 0)) {
            copyTailInclusive(node.left, fromKey, inclusive, result);
            result.put(node.key, node.value);
            copyTailInclusive(node.right, fromKey, inclusive, result);
        } else {
            copyTailInclusive(node.right, fromKey, inclusive, result);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result);
        result.append("}");
        return result.toString();
    }

    private void appendInOrder(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }

        appendInOrder(node.left, result);

        if (result.length() > 1) {
            result.append(", ");
        }

        result.append(node.key).append("=").append(node.value);

        appendInOrder(node.right, result);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry
                : map.entrySet()) {
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
}

