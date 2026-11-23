package by.it.group551001.docenko.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    private static Integer toKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (Integer) key;
    }

    private static Node rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        pivot.right = node;
        return pivot;
    }

    private static Node rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        return pivot;
    }

    private static Node splay(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) {
                return node;
            }
            int cmpChild = key.compareTo(node.left.key);
            if (cmpChild < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmpChild > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return node.left == null ? node : rotateRight(node);
        }
        if (cmp > 0) {
            if (node.right == null) {
                return node;
            }
            int cmpChild = key.compareTo(node.right.key);
            if (cmpChild > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            } else if (cmpChild < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }
            return node.right == null ? node : rotateLeft(node);
        }
        return node;
    }

    private Node access(Integer key) {
        root = splay(root, key);
        return root != null && root.key.equals(key) ? root : null;
    }

    private Node splayFound(Node found) {
        if (found != null) {
            root = splay(root, found.key);
        }
        return found;
    }

    private Node minNode() {
        Node node = root;
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return splayFound(node);
    }

    private Node maxNode() {
        Node node = root;
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return splayFound(node);
    }

    private Node lowerNode(Integer key, boolean inclusive) {
        Node current = root;
        Node best = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp < 0 || inclusive && cmp == 0) {
                best = current;
                if (cmp == 0) {
                    break;
                }
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return splayFound(best);
    }

    private Node higherNode(Integer key, boolean inclusive) {
        Node current = root;
        Node best = null;
        while (current != null) {
            int cmp = current.key.compareTo(key);
            if (cmp > 0 || inclusive && cmp == 0) {
                best = current;
                if (cmp == 0) {
                    break;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return splayFound(best);
    }

    private static Integer keyOf(Node node) {
        return node == null ? null : node.key;
    }

    private static Node snapshot(Node node) {
        return node == null ? null : new Node(node.key, node.value);
    }

    private static void appendInOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendInOrder(node.right, sb);
    }

    private static boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private static boolean matches(Node node, Map<?, ?> other) {
        if (node == null) {
            return true;
        }
        Object otherValue = other.get(node.key);
        boolean same = node.value == null
                ? otherValue == null && other.containsKey(node.key)
                : node.value.equals(otherValue);
        return same && matches(node.left, other) && matches(node.right, other);
    }

    private static int hash(Node node) {
        if (node == null) {
            return 0;
        }
        int h = node.key.hashCode() ^ (node.value == null ? 0 : node.value.hashCode());
        return h + hash(node.left) + hash(node.right);
    }

    private static void copyRange(Node node, MySplayMap target,
                                  Integer from, boolean fromInclusive,
                                  Integer to, boolean toInclusive) {
        if (node == null) {
            return;
        }
        int cmpFrom = from == null ? 1 : node.key.compareTo(from);
        int cmpTo = to == null ? -1 : node.key.compareTo(to);
        boolean aboveFrom = cmpFrom > 0 || fromInclusive && cmpFrom == 0;
        boolean belowTo = cmpTo < 0 || toInclusive && cmpTo == 0;
        if (cmpFrom > 0) {
            copyRange(node.left, target, from, fromInclusive, to, toInclusive);
        }
        if (aboveFrom && belowTo) {
            target.put(node.key, node.value);
        }
        if (cmpTo < 0) {
            copyRange(node.right, target, from, fromInclusive, to, toInclusive);
        }
    }

    private MySplayMap range(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        copyRange(root, result, from, fromInclusive, to, toInclusive);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    @Override
    public String put(Integer key, String value) {
        toKey(key);
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            return root.setValue(value);
        }
        Node node = new Node(key, value);
        if (cmp < 0) {
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
    public String remove(Object key) {
        Node node = access(toKey(key));
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            root = node.right;
        } else {
            Node right = node.right;
            root = splay(node.left, node.key);
            root.right = right;
        }
        size--;
        return node.value;
    }

    @Override
    public String get(Object key) {
        Node node = access(toKey(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return access(toKey(key)) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(null, false, toKey(toKey), inclusive);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(toKey(fromKey), inclusive, null, false);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        if (toKey(fromKey).compareTo(toKey(toKey)) > 0) {
            throw new IllegalArgumentException();
        }
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public Integer firstKey() {
        Node node = minNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        Node node = maxNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyOf(lowerNode(toKey(key), false));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyOf(lowerNode(toKey(key), true));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyOf(higherNode(toKey(key), true));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyOf(higherNode(toKey(key), false));
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return snapshot(lowerNode(toKey(key), false));
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return snapshot(lowerNode(toKey(key), true));
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return snapshot(higherNode(toKey(key), true));
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return snapshot(higherNode(toKey(key), false));
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return snapshot(minNode());
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return snapshot(maxNode());
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Node node = snapshot(minNode());
        if (node != null) {
            remove(node.key);
        }
        return node;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Node node = snapshot(maxNode());
        if (node != null) {
            remove(node.key);
        }
        return node;
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> other = (Map<?, ?>) o;
        return other.size() == size && matches(root, other);
    }

    @Override
    public int hashCode() {
        return hash(root);
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
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
