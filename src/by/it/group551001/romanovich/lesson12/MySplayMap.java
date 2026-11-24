package by.it.group551001.romanovich.lesson12;

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
    private static final class Node {
        final int key;
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

    private record Snapshot(Integer key, String value) implements Entry<Integer, String> {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Entry<?, ?> entry
                    && Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    private static int key(Object key) {
        return (Integer) Objects.requireNonNull(key);
    }

    private void rotate(Node node) {
        Node parent = node.parent;
        Node grandparent = parent.parent;
        if (node == parent.left) {
            parent.left = node.right;
            if (node.right != null) {
                node.right.parent = parent;
            }
            node.right = parent;
        } else {
            parent.right = node.left;
            if (node.left != null) {
                node.left.parent = parent;
            }
            node.left = parent;
        }
        parent.parent = node;
        node.parent = grandparent;
        if (grandparent == null) {
            root = node;
        } else if (grandparent.left == parent) {
            grandparent.left = node;
        } else {
            grandparent.right = node;
        }
    }

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
            if (grandparent == null) {
                rotate(node);
            } else if ((node == parent.left) == (parent == grandparent.left)) {
                rotate(parent);
                rotate(node);
            } else {
                rotate(node);
                rotate(node);
            }
        }
    }

    private Node find(int key) {
        Node node = root;
        Node last = null;
        while (node != null) {
            last = node;
            if (key == node.key) {
                splay(node);
                return node;
            }
            node = key < node.key ? node.left : node.right;
        }
        if (last != null) {
            splay(last);
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        int k = key(key);
        Node node = root;
        Node parent = null;
        while (node != null) {
            parent = node;
            if (k == node.key) {
                String previous = node.value;
                node.value = value;
                splay(node);
                return previous;
            }
            node = k < node.key ? node.left : node.right;
        }
        Node added = new Node(k, value, parent);
        if (parent == null) {
            root = added;
        } else if (k < parent.key) {
            parent.left = added;
        } else {
            parent.right = added;
        }
        size++;
        splay(added);
        return null;
    }

    @Override
    public String get(Object key) {
        Node node = find(key(key));
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key(key)) != null;
    }

    @Override
    public String remove(Object key) {
        Node node = find(key(key));
        if (node == null) {
            return null;
        }
        String previous = node.value;
        Node left = root.left;
        Node right = root.right;
        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }
        root = left;
        if (left == null) {
            root = right;
        } else {
            Node largest = left;
            while (largest.right != null) {
                largest = largest.right;
            }
            splay(largest);
            root.right = right;
            if (right != null) {
                right.parent = root;
            }
        }
        size--;
        return previous;
    }

    private static Node minimum(Node node) {
        if (node != null) {
            while (node.left != null) {
                node = node.left;
            }
        }
        return node;
    }

    private static Node successor(Node node) {
        if (node.right != null) {
            return minimum(node.right);
        }
        Node parent = node.parent;
        while (parent != null && node == parent.right) {
            node = parent;
            parent = parent.parent;
        }
        return parent;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node node = minimum(root); node != null; node = successor(node)) {
            if (Objects.equals(value, node.value)) {
                splay(node);
                return true;
            }
        }
        return false;
    }

    private Node boundary(int key, boolean lower, boolean inclusive) {
        Node node = root;
        Node best = null;
        Node last = null;
        while (node != null) {
            last = node;
            if (node.key == key && inclusive) {
                best = node;
                break;
            }
            if (lower ? node.key < key : node.key > key) {
                best = node;
                node = lower ? node.right : node.left;
            } else {
                node = lower ? node.left : node.right;
            }
        }
        if (best != null || last != null) {
            splay(best != null ? best : last);
        }
        return best;
    }

    private static Integer nodeKey(Node node) {
        return node == null ? null : node.key;
    }

    private static Entry<Integer, String> entry(Node node) {
        return node == null ? null : new Snapshot(node.key, node.value);
    }

    @Override
    public Integer lowerKey(Integer key) {
        return nodeKey(boundary(key(key), true, false));
    }

    @Override
    public Integer floorKey(Integer key) {
        return nodeKey(boundary(key(key), true, true));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return nodeKey(boundary(key(key), false, true));
    }

    @Override
    public Integer higherKey(Integer key) {
        return nodeKey(boundary(key(key), false, false));
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return entry(boundary(key(key), true, false));
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return entry(boundary(key(key), true, true));
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return entry(boundary(key(key), false, true));
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return entry(boundary(key(key), false, false));
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        Node node = minimum(root);
        if (node != null) {
            splay(node);
        }
        return entry(node);
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        Node node = root;
        if (node != null) {
            while (node.right != null) {
                node = node.right;
            }
            splay(node);
        }
        return entry(node);
    }

    @Override
    public Integer firstKey() {
        Entry<Integer, String> first = firstEntry();
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.getKey();
    }

    @Override
    public Integer lastKey() {
        Entry<Integer, String> last = lastEntry();
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.getKey();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        Entry<Integer, String> first = firstEntry();
        if (first != null) {
            remove(first.getKey());
        }
        return first;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        Entry<Integer, String> last = lastEntry();
        if (last != null) {
            remove(last.getKey());
        }
        return last;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return range(null, false, key(toKey), inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return range(key(fromKey), inclusive, null, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                               Integer toKey, boolean toInclusive) {
        int from = key(fromKey);
        int to = key(toKey);
        if (from > to) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        return range(from, fromInclusive, to, toInclusive);
    }

    private MySplayMap range(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        for (Node node = minimum(root); node != null; node = successor(node)) {
            if (to != null && (node.key > to || node.key == to && !toInclusive)) {
                break;
            }
            if (from == null || node.key > from || node.key == from && fromInclusive) {
                result.put(node.key, node.value);
            }
        }
        return result;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (Node node = minimum(root); node != null; node = successor(node)) {
            if (result.length() > 1) {
                result.append(", ");
            }
            result.append(node.key).append('=').append(node.value);
        }
        return result.append('}').toString();
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
}
