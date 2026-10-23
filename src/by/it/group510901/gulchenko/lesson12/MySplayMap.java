package by.it.group510901.gulchenko.lesson12;

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

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    private void rotateLeft(Node node) {
        Node newRoot = node.right;

        node.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.parent = node;
        }

        newRoot.parent = node.parent;

        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.left) {
            node.parent.left = newRoot;
        } else {
            node.parent.right = newRoot;
        }

        newRoot.left = node;
        node.parent = newRoot;
    }

    private void rotateRight(Node node) {
        Node newRoot = node.left;

        node.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.parent = node;
        }

        newRoot.parent = node.parent;

        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.left) {
            node.parent.left = newRoot;
        } else {
            node.parent.right = newRoot;
        }

        newRoot.right = node;
        node.parent = newRoot;
    }

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node lastVisited = null;

        while (current != null) {
            lastVisited = current;
            int comparison = key.compareTo(current.key);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (lastVisited != null) {
            splay(lastVisited);
        }

        return null;
    }

    private Node nextNode(Node node) {
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    private Node minNode() {
        Node node = root;

        if (node != null) {
            while (node.left != null) {
                node = node.left;
            }
        }

        return node;
    }

    private Integer findBoundary(Integer key, boolean lower, boolean inclusive) {
        if (key == null) {
            throw new NullPointerException();
        }

        Node current = root;
        Node candidate = null;
        Node lastVisited = null;

        while (current != null) {
            lastVisited = current;
            int comparison = current.key.compareTo(key);

            if (comparison == 0 && inclusive) {
                candidate = current;
                break;
            }

            if (lower) {
                if (comparison < 0) {
                    candidate = current;
                    current = current.right;
                } else {
                    current = current.left;
                }
            } else {
                if (comparison > 0) {
                    candidate = current;
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }

        if (candidate != null) {
            splay(candidate);
            return candidate.key;
        }

        if (lastVisited != null) {
            splay(lastVisited);
        }

        return null;
    }

    private MySplayMap copyRange(
            Integer fromKey, boolean fromInclusive,
            Integer toKey, boolean toInclusive) {

        MySplayMap result = new MySplayMap();
        Node current = minNode();

        while (current != null) {
            boolean afterStart = fromKey == null
                    || current.key.compareTo(fromKey) > 0
                    || (fromInclusive && current.key.compareTo(fromKey) == 0);

            boolean beforeEnd = toKey == null
                    || current.key.compareTo(toKey) < 0
                    || (toInclusive && current.key.compareTo(toKey) == 0);

            if (!beforeEnd) {
                break;
            }

            if (afterStart) {
                result.put(current.key, current.value);
            }

            current = nextNode(current);
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        String result = "{";
        Node current = minNode();
        boolean first = true;

        while (current != null) {
            if (!first) {
                result += ", ";
            }

            result += current.key + "=" + current.value;
            first = false;
            current = nextNode(current);
        }

        return result + "}";
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node current = root;

        while (true) {
            int comparison = key.compareTo(current.key);

            if (comparison == 0) {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }

            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node(key, value, current);
                    splay(current.left);
                    break;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(key, value, current);
                    splay(current.right);
                    break;
                }
                current = current.right;
            }
        }

        size++;
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

        Node node = findNode((Integer) key);

        if (node == null) {
            return null;
        }

        String oldValue = node.value;
        Node left = node.left;
        Node right = node.right;

        if (left != null) {
            left.parent = null;
        }
        if (right != null) {
            right.parent = null;
        }

        node.left = null;
        node.right = null;

        if (left == null) {
            root = right;
        } else {
            root = left;
            Node maximum = left;

            while (maximum.right != null) {
                maximum = maximum.right;
            }

            splay(maximum);
            root.right = right;

            if (right != null) {
                right.parent = root;
            }
        }

        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        return key instanceof Integer && findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node current = minNode();

        while (current != null) {
            if (value == null ? current.value == null : value.equals(current.value)) {
                splay(current);
                return true;
            }
            current = nextNode(current);
        }

        return false;
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
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        Node node = minNode();

        if (node == null) {
            throw new NoSuchElementException();
        }

        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }

        Node node = root;

        while (node.right != null) {
            node = node.right;
        }

        splay(node);
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return findBoundary(key, true, false);
    }

    @Override
    public Integer floorKey(Integer key) {
        return findBoundary(key, true, true);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return findBoundary(key, false, true);
    }

    @Override
    public Integer higherKey(Integer key) {
        return findBoundary(key, false, false);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы интерфейса                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(
            Integer toKey, boolean inclusive) {
        if (toKey == null) {
            throw new NullPointerException();
        }

        return copyRange(null, false, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(
            Integer fromKey, boolean inclusive) {
        if (fromKey == null) {
            throw new NullPointerException();
        }

        return copyRange(fromKey, inclusive, null, false);
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

        return copyRange(fromKey, fromInclusive, toKey, toInclusive);
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
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}