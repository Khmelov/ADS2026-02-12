package by.it.group510901.poltorak.lesson12;

import java.util.*;

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
    private int size = 0;

    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p != null ? p.parent : null;

        if (g != null) {
            if (p == g.left) {
                g.left = x;
            } else {
                g.right = x;
            }
        }
        x.parent = g;

        if (x == p.left) {
            p.left = x.right;
            if (x.right != null) {
                x.right.parent = p;
            }
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) {
                x.left.parent = p;
            }
            x.left = p;
        }
        p.parent = x;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                rotate(x);
            } else if ((p == g.left && x == p.left) || (p == g.right && x == p.right)) {
                rotate(p);
                rotate(x);
            } else {
                rotate(x);
                rotate(x);
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node last = null;
        while (current != null) {
            last = current;
            int cmp = key.compareTo(current.key);
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
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть null");
        }
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;

        String removedValue = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            Node leftSubTree = node.left;
            leftSubTree.parent = null;
            node.left = null;

            Node rightSubTree = node.right;
            rightSubTree.parent = null;
            node.right = null;

            Node maxLeft = leftSubTree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft);
            maxLeft.right = rightSubTree;
            if (rightSubTree != null) rightSubTree.parent = maxLeft;
            root = maxLeft;
        }
        size--;
        return removedValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.key;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (candidate != null) splay(candidate);
        return candidate != null ? candidate.key : null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp >= 0) {
                candidate = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (candidate != null) splay(candidate);
        return candidate != null ? candidate.key : null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (candidate != null) splay(candidate);
        return candidate != null ? candidate.key : null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node current = root;
        Node candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (candidate != null) splay(candidate);
        return candidate != null ? candidate.key : null;
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap sub = new MySplayMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node node, Integer toKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            sub.put(node.key, node.value);
            fillHeadMap(node.left, toKey, sub);
            fillHeadMap(node.right, toKey, sub);
        } else {
            fillHeadMap(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap sub = new MySplayMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node node, Integer fromKey, MySplayMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            sub.put(node.key, node.value);
            fillTailMap(node.left, fromKey, sub);
            fillTailMap(node.right, fromKey, sub);
        } else {
            fillTailMap(node.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendToString(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void appendToString(Node node, StringBuilder sb) {
        if (node == null) return;
        appendToString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append('=').append(node.value);
        appendToString(node.right, sb);
    }
}