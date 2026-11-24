package by.it.group551002.yermakou.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;

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

    private static class TreeHolder {
        Node root;
        int size = 0;
    }

    private final TreeHolder holder;
    private final Integer fromKey;
    private final Integer toKey;
    private final boolean fromInclusive;
    private final boolean toInclusive;

    public MySplayMap() {
        this.holder = new TreeHolder();
        this.fromKey = null;
        this.toKey = null;
        this.fromInclusive = true;
        this.toInclusive = false;
    }

    private MySplayMap(TreeHolder holder, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        this.holder = holder;
        this.fromKey = fromKey;
        this.fromInclusive = fromInclusive;
        this.toKey = toKey;
        this.toInclusive = toInclusive;
    }

    private boolean inRange(Integer key) {
        if (fromKey != null) {
            int cmp = key.compareTo(fromKey);
            if (fromInclusive ? cmp < 0 : cmp <= 0) return false;
        }
        if (toKey != null) {
            int cmp = key.compareTo(toKey);
            if (toInclusive ? cmp > 0 : cmp >= 0) return false;
        }
        return true;
    }

    private void checkKeyRange(Integer key) {
        if (!inRange(key)) {
            throw new IllegalArgumentException("Key out of range");
        }
    }

    // =========================================================================
    // Splay Операции
    // =========================================================================

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == null) {
                holder.root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == null) {
                holder.root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    private Node findNode(Integer key) {
        Node current = holder.root;
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

    // =========================================================================
    // Обязательные к реализации методы
    // =========================================================================

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        checkKeyRange(key);

        if (holder.root == null) {
            holder.root = new Node(key, value);
            holder.size = 1;
            return null;
        }

        Node current = holder.root;
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
                String oldVal = current.value;
                current.value = value;
                return oldVal;
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
        holder.size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (!inRange(key)) return null;

        Node node = findNode(key);
        if (node == null) return null;

        String oldValue = node.value;

        if (node.left == null) {
            holder.root = node.right;
            if (holder.root != null) holder.root.parent = null;
        } else if (node.right == null) {
            holder.root = node.left;
            if (holder.root != null) holder.root.parent = null;
        } else {
            Node leftSubTree = node.left;
            leftSubTree.parent = null;
            node.left = null;

            Node rightSubTree = node.right;
            rightSubTree.parent = null;
            node.right = null;

            Node maxInLeft = leftSubTree;
            while (maxInLeft.right != null) {
                maxInLeft = maxInLeft.right;
            }
            splay(maxInLeft);
            maxInLeft.right = rightSubTree;
            rightSubTree.parent = maxInLeft;
            holder.root = maxInLeft;
        }

        holder.size--;
        return oldValue;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (!inRange(key)) return null;

        Node node = findNode(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        Integer key = (Integer) keyObj;
        if (!inRange(key)) return false;
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueInSubtree(holder.root, value);
    }

    private boolean containsValueInSubtree(Node p, Object value) {
        if (p == null) return false;
        if (inRange(p.key)) {
            if (value == null ? p.value == null : value.equals(p.value)) {
                return true;
            }
        }
        return containsValueInSubtree(p.left, value) || containsValueInSubtree(p.right, value);
    }

    @Override
    public int size() {
        if (fromKey == null && toKey == null) {
            return holder.size;
        }
        int count = 0;
        Node current = getFirstNode();
        while (current != null && inRange(current.key)) {
            count++;
            current = successor(current);
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        if (fromKey == null && toKey == null) {
            holder.root = null;
            holder.size = 0;
        } else {
            Node current = getFirstNode();
            while (current != null && inRange(current.key)) {
                Node next = successor(current);
                remove(current.key);
                current = next;
            }
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        return new MySplayMap(this.holder, this.fromKey, this.fromInclusive, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        return new MySplayMap(this.holder, fromKey, inclusive, this.toKey, this.toInclusive);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        return new MySplayMap(this.holder, fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public Integer firstKey() {
        Node node = getFirstNode();
        if (node == null || !inRange(node.key)) {
            throw new java.util.NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        Node node = getLastNode();
        if (node == null || !inRange(node.key)) {
            throw new java.util.NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public String toString() {
        String result = "{";
        boolean first = true;

        Node current = getFirstNode();
        while (current != null && inRange(current.key)) {
            if (!first) {
                result += ", ";
            }
            result += current.key + "=" + current.value;
            first = false;
            current = successor(current);
        }

        result += "}";
        return result;
    }

    // =========================================================================
    // Навигационные методы (lower, floor, ceiling, higher)
    // =========================================================================

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = holder.root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0) {
                if (inRange(current.key)) {
                    candidate = current.key;
                }
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return candidate;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = holder.root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp >= 0) {
                if (inRange(current.key)) {
                    candidate = current.key;
                }
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return candidate;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = holder.root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp <= 0) {
                if (inRange(current.key)) {
                    candidate = current.key;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return candidate;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        Node current = holder.root;
        Integer candidate = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                if (inRange(current.key)) {
                    candidate = current.key;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return candidate;
    }

    // =========================================================================
    // Вспомогательные навигационные методы
    // =========================================================================

    private Node getFirstNode() {
        Node p = holder.root;
        if (p == null) return null;
        while (p.left != null) {
            p = p.left;
        }
        while (p != null && !inRange(p.key)) {
            p = successor(p);
        }
        return p;
    }

    private Node getLastNode() {
        Node p = holder.root;
        if (p == null) return null;
        while (p.right != null) {
            p = p.right;
        }
        while (p != null && !inRange(p.key)) {
            p = predecessor(p);
        }
        return p;
    }

    private Node successor(Node t) {
        if (t == null) return null;
        if (t.right != null) {
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Node predecessor(Node t) {
        if (t == null) return null;
        if (t.left != null) {
            Node p = t.left;
            while (p.right != null) p = p.right;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    // =========================================================================
    // Остальные методы NavigableMap (Заглушки)
    // =========================================================================

    @Override public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Entry<Integer, String> firstEntry() { return null; }
    @Override public Entry<Integer, String> lastEntry() { return null; }
    @Override public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
}