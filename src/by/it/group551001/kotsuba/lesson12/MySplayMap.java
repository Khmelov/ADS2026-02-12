package by.it.group551001.kotsuba.lesson12;

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

    public MySplayMap() {
        this.root = null;
        this.size = 0;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
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
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
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
            } else {
                if (x == p.left && p == g.left) {
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
    }

    private Node findNode(Integer key) {
        if (key == null || root == null) return null;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrder(node.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        if (root == null) {
            root = new Node(key, value, null);
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
                String oldVal = current.value;
                current.value = value;
                splay(current);
                return oldVal;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;

        String oldVal = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else if (node.right == null) {
            root = node.left;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node leftSubtree = node.left;
            leftSubtree.parent = null;
            Node rightSubtree = node.right;
            rightSubtree.parent = null;

            Node maxLeft = leftSubtree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }

            root = leftSubtree;
            splay(maxLeft);

            maxLeft.right = rightSubtree;
            rightSubtree.parent = maxLeft;
            root = maxLeft;
        }
        size--;
        return oldVal;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node node = findByValue(root, value);
        if (node != null) {
            splay(node);
            return true;
        }
        return false;
    }

    private Node findByValue(Node node, Object value) {
        if (node == null) return null;
        if (value == null ? node.value == null : value.equals(node.value)) {
            return node;
        }
        Node res = findByValue(node.left, value);
        if (res != null) return res;
        return findByValue(node.right, value);
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
        MySplayMap subMap = new MySplayMap();
        collectRange(root, null, false, toKey, inclusive, subMap);
        return subMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap subMap = new MySplayMap();
        collectRange(root, fromKey, inclusive, null, false, subMap);
        return subMap;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node min = root;
        while (min.left != null) {
            min = min.left;
        }
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node max = root;
        while (max.right != null) {
            max = max.right;
        }
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) return null;
        Node current = root;
        Node best = null;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            if (current.key.compareTo(key) < 0) {
                best = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        } else if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) return null;
        Node current = root;
        Node best = null;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            int cmp = current.key.compareTo(key);
            if (cmp <= 0) {
                best = current;
                if (cmp == 0) break;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        } else if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) return null;
        Node current = root;
        Node best = null;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            int cmp = current.key.compareTo(key);
            if (cmp >= 0) {
                best = current;
                if (cmp == 0) break;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        } else if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) return null;
        Node current = root;
        Node best = null;
        Node lastVisited = null;
        while (current != null) {
            lastVisited = current;
            if (current.key.compareTo(key) > 0) {
                best = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (best != null) {
            splay(best);
            return best.key;
        } else if (lastVisited != null) {
            splay(lastVisited);
        }
        return null;
    }

    private void collectRange(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap target) {
        if (node == null) return;

        boolean aboveFrom = (fromKey == null) || (fromInclusive ? node.key.compareTo(fromKey) >= 0 : node.key.compareTo(fromKey) > 0);
        boolean belowTo = (toKey == null) || (toInclusive ? node.key.compareTo(toKey) <= 0 : node.key.compareTo(toKey) < 0);

        if (fromKey != null && node.key.compareTo(fromKey) < 0) {
            collectRange(node.right, fromKey, fromInclusive, toKey, toInclusive, target);
        } else if (toKey != null && node.key.compareTo(toKey) > 0) {
            collectRange(node.left, fromKey, fromInclusive, toKey, toInclusive, target);
        } else {
            collectRange(node.left, fromKey, fromInclusive, toKey, toInclusive, target);
            if (aboveFrom && belowTo) {
                target.put(node.key, node.value);
            }
            collectRange(node.right, fromKey, fromInclusive, toKey, toInclusive, target);
        }
    }

    public static class SplayEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private String value;

        public SplayEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() { return key; }

        @Override
        public String getValue() { return value; }

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

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        return k == null ? null : new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        return k == null ? null : new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        return k == null ? null : new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        return k == null ? null : new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        if (isEmpty()) return null;
        Integer k = firstKey();
        return new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        if (isEmpty()) return null;
        Integer k = lastKey();
        return new SplayEntry(k, get(k));
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        if (isEmpty()) return null;
        Integer k = firstKey();
        String v = remove(k);
        return new SplayEntry(k, v);
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        if (isEmpty()) return null;
        Integer k = lastKey();
        String v = remove(k);
        return new SplayEntry(k, v);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap subMap = new MySplayMap();
        collectRange(root, fromKey, fromInclusive, toKey, toInclusive, subMap);
        return subMap;
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
